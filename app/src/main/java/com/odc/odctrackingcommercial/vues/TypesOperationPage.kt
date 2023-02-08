@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.odc.odctrackingcommercial.vues

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.lib.utils.Urls
import com.odc.odctrackingcommercial.ui.theme.*


@Composable
fun TypesOperationPage(navC: NavHostController) {
    val naviguer = { route: String ->
        navC.navigate(route)
    }

    val retourArriere: () -> Unit = {
        navC.popBackStack()
    }

    OperationBody(naviguer, retourArriere)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun OperationBody(naviguer: (String) -> Unit = {}, retourArriere: () -> Unit = {}) {
    val context = LocalContext.current
    Scaffold(topBar = {
        TypesOperationAppBar(retourArriere)
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            OperationOptionVue(
                title = "Articles",
                route = Urls.ArticlesPage.name,
                icon = Icons.Filled.LocalMall,
                naviguer
            )
            OperationOptionVue(
                title = "Declarer Achat",
                route = Urls.AchatPage.name,
                icon = Icons.Filled.ShoppingCart,
                naviguer
            )
            OperationOptionVue(
                title = "Declarer Vente",
                route = Urls.VentePage.name,
                icon = Icons.Filled.PointOfSale,
                naviguer
            )
        }
    }

}


@Composable
fun TypesOperationAppBar(retourArriere: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { retourArriere() }) {
                Icon(
                    Icons.Default.Close,
                    "",
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        title = {
            Text(
                "Operations",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        elevation = 0.dp, backgroundColor = MaterialTheme.colors.topAppBarBackGroundColor,
    )
}

@Composable
@Preview
fun OperationOptionVue(
    title: String = "Action 1",
    route: String = "",
    icon: ImageVector = Icons.Default.Timer,
    naviguer: (route: String) -> Unit = {},
) {
    Surface(modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.rowBackgroundColor,
        shape = RectangleShape,
        elevation = 2.dp,
        onClick = { naviguer(route) }) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(15.dp)) {
            Icon(icon, "", tint = MaterialTheme.colors.rowIconColor)
            Spacer(modifier = Modifier.width(10.dp))
            Text(title, color = MaterialTheme.colors.rowTextColor)
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Icon(Icons.Default.ArrowRight, "", tint = MaterialTheme.colors.rowIconColor)

            }
        }
    }

}


@Composable
@Preview(showBackground = true)
private fun PreviewTypesOperationPage() {
    ODCTrackingCommercialTheme {
        OperationBody()
    }
}
