package org.calamarfederal.messydiet.measure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

/**
 * [OutlinedTextField] and [UnitPicker] combined and specialized for input numbers and choosing units.
 */
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

/**
 * Display chosen unit and off Drop Down Menu to change it
 */
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
