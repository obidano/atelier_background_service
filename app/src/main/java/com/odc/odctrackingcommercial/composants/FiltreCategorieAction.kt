package com.odc.odctrackingcommercial.composants

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.R
import com.odc.odctrackingcommercial.lib.utils.Categorie
import com.odc.odctrackingcommercial.ui.theme.Typography
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

@Composable
@Preview(showBackground = false, widthDp = 300)
fun FiltreCategorieAction(onFiltreClick: (Categorie) -> Unit = {}) {
    val expanded = remember {
        mutableStateOf(false)
    }
    IconButton(onClick = { expanded.value = true }) {
        Icon(
            painterResource(id = R.drawable.ic_filter),
            "",
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(expanded = expanded.value,
            onDismissRequest = { expanded.value = false }) {

            DropdownMenuItem(onClick = {
                expanded.value = false
                onFiltreClick(Categorie.All)
            }) {
                CategoryVueItem(Categorie.All)
            }

            DropdownMenuItem(onClick = {
                expanded.value = false
                onFiltreClick(Categorie.Depenses)
            }) {
                CategoryVueItem(Categorie.Depenses)
            }
            DropdownMenuItem(onClick = {
                expanded.value = false
                onFiltreClick(Categorie.Revenus)
            }) {
                CategoryVueItem(Categorie.Revenus)
            }

        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 300)
fun CategoryVueItem(type: Categorie = Categorie.Revenus) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = type.color)
        }

        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = type.display,
            style = Typography.subtitle1,
            color = MaterialTheme.colors.onSurface
        )
    }
}
