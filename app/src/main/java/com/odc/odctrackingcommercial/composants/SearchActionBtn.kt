package com.odc.odctrackingcommercial.composants

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

@Composable
@Preview(showBackground = false, widthDp = 300)
fun SearchActionBtn(onOpenSearchClick: () -> Unit = {}) {
    IconButton(onClick = { onOpenSearchClick() }) {
        Icon(Icons.Filled.Search, "", tint = MaterialTheme.colors.topAppBarContentColor)
    }
}