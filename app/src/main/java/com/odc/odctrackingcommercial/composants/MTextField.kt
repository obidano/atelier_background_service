@file:OptIn(ExperimentalComposeUiApi::class)

package com.odc.odctrackingcommercial.composants

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
@Preview
fun MTextField(
    label: String = "", value: String? = "",
    onChange: (String) -> Unit = {}, kb: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false, fraction: Float = 1f, focus: Boolean = false,
    dialogFocus: Boolean = false,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val localView = LocalView.current
    LaunchedEffect(Unit) {
        if (focus) {
            Log.d("TAG", "MTextField: Request focus")
            focusRequester.requestFocus()
        }
        if (dialogFocus) {
            //awaitFrame()
            delay(700L)
            focusRequester.requestFocus()
            /* localView.viewTreeObserver.addOnWindowFocusChangeListener {
                 if (it) focusRequester.requestFocus()
             }*/
        }

    }
    TextField(
        modifier = Modifier
            .fillMaxWidth(fraction)
            .focusable(interactionSource = interactionSource)
            .focusRequester(focusRequester),
        value = value?: "",
        onValueChange = onChange,
        label = { Text(label) },
        readOnly = readOnly,
        placeholder = { Text("Saisir") },
        textStyle = MaterialTheme.typography.body1,
        singleLine = true,

        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        ),
        keyboardOptions = KeyboardOptions(keyboardType = kb, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() }),
    )
}