package org.calamarfederal.messydiet.feature.measure

import android.icu.number.LocalizedNumberFormatter
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.calamarfederal.messydiet.diet_data.model.Portion
import org.calamarfederal.messydiet.measure.*

/**
 * State holder for [Portion] Input. Designed to be used with [MeasuredUnitField]
 */
@Stable
interface PortionInputState {
    var input: String
    val weightUnit: WeightUnit?
    val volumeUnit: VolumeUnit?

    val isInWeight: Boolean
    val isInVolume: Boolean

    val weightUnitChoices: List<WeightUnit>
    val volumeUnitChoices: List<VolumeUnit>

    val portionFlow: Flow<Portion?>

    fun setInputFromPortion(portion: Portion, formatter: LocalizedNumberFormatter)

    fun changeToWeightUnit(index: Int)
    fun changeToVolumeUnit(index: Int)

    fun forceWeightUnit(unit: WeightUnit)
    fun forceVolumeUnit(unit: VolumeUnit)

    companion object {
        operator fun invoke(
            initialInput: String = "",
            initialWeightUnit: WeightUnit = WeightUnit.Gram,
            weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
            volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
        ): PortionInputState = PortionInputStateImplementation(
            initialInput = initialInput,
            initialWeightUnit = initialWeightUnit,
            weightUnitChoices = weightUnitChoices,
            volumeUnitChoices = volumeUnitChoices,
        )

        operator fun invoke(
            initialInput: String = "",
            initialVolumeUnit: VolumeUnit = VolumeUnit.Milliliter,
            weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
            volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
        ): PortionInputState = PortionInputStateImplementation(
            initialInput = initialInput,
            initialVolumeUnit = initialVolumeUnit,
            weightUnitChoices = weightUnitChoices,
            volumeUnitChoices = volumeUnitChoices,
        )
    }
}

internal class PortionInputStateImplementation private constructor(
    initialInput: String = "",
    initialWeightUnit: WeightUnit? = WeightUnit.Gram,
    initialVolumeUnit: VolumeUnit? = null,
    override val weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
    override val volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
) : PortionInputState {
    constructor(
        initialInput: String = "",
        initialVolumeUnit: VolumeUnit = VolumeUnit.Milliliter,
        weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
        volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
    ) : this(
        initialInput = initialInput,
        initialWeightUnit = null,
        initialVolumeUnit = initialVolumeUnit,
        weightUnitChoices = weightUnitChoices,
        volumeUnitChoices = volumeUnitChoices
    )

    constructor(
        initialInput: String = "",
        initialWeightUnit: WeightUnit = WeightUnit.Gram,
        weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
        volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
    ) : this(
        initialInput = initialInput,
        initialWeightUnit = initialWeightUnit,
        initialVolumeUnit = null,
        weightUnitChoices = weightUnitChoices,
        volumeUnitChoices = volumeUnitChoices
    )

    private var inputText by mutableStateOf(initialInput)
    override var input: String
        get() = inputText
        set(value) {
            inputText = value
        }

    private var _weightUnit: WeightUnit? by mutableStateOf(initialWeightUnit)
    private var _volumeUnit: VolumeUnit? by mutableStateOf(initialVolumeUnit)
    override var weightUnit: WeightUnit?
        get() = _weightUnit
        private set(value) {
            _volumeUnit = null
            _weightUnit = value
        }
    override var volumeUnit: VolumeUnit?
        get() = _volumeUnit
        private set(value) {
            _volumeUnit = value
            _weightUnit = null
        }

    override val isInVolume: Boolean by derivedStateOf { volumeUnit != null }
    override val isInWeight: Boolean by derivedStateOf { weightUnit != null }

    /**
     * Get the lable
     */

    override val portionFlow: Flow<Portion?> = combine(
        snapshotFlow { inputText },
        snapshotFlow { weightUnit },
        snapshotFlow { volumeUnit },
    ) { str, weight, volume ->
        val num = str.toDoubleOrNull() ?: return@combine null
        when {
            weight != null && volume == null -> Portion(weight.weightOf(num))
            weight == null && volume != null -> Portion(volume.volumeOf(num))
            else -> null
        }
    }

    override fun setInputFromPortion(portion: Portion, formatter: LocalizedNumberFormatter) {
        val weightUnitLocal = weightUnit
        val volumeUnitLocal = volumeUnit

        require(volumeUnitLocal != null || weightUnitLocal != null)
        val number = when {
            weightUnitLocal != null && portion.weight != null -> portion.weight!!.inUnits(weightUnitLocal)
            volumeUnitLocal != null && portion.volume != null -> portion.volume!!.inUnits(volumeUnitLocal)
            else -> throw (IllegalStateException("weightUnit or volumeUnit does not match with input portion"))
        }

        input = formatter.format(number).toString()
    }

    override fun changeToWeightUnit(index: Int) {
        weightUnit = weightUnitChoices[index]
    }

    override fun changeToVolumeUnit(index: Int) {
        volumeUnit = volumeUnitChoices[index]
    }

    override fun forceWeightUnit(unit: WeightUnit) {
        weightUnit = unit
    }

    override fun forceVolumeUnit(unit: VolumeUnit) {
        volumeUnit = unit
    }
}

