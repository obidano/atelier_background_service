package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.vues.IActivitesPage
import com.odc.odctrackingcommercial.ui.theme.fabBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.iconTintColor

@Composable
@Preview(showBackground = false, widthDp = 300, heightDp = 300)
fun FloatingBtn(fn: IActivitesPage?=null) {
    FloatingActionButton(
        onClick = { fn?.naviguerVersOperation?.invoke() },
        backgroundColor = MaterialTheme.colors.fabBackGroundColor
    ) {
        Icon(
            Icons.Filled.Inventory,
            "",
            tint = MaterialTheme.colors.iconTintColor,
            modifier = Modifier.size(20.dp)
        )
    }
}