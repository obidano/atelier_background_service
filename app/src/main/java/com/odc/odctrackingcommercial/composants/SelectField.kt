@file:OptIn(ExperimentalMaterialApi::class)

package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.odc.odctrackingcommercial.models.SelectModel

@Composable
@Preview(showBackground = true)
fun SelectField(
    modifier: Modifier = Modifier,
    label: String = "",
    value: () -> String = { "" },
    onValueChange: (String) -> Unit = {},
    options: List<SelectModel> = emptyList(),
) {

    var selectedItem by remember {
        mutableStateOf(value())
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        // text field
       TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
           colors = TextFieldDefaults.textFieldColors(
               backgroundColor = Color.Transparent,
               unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
           ),
            // colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.Transparent).fillMaxWidth(.94f)
        ) {
            options.forEach { selectedOption ->
                // menu item
                DropdownMenuItem( modifier = Modifier.fillMaxWidth(.94f),onClick = {
                    selectedItem = selectedOption.label
                    onValueChange(selectedOption.value)
                    expanded = false
                }) {
                    Text(text = selectedOption.label)
                }
            }
        }
    }
}