val PortionInputState.Companion.saver
    get() = listSaver<PortionInputState, String>(
        save = { state ->
            listOf(
                state.input,
                state.weightUnit?.ordinal.toString(),
                state.volumeUnit?.ordinal.toString(),
                state.weightUnitChoices.size.toString(),
                state.volumeUnitChoices.size.toString(),
                *state.weightUnitChoices.map { it.ordinal.toString() }.toTypedArray(),
                *state.volumeUnitChoices.map { it.ordinal.toString() }.toTypedArray(),
            )
        },
        restore = { data ->
            val weightChoices = data[3].toInt()
            val volumeChoices = data[4].toInt()
            val weightUnit = data[1].toIntOrNull()?.let { WeightUnit.entries[it] }
            val volumeUnit = data[2].toIntOrNull()?.let { VolumeUnit.entries[it] }
            require((weightUnit != null || volumeUnit != null) && (weightUnit == null || volumeUnit == null))
            require(data.size == 5 + weightChoices + volumeChoices)
            if (weightUnit != null && volumeUnit == null) {
                PortionInputStateImplementation(
                    initialInput = data[0],
                    initialWeightUnit = weightUnit,
                    weightUnitChoices = data.subList(5, 5 + weightChoices).map { WeightUnit.entries[it.toInt()] },
                    volumeUnitChoices = data.subList(5 + weightChoices, 5 + weightChoices + volumeChoices)
                        .map { VolumeUnit.entries[it.toInt()] },
                )
            } else if (volumeUnit != null && weightUnit == null) {
                PortionInputStateImplementation(
                    initialInput = data[0],
                    initialVolumeUnit = volumeUnit,
                    weightUnitChoices = data.subList(5, 5 + weightChoices).map { WeightUnit.entries[it.toInt()] },
                    volumeUnitChoices = data.subList(5 + weightChoices, 5 + weightChoices + volumeChoices)
                        .map { VolumeUnit.entries[it.toInt()] },
                )
            } else {
                throw (IllegalStateException("Portion needs to have exactly one of weightUnit and volumeUnit not null"))
            }
        },
    )

@Composable
fun rememberPortionInputState(
    initialInput: String = "",
    initialVolumeUnit: VolumeUnit = VolumeUnit.Milliliter,
    weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
    volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
) = rememberSaveable(saver = PortionInputState.saver) {
    PortionInputState(
        initialInput = initialInput,
        initialVolumeUnit = initialVolumeUnit,
        weightUnitChoices = weightUnitChoices,
        volumeUnitChoices = volumeUnitChoices,
    )
}

@Composable
fun rememberPortionInputState(
    initialInput: String = "",
    initialWeightUnit: WeightUnit = WeightUnit.Gram,
    weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
    volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
) = rememberSaveable(saver = PortionInputState.saver) {
    PortionInputState(
        initialInput = initialInput,
        initialWeightUnit = initialWeightUnit,
        weightUnitChoices = weightUnitChoices,
        volumeUnitChoices = volumeUnitChoices,
    )
}

/**
 * State holder for Weight Input. Designed to be used to [MeasuredUnitField]
 */
@Stable
interface WeightInputState {
    var input: String
    val weightUnit: WeightUnit
    val weightUnitChoices: List<WeightUnit>

    val weightFlow: Flow<Weight?>

    fun setInputFromWeight(weight: Weight, formatter: LocalizedNumberFormatter)

    fun changeWeightUnitByIndex(index: Int)

    fun forceWeightUnit(unit: WeightUnit)

    companion object {
        operator fun invoke(): WeightInputState = WeightInputStateImpl()
    }
}

