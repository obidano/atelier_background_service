package com.odc.odctrackingcommercial.composants

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

@Composable
@Preview
fun BackActionBtn(onBackClicked: () -> Unit={}) {
    IconButton(onClick = { onBackClicked() }) {
        Icon(Icons.Filled.ArrowBackIos, "",
            tint = MaterialTheme.colors.topAppBarContentColor)
    }
}