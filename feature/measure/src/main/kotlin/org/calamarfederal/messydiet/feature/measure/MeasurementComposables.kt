package org.calamarfederal.messydiet.feature.measure

import android.icu.number.LocalizedNumberFormatter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.calamarfederal.messydiet.measure.Weight
import org.calamarfederal.messydiet.measure.WeightUnit
import org.calamarfederal.messydiet.measure.inUnits
import org.calamarfederal.messydiet.measure.weightIn

@Composable
fun MeasuredUnitField(
    value: String,
    onValueChange: (String) -> Unit,
    unitLabel: String,
    unitChoices: List<String>,
    onUnitChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    unitPickerModifier: Modifier = Modifier,
    unitPickerEnabled: Boolean = true,
    includeDecimal: Boolean = true,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    placeholder: @Composable (() -> Unit)? = null,
    label: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        trailingIcon = {
            UnitPicker(
                unitText = unitLabel,
                unitOptions = unitChoices,
                onUnitChanged = onUnitChange,
                enabled = unitPickerEnabled,
                modifier = unitPickerModifier,
            )
        },
        placeholder = placeholder,
        label = label,
        supportingText = supportingText,
        enabled = enabled,
        readOnly = readOnly,
        isError = isError,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (includeDecimal) KeyboardType.Decimal else KeyboardType.Number,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}

@Composable
fun UnitPicker(
    unitText: String,
    unitOptions: List<String>,
    onUnitChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier,
    ) {
        var showOptions by remember(enabled) {
            mutableStateOf(false)
        }
        TextButton(
            enabled = enabled,
            onClick = { showOptions = true },
            contentPadding = ButtonDefaults.TextButtonWithIconContentPadding,
        ) {
            Text(
                text = unitText,
            )
            Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
            Icon(
                Icons.Default.ArrowDropDown,
                null,
                modifier = Modifier
                    .rotate(if (showOptions) 180f else 0f)
            )
        }
        DropdownMenu(
            expanded = showOptions,
            onDismissRequest = { showOptions = false }
        ) {
            for ((index, text) in unitOptions.withIndex()) {
                DropdownMenuItem(
                    text = { Text(text = text) },
                    onClick = { onUnitChanged(index); showOptions = false },
                )
            }
        }
    }

}

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
        set(value) { inputText = inputFilter(value) }
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
