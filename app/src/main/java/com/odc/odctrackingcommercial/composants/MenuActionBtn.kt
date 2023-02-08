package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.R
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

@Composable
@Preview(showBackground = false, widthDp = 300)
fun MenuActionBtn() {
    val expanded = remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded.value = true }) {
        Icon(
            painterResource(id = R.drawable.ic_vert),
            "",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded.value,
            onDismissRequest = { expanded.value = false }) {
            DropdownMenuItem(onClick = {
                expanded.value = false
            }) {
                Text("A propos", modifier = Modifier.padding(start = 15.dp))
            }

        }
    }
}