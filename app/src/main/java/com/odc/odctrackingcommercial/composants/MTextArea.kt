@file:OptIn(ExperimentalComposeUiApi::class)

package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MTextArea(label: String="", value: String?="",
              onChange: (String) -> Unit={},
              kb: KeyboardType = KeyboardType.Text,
){
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth().fillMaxHeight(.5f).heightIn(min=100.dp),
        value = value?: "",

        onValueChange = onChange,
        label = { Text(label) },
        placeholder = { Text("Saisir") },
        textStyle = MaterialTheme.typography.body1,
        singleLine = false,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(keyboardType = kb),
       /* keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Enter)
            }
        )*/
    )
}