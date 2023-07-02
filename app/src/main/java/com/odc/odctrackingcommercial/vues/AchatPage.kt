@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalComposeUiApi::class
)

package com.odc.odctrackingcommercial.vues


import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.ui.theme.ODCTrackingCommercialTheme
import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.MainActivity
import com.odc.odctrackingcommercial.composants.*
import com.odc.odctrackingcommercial.lib.utils.Categorie
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.lib.utils.Constantes.dateFormatter
import com.odc.odctrackingcommercial.lib.utils.Urls
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.models.SelectModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.topAppBarBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor
import java.util.*

interface IAchatPage {
    val saveDepenseActivite: () -> Unit

    val naviguerVersHomePage: () -> Unit
    val retourArriere: () -> Unit
}

interface IFormAchatPage {
    var articleSelectionne: ArticlesModel?
    var prixUnitAchat: String
    var montant: String
    var qte: String
    var description: String?
}

@Composable
fun AchatPage(
    navC: NavHostController,
    shareVM: SharedVueModel,
    navCB: NavHostController,
    scaffoldState: ScaffoldState,
) {

    val allArticles = shareVM.allArticles.collectAsState().value
    val context = LocalContext.current
    val snackbarHostState = scaffoldState.snackbarHostState
    val keyboardController = LocalSoftwareKeyboardController.current

    var isSuccess by remember {
        mutableStateOf(false)
    }
    var notifMsg by remember {
        mutableStateOf("")
    }


    val form = object : IFormAchatPage {
        override var articleSelectionne by remember {
            mutableStateOf<ArticlesModel?>(null)
        }

        override var prixUnitAchat by remember {
            mutableStateOf("")
        }

        override var montant by remember {
            mutableStateOf("")
        }

        override var qte by remember {
            mutableStateOf("")
        }

        override var description: String? by remember {
            mutableStateOf("".repeat(100))
        }
    }
    LaunchedEffect(form.articleSelectionne, form.prixUnitAchat, form.qte) {
        val pa = form.prixUnitAchat.toDoubleOrNull() ?: 0.0
        val q = form.qte.toIntOrNull() ?: 0
        if (pa > 0) {
            form.montant = (q * pa).toString()
        }
    }

    LaunchedEffect(Unit ){
        shareVM.locationH.openGpsService(context as MainActivity)
    }

    val fn = object : IAchatPage {
        override val saveDepenseActivite = { ->
            keyboardController?.hide()

            if (form.articleSelectionne == null ||
                (form.prixUnitAchat.toDoubleOrNull() ?: 0.0) <= 0 ||
                (form.qte.toIntOrNull() ?: 0) <= 0
            ) {
                Toast.makeText(
                    context,
                    "Certains champs sont obligatoires",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val newDAta = ActivitesModel(
                    description = form.description ?: "",
                    category = Categorie.Depenses,
                    prixUnitAchat = form.prixUnitAchat.toDoubleOrNull() ?: 0.0,
                    article = form.articleSelectionne!!,
                    montant = form.montant.toDoubleOrNull() ?: 0.0,
                    quantite = form.qte.toIntOrNull() ?: 0,
                    date = dateFormatter.format(Calendar.getInstance().time),
                    identifiant = shareVM.identifiant!!
                )
                isSuccess = true
                notifMsg = "Achat Enregistré"
                shareVM.saveActivite(newDAta)

            }


        }
        override val naviguerVersHomePage = { ->
            navC.navigate(Urls.HomePage.name) {
                popUpTo(Urls.HomePage.name) {
                    inclusive = true
                }
            }
        }
        override val retourArriere: () -> Unit = {
            navC.popBackStack()

        }

    }

    ShowSnackBar(snackbarHostState, notifMsg,isSuccess) {
        notifMsg = ""
        if(isSuccess){
            fn.naviguerVersHomePage.invoke()
        }
        isSuccess = false

    }

    AchatBody(allArticles, fn, form)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun AchatBody(
    allArticles: List<ArticlesModel>,
    fn: IAchatPage? = null,
    form: IFormAchatPage? = null,

    ) {


    Scaffold(topBar = { AchatAppBar(fn, form) }) {
//        val focusManager = LocalFocusManager.current
//        val bringIntoViewRequester = BringIntoViewRequester()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            SelectField(
                label = "Article",
                value = {
                    if (form?.articleSelectionne != null) "${form.articleSelectionne!!.id}" else ""
                },
                onValueChange = { newValue ->
                    form?.articleSelectionne =
                        allArticles.find { it.id.toString().equals(newValue) }
                },
                options = allArticles.map { SelectModel(label = it.nom, value = "${it.id}") })
            Spacer(modifier = Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                MTextField(
                    "Quantité Achetée", form?.qte,
                    onChange = { form?.qte = it },
                    kb = KeyboardType.Number, fraction = 0.5f, focus = true
                )
                Spacer(modifier = Modifier.width(20.dp))
                MTextField(
                    "Prix Unit",
                    form?.prixUnitAchat ?: "",
                    onChange = { form?.prixUnitAchat = it },
                    kb = KeyboardType.Number, fraction = 1f
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            MTextField(
                "Montant total", form?.montant, readOnly = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            MTextArea("Description", form?.description,
                onChange = { form?.description = it })
            Spacer(modifier = Modifier.height(100.dp))

        }
    }

}

@Composable
fun AchatAppBar(fn: IAchatPage? = null, form: IFormAchatPage?) {
    TopAppBar(
        /*   navigationIcon = {
               BackActionBtn(fn?.retourArriere ?: {})
           },*/
        title = {
            Text(
                "Achat",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        elevation = 2.dp, backgroundColor = MaterialTheme.colors.topAppBarBackGroundColor,
        actions = {
            TextButton(modifier = Modifier,
                onClick = { fn?.saveDepenseActivite?.invoke() }) {
                Icon(Icons.Filled.Add, "", tint = MaterialTheme.colors.topAppBarContentColor)
                Text("Sauvegarder", color = MaterialTheme.colors.topAppBarContentColor)
            }
        }
    )
}


@Composable
@Preview(showBackground = true)
private fun PreviewAchatPage() {
    ODCTrackingCommercialTheme {
        AchatBody(Constantes.FakeArticles)
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun PreviewDarkAchatPage() {
    ODCTrackingCommercialTheme {
        AchatBody(Constantes.FakeArticles)
    }
}


