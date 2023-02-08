@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class)

package com.odc.odctrackingcommercial


import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.ui.theme.ODCTrackingCommercialTheme
import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.composants.ShowSnackBar
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.vues.IFormVentePage
import com.odc.odctrackingcommercial.vues.IVentePage
import com.odc.odctrackingcommercial.ui.theme.topAppBarBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

interface ITemplatePage {
    val naviguer: (String) -> Any
    val retourArriere: () -> Unit
}

@Composable
fun TemplatePage(
    navC: NavHostController,
    shareVM: SharedVueModel,
    navCB: NavHostController,
    scaffoldState: ScaffoldState,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = scaffoldState.snackbarHostState
    val keyboardController = LocalSoftwareKeyboardController.current

    var isSuccess by remember {
        mutableStateOf(false)
    }
    var notifMsg by remember {
        mutableStateOf("")
    }


    val fn = object : ITemplatePage {
        override val naviguer = { route: String ->
            navC.navigate(route)
        }

        override val retourArriere: () -> Unit = {
            navC.popBackStack()
        }
    }

    ShowSnackBar(snackbarHostState, notifMsg, isSuccess) {
        notifMsg = ""
        isSuccess = false
    }


    TemplateBody(scaffoldState, fn)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun TemplateBody(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    fn: ITemplatePage? = null,
) {
    val context = LocalContext.current
    Scaffold(scaffoldState = scaffoldState, topBar = { AppBar(fn) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


        }
    }

}

@Composable
private fun AppBar(fn: ITemplatePage?) {
    TopAppBar(
        /*  navigationIcon = {
              IconButton(onClick = { fn?.retourArriere?.invoke() }) {
                  Icon(
                      Icons.Default.Close,
                      "",
                      tint = MaterialTheme.colors.topAppBarContentColor
                  )
              }
          },*/
        title = {
            Text(
                "Template",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        elevation = 2.dp,
        backgroundColor = MaterialTheme.colors.topAppBarBackGroundColor,
        actions = {
            /* TextButton(modifier = Modifier,
                 onClick = { }) {
                 Icon(Icons.Filled.Add, "", tint = MaterialTheme.colors.topAppBarContentColor)
                 Text("Sauvegarder", color = MaterialTheme.colors.topAppBarContentColor)
             }*/
        }
    )
}


@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun PreviewDarkVentePage() {
    ODCTrackingCommercialTheme() {
        TemplateBody()
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewTemplatePage() {
    ODCTrackingCommercialTheme {
        TemplateBody()
    }
}


