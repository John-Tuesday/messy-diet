package org.calamarfederal.messydiet.feature.search.presentation.scanner

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import org.calamarfederal.messydiet.feature.search.presentation.R

@Stable
interface BarcodeScannerState {
    val cameraController: LifecycleCameraController
    val resultState: StateFlow<Result<String>?>
}

@Composable
internal fun BarcodeScannerScreen(
    onBack: () -> Unit,
    onBarcodeFound: (String) -> Unit,
    context: Context = LocalContext.current,
    barcodeScannerState: BarcodeScannerState = rememberBarcodeScannerState(context = context),
) {
    var cameraPermissionGranted by remember(context) {
        mutableStateOf(
            PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA,
            )
        )
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        cameraPermissionGranted = isGranted
    }
    LaunchedEffect(cameraPermissionGranted) {
        if (!cameraPermissionGranted)
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    if (cameraPermissionGranted) {
        val cameraController = barcodeScannerState.cameraController
        val barcodeResultState by barcodeScannerState.resultState.collectAsStateWithLifecycle()

        val hapticFeedback = LocalHapticFeedback.current

        LaunchedEffect(barcodeResultState) {
            barcodeResultState?.onSuccess { barcode ->
                if (barcode.isNotBlank()) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    onBarcodeFound(barcode)
                }
            }
        }

        BarcodeScannerScreenContent(
            cameraController = cameraController,
        )
    } else {
        require(!cameraPermissionGranted)
        CameraPermissionDeniedScreen(
            onBack = onBack,
        )
    }
}

@Composable
private fun CameraPermissionDeniedScreen(
    onBack: () -> Unit,
    context: Context = LocalContext.current,
) {
    AlertDialog(
        onDismissRequest = onBack,
        properties = DialogProperties(
            dismissOnClickOutside = true,
            dismissOnBackPress = true,
        ),
        title = {
            Text(
                text = stringResource(id = R.string.camera_permission_denied_title),
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.camera_permission_denied_body),
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    context.startActivity(
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                    )
                },
            ) {
                Text(text = stringResource(id = R.string.app_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onBack) {
                Text(text = stringResource(id = R.string.camera_permission_denied_back))
            }
        }
    )
}

@Composable
internal fun BarcodeScannerScreenContent(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraController: LifecycleCameraController = LocalContext.current.let { context ->
        remember { LifecycleCameraController(context) }
    },
) {
    Scaffold(
        modifier = modifier,
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            CameraPreviewSurface(
                lifecycleOwner = lifecycleOwner,
                cameraController = cameraController,
            )
        }
    }
}

@Composable
private fun CameraPreviewSurface(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    cameraController: LifecycleCameraController = LocalContext.current.let { context ->
        remember { LifecycleCameraController(context) }
    },
) {
    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.BLUE)
                scaleType = PreviewView.ScaleType.FILL_START

                controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier.fillMaxSize(),
    )
}
