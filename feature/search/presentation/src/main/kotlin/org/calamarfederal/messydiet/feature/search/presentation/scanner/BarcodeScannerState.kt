package org.calamarfederal.messydiet.feature.search.presentation.scanner

import android.content.Context
import android.util.Size
import androidx.camera.core.CameraState
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions.ZoomCallback
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.coroutines.cancellation.CancellationException

internal interface BarcodeScannerProcessor {
    val barcodeResultState: StateFlow<Result<List<Barcode>>?>
    operator fun invoke(inputImage: InputImage, onComplete: () -> Unit)

    companion object
}

internal fun BarcodeScannerProcessor.Companion.default(
    barcodeScanner: BarcodeScanner,
): BarcodeScannerProcessor = BarcodeScannerProcessorImplementation(
    barcodeScanner = barcodeScanner,
)

internal class BarcodeScannerProcessorImplementation(
    private val barcodeScanner: BarcodeScanner,
) : BarcodeScannerProcessor {
    private val _barcodeResultState = MutableStateFlow<Result<List<Barcode>>?>(null)
    override val barcodeResultState = _barcodeResultState.asStateFlow()

    override fun invoke(inputImage: InputImage, onComplete: () -> Unit) {
        barcodeScanner
            .process(inputImage)
            .addOnCompleteListener { task ->
                when {
                    task.isSuccessful -> _barcodeResultState.update { Result.success(task.result!!) }
                    task.isCanceled -> _barcodeResultState.update { Result.failure(CancellationException()) }
                    else -> _barcodeResultState.update { Result.failure(task.exception!!) }
                }
                onComplete()
            }
    }
}

internal class BarcodeScannerImageAnalyzerImplementation(
    private val detector: BarcodeScannerProcessor,
) : ImageAnalysis.Analyzer {
    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val inputImage = InputImage.fromMediaImage(
            image.image ?: return,
            image.imageInfo.rotationDegrees,
        )

        detector(inputImage) {
            image.close()
        }
    }

}

internal fun provideBarcodeOptions(
    maxZoomRatio: () -> Float,
    zoomCallback: ZoomCallback,
) = BarcodeScannerOptions
    .Builder()
    .setBarcodeFormats(
        Barcode.FORMAT_UPC_A,
        Barcode.FORMAT_UPC_E,
        Barcode.FORMAT_CODE_128,
        Barcode.FORMAT_CODE_93,
        Barcode.FORMAT_CODE_39,
        Barcode.FORMAT_EAN_13,
        Barcode.FORMAT_EAN_8,
        Barcode.FORMAT_CODABAR,
        Barcode.FORMAT_ITF,
    )
    .setZoomSuggestionOptions(
        ZoomSuggestionOptions
            .Builder(zoomCallback)
            .setMaxSupportedZoomRatio(maxZoomRatio())
            .build()
    )
    .build()

@Composable
internal fun rememberBarcodeScannerState(
    context: Context = LocalContext.current,
    lifecycleScope: CoroutineScope = LocalLifecycleOwner.current.lifecycleScope,
): BarcodeScannerState = remember(lifecycleScope) {
    BarcodeScannerStateImplementation(
        context = context,
        lifecycleScope = lifecycleScope,
    )
}

internal class BarcodeScannerStateImplementation(
    context: Context,
    lifecycleScope: CoroutineScope,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : BarcodeScannerState {
    private val barcodeScannerProcessor = BarcodeScannerProcessor
        .default(
            BarcodeScanning.getClient(
                provideBarcodeOptions(
                    maxZoomRatio = { 1f },
                    zoomCallback = { zoomRatio ->
                        if (cameraController.cameraInfo!!.cameraState.value!!.type == CameraState.Type.CLOSED)
                            false
                        else
                            true.also {
                                cameraController.cameraControl!!.setLinearZoom(zoomRatio)
                            }
                    },
                )
            )
        )


    override val cameraController: LifecycleCameraController by mutableStateOf(
        LifecycleCameraController(context).apply {
            setImageAnalysisAnalyzer(
                context.mainExecutor,
                BarcodeScannerImageAnalyzerImplementation(barcodeScannerProcessor),
            )
            imageAnalysisBackpressureStrategy = ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            imageAnalysisTargetSize = CameraController.OutputSize(Size(1280, 720))
        }
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    override val resultState = barcodeScannerProcessor
        .barcodeResultState
        .mapLatest {
            val barcode = it?.getOrNull()?.singleOrNull()?.rawValue ?: return@mapLatest null
            if (it.isFailure)
                Result.failure(it.exceptionOrNull() ?: return@mapLatest null)
            else
                Result.success(barcode)
        }
        .stateIn(
            scope = lifecycleScope + dispatcher,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

}
