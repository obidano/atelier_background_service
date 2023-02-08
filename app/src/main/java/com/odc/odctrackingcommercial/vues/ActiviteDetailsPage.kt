@file:OptIn(ExperimentalComposeUiApi::class)

package com.odc.odctrackingcommercial.vues


import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.composants.BackActionBtn
import com.odc.odctrackingcommercial.lib.BackHandler
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.lib.utils.Urls
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.*
import kotlinx.coroutines.launch
import java.util.*

interface IActiviteDetailsPage {
    val naviguer: (String) -> Unit
    val retourArriere: () -> Unit
    val onUpdateClicked: () -> Unit
    val onCloseClicked: () -> Unit
    val onDeleteClicked: () -> Unit
}

@Composable
fun ActivitesDetailsPage(navC: NavHostController, shareVM: SharedVueModel, id: Int) {

    val snackbarHostState = remember { SnackbarHostState() }
    val scaffoldState: ScaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

    var snackText by remember {
        mutableStateOf("")
    }
    val selectedActivite by shareVM.selectedActivite.collectAsState()

    LaunchedEffect(selectedActivite) {
        shareVM.getSelectedActivite(id)
    }

    DisplaySnackBar(snackbarHostState, snackText)

    /*
    FN
     */

    val fn = object : IActiviteDetailsPage {

        override val naviguer = { route: String ->
            navC.navigate(route) {
                popUpTo(Urls.HomePage.name) { inclusive = true }
            }
        }

        override val retourArriere = {
            //navC.popBackStack()
            navC.navigate(Urls.HomePage.name) {
                popUpTo(Urls.HomePage.name) { inclusive = true }
            }
        }
        override val onUpdateClicked = {
            snackText = Constantes.dateFormatter2.format(Calendar.getInstance().time)
            /* scope.launch {
                 Log.d("TAG", "ActivitesDetailsPage: clicked ")

                 val snackbarResult = snackbarHostState.showSnackbar(
                     message = "Good to know",
                     actionLabel = "Fermer",
                     duration = SnackbarDuration.Short
                 )
                 Log.d("TAG", "DisplaySnackBar: $snackbarResult")
             }*/
        }
        override val onCloseClicked = {}
        override val onDeleteClicked = {}
    }

    BackHandler(onBack = fn.retourArriere)

    ActiviteDetailsBody(
        selectedActivite,
        scaffoldState = scaffoldState, fn
    )

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ActiviteDetailsBody(
    selectedActivite: ActivitesModel?,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    fn: IActiviteDetailsPage? = null,
) {
    val context = LocalContext.current
    Scaffold(scaffoldState = scaffoldState, topBar = {
        if (selectedActivite == null) {
            NewActDetailsAppBar(fn)
        } else {
            ExistingActDetailsAppBar(selectedActivite, fn)
        }

    }) {
        ActivityForm()
    }

}

@Composable
@Preview
fun DefaultActDetailsAppBar(retourArriere: () -> Unit = {}) {
    val colors: Colors = MaterialTheme.colors
    TopAppBar(
        navigationIcon = {
            BackActionBtn {
                retourArriere()
            }
        },
        title = { Text("", color = colors.topAppBarContentColor) },
        backgroundColor = colors.topAppBarBackGroundColor,
        actions = {

        }
    )
}

@Composable
@Preview
fun NewActDetailsAppBar(fn: IActiviteDetailsPage? = null) {
    val colors: Colors = MaterialTheme.colors
    TopAppBar(
        navigationIcon = {
            BackActionBtn {
                fn?.retourArriere?.invoke()
            }
        },
        title = { Text("Créer Activité", color = colors.topAppBarContentColor) },
        backgroundColor = colors.topAppBarBackGroundColor,
        actions = {
            AddActivityBtn {

            }
        }
    )
}

@Composable
@Preview
fun ExistingActDetailsAppBar(
    data: ActivitesModel = Constantes.FakeActivites[0],
    fn: IActiviteDetailsPage? = null,
) {
    val colors: Colors = MaterialTheme.colors
    TopAppBar(
        navigationIcon = {
            CloseBtn(fn)
        },
        title = {
            Text(
                data.article.nom,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = colors.topAppBarContentColor
            )
        },
        backgroundColor = colors.topAppBarBackGroundColor,
        actions = {
            CreateActivityBtn(fn)
            DeleteActionBtn()
            UpdateActionBtn(fn)
        }
    )
}

@Composable
@Preview
fun AddActivityBtn(onAddClicked: () -> Unit = {}) {
    IconButton(onClick = { onAddClicked() }) {
        Icon(Icons.Filled.Check, "", tint = MaterialTheme.colors.topAppBarContentColor)
    }
}

@Composable
@Preview
fun CreateActivityBtn(fn: IActiviteDetailsPage? = null) {
    IconButton(onClick = { fn?.naviguer?.invoke(Urls.DetailActivitePage.name + "/-1") }) {
        Icon(Icons.Filled.Add, "", tint = MaterialTheme.colors.topAppBarContentColor)
    }
}


@Composable
@Preview
fun CloseBtn(fn: IActiviteDetailsPage? = null) {
    IconButton(onClick = { fn?.onCloseClicked?.invoke() }) {
        Icon(Icons.Filled.Close, "", tint = MaterialTheme.colors.topAppBarContentColor)
    }
}

@Composable
@Preview
fun DeleteActionBtn(fn: IActiviteDetailsPage? = null) {
    IconButton(onClick = { fn?.onDeleteClicked?.invoke() }) {
        Icon(Icons.Filled.Delete, "", tint = MaterialTheme.colors.topAppBarContentColor)
    }
}

@Composable
@Preview
fun UpdateActionBtn(fn: IActiviteDetailsPage? = null) {
    IconButton(onClick = {
        fn?.onUpdateClicked?.invoke()
    }) {
        Icon(Icons.Filled.DoneAll, "", tint = MaterialTheme.colors.topAppBarContentColor)
    }
}

fun Modifier.bottomBorder(strokeWidth: Dp, color: Color) = composed(
    factory = {
        val density = LocalDensity.current

        val strokeWidthPx = density.run { strokeWidth.toPx() }

        Modifier.drawBehind {
            val width = size.width
            val height = size.height - strokeWidthPx / 2

            drawLine(
                color = color,
                start = Offset(x = 0f, y = height),
                end = Offset(x = width, y = height),
                strokeWidth = strokeWidthPx
            )
        }
    }
)

@Composable
@Preview
fun ArticleDropDown(
    article: ArticlesModel = Constantes.FakeArticles[0],
    onSelected: (ArticlesModel) -> Unit = {},
) {
    var expanded by remember {
        mutableStateOf(false)
    }
    val angle by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Row(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { expanded = true }
            /*.border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                shape = MaterialTheme.shapes.small
            )*/
            .bottomBorder(1.dp, MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(10.dp)
                .weight(1f)
        ) {
            drawCircle(color = Color.Red)
        }
        Text(
            article.nom,
            color = MaterialTheme.colors.rowTextColor,
            style = MaterialTheme.typography.subtitle2,
            modifier = Modifier.weight(8f)
        )

        IconButton(
            onClick = { expanded = true },
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(angle)
                .weight(1.5f)
        ) {
            Icon(Icons.Filled.ArrowDropDown, "")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.94f)
        ) {

            Constantes.FakeArticles.forEach {
                DropdownMenuItem(onClick = {
                    expanded = false
                    onSelected(it)
                }) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            it.nom,
                            color = MaterialTheme.colors.rowTextColor,
                            style = MaterialTheme.typography.subtitle2,
                            modifier = Modifier.weight(8f)
                        )
                    }
                }
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun ActivityForm() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val kb = KeyboardType.Text
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        Modifier
            .fillMaxSize()
            .padding(25.dp)
    ) {


        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = title,
            onValueChange = {
                if (it.length < 10)
                    title = it
            },
            label = { Text("Titre") },
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
        //Divider(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(10.dp))
        ArticleDropDown()
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize(.7f),
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            placeholder = { Text("Saisir") },
            textStyle = MaterialTheme.typography.body1,
            singleLine = false,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = kb)
        )

    }

}

@Composable
fun DisplaySnackBar(snackState: SnackbarHostState, snackText: String) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(snackText) {
        Log.d("TAG", "DisplaySnackBar: $snackText")
        scope.launch {
            val snackbarResult = snackState.showSnackbar(
                message = "Good to know",
                actionLabel = "Fermer",
                duration = SnackbarDuration.Short
            )
            Log.d("TAG", "DisplaySnackBar: $snackbarResult")
        }
    }

}

@Composable
@Preview(showBackground = true)
private fun PreviewActivitesDetailsPage() {
    ODCTrackingCommercialTheme {
        ActiviteDetailsBody(selectedActivite = Constantes.FakeActivites[0])
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun DarkPreviewActivitesDetailsPage() {

    ODCTrackingCommercialTheme {
        ActiviteDetailsBody(selectedActivite = Constantes.FakeActivites[0])
    }
}