internal class WeightInputStateImpl constructor(
    initialWeightUnit: WeightUnit = WeightUnit.Gram,
    override val weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
    private val inputFilter: (String) -> String = { it },
) : WeightInputState {
    private var inputText by mutableStateOf("")
    override var input
        get() = inputText
        set(value) {
            inputText = inputFilter(value)
        }
    override var weightUnit by mutableStateOf(initialWeightUnit)
        internal set

    override val weightFlow = combine(snapshotFlow { input }, snapshotFlow { weightUnit }) { str, unit ->
        str.toDoubleOrNull()?.weightIn(unit)
    }

    override fun setInputFromWeight(weight: Weight, formatter: LocalizedNumberFormatter) {
        inputText = formatter.format(weight.inUnits(weightUnit)).toString()
    }

    override fun changeWeightUnitByIndex(index: Int) {
        weightUnit = weightUnitChoices[index]
    }

    override fun forceWeightUnit(unit: WeightUnit) {
        weightUnit = unit
    }
}

val WeightInputState.Companion.saver
    get() = listSaver<WeightInputState, String>(
        save = { state ->
            listOf(
                state.input,
                state.weightUnit.ordinal.toString(),
                *state.weightUnitChoices.map { it.ordinal.toString() }.toTypedArray()
            )
        },
        restore = {
            WeightInputStateImpl(
                initialWeightUnit = WeightUnit.entries[it[1].toInt()],
                weightUnitChoices = it.drop(2).map { s -> WeightUnit.entries[s.toInt()] },
            )
        },
    )

@Composable
fun rememberWeightInputState(
    initialWeightUnit: WeightUnit,
    initialValue: String = "",
    weightUnitChoices: List<WeightUnit> = WeightUnit.entries,
) = rememberSaveable(saver = WeightInputState.saver) {
    WeightInputStateImpl(
        initialWeightUnit = initialWeightUnit,
        weightUnitChoices = weightUnitChoices,
    ).apply { input = initialValue }
}

/**
 * State holder for Volume Input. Designed to be used to [MeasuredUnitField]
 */
@Stable
interface VolumeInputState {
    var input: String
    val volumeUnit: VolumeUnit
    val volumeUnitChoices: List<VolumeUnit>

    val volumeFlow: Flow<Volume?>

    fun setInputFromVolume(volume: Volume, formatter: LocalizedNumberFormatter)

    fun changeVolumeUnitByIndex(index: Int)

    fun forceVolumeUnit(unit: VolumeUnit)

    companion object
}

val VolumeInputState.Companion.saver
    get() = listSaver<VolumeInputState, String>(
        save = { state ->
            listOf(
                state.input,
                state.volumeUnit.ordinal.toString(),
                *state.volumeUnitChoices.map { it.ordinal.toString() }.toTypedArray(),
            )
        },
        restore = { data ->
            VolumeInputStateImplementation(
                initialInput = data[1],
                initialVolumeUnit = VolumeUnit.entries[data[2].toInt()],
                volumeUnitChoices = data.drop(2).map { str -> VolumeUnit.entries[str.toInt()] }
            )
        },
    )


@Composable
fun rememberVolumeInputState(
    initialInput: String = "",
    initialVolumeUnit: VolumeUnit = VolumeUnit.Milliliter,
    volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
) = rememberSaveable(saver = VolumeInputState.saver) {
    VolumeInputStateImplementation(
        initialInput = initialInput,
        initialVolumeUnit = initialVolumeUnit,
        volumeUnitChoices = volumeUnitChoices
    )
}

internal class VolumeInputStateImplementation(
    initialInput: String = "",
    initialVolumeUnit: VolumeUnit = VolumeUnit.Milliliter,
    override val volumeUnitChoices: List<VolumeUnit> = VolumeUnit.entries,
) : VolumeInputState {
    private var inputText by mutableStateOf(initialInput)
    override var input: String
        get() = inputText
        set(value) {
            inputText = value
        }

    override var volumeUnit: VolumeUnit by mutableStateOf(initialVolumeUnit)
        private set

    override val volumeFlow: Flow<Volume?> = combine(
        snapshotFlow { inputText },
        snapshotFlow { volumeUnit },
    ) { str, unit ->
        str.toDoubleOrNull()?.let { unit.volumeOf(it) }
    }

    override fun setInputFromVolume(volume: Volume, formatter: LocalizedNumberFormatter) {
        inputText = formatter.format(volume.inUnits(volumeUnit)).toString()
    }

    override fun changeVolumeUnitByIndex(index: Int) {
        volumeUnit = volumeUnitChoices[index]
    }

    override fun forceVolumeUnit(unit: VolumeUnit) {
        volumeUnit = unit
    }
}
