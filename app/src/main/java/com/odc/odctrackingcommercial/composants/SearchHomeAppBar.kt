package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.lib.utils.TrailingIconState
import com.odc.odctrackingcommercial.vues.IActivitesPage
import com.odc.odctrackingcommercial.ui.theme.topAppBarBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

@Composable
@Preview(showBackground = false, widthDp = 300)
fun SearchHomeAppBar(
    searchText: String = "",
    fn: IActivitesPage?=null,

    ) {
    val trailingState = remember {
        mutableStateOf(TrailingIconState.ReadyToDelete)
    }
    val focusRequester = FocusRequester()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .height(56.dp), elevation = 4.dp,
        color = MaterialTheme.colors.topAppBarBackGroundColor
    ) {
        TextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            value = searchText,
            onValueChange = fn?.onTextSearchChange ?:{},
            placeholder = {
                Text(
                    "Rechercher",
                    color = Color.White,
                    modifier = Modifier.alpha(ContentAlpha.medium)
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(ContentAlpha.disabled),
                    onClick = { /*TODO*/ }) {
                    Icon(
                        Icons.Filled.Search, "",
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    when (trailingState.value) {
                        TrailingIconState.ReadyToDelete -> {
                            fn?.onTextSearchChange?.invoke("")
                            trailingState.value = TrailingIconState.ReadyToClose
                        }
                        TrailingIconState.ReadyToClose -> {
                            if (searchText.isNotEmpty()) {
                                fn?.onTextSearchChange?.invoke("")
                            } else {
                                fn?.onSearchCloseClick?.invoke()
                                trailingState.value = TrailingIconState.ReadyToDelete
                            }
                        }
                    }
                }) {
                    Icon(Icons.Filled.Close, "", tint = MaterialTheme.colors.topAppBarContentColor)

                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {

            }),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }

}