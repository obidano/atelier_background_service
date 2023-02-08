@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalComposeUiApi::class
)

package com.odc.odctrackingcommercial.vues


import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.odc.odctrackingcommercial.ui.theme.ODCTrackingCommercialTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.odc.odctrackingcommercial.composants.*
import com.odc.odctrackingcommercial.lib.navigation.HomeScreens
import com.odc.odctrackingcommercial.lib.utils.Categorie
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.lib.utils.Constantes.dateFormatter
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.models.SelectModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.topAppBarBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor
import kotlinx.coroutines.launch
import java.util.*

interface IVentePage {
    val saveRevenuActivite: (IFormVentePage) -> Any
    val naviguerVersActivitePage: () -> Unit
    val retourArriere: () -> Unit
}

interface IFormVentePage {
    var articleSelectionne: ArticlesModel?
    var prixVente: String
    var montant: String
    var qte: String
    var description: String?
}

@Composable
fun VentePage(
    navC: NavHostController,
    shareVM: SharedVueModel,
    navCB: NavHostController,
    scaffoldState: ScaffoldState,
) {
    val allArticles = shareVM.allArticles.collectAsState().value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = scaffoldState.snackbarHostState
    val keyboardController = LocalSoftwareKeyboardController.current

    val fn = object : IVentePage {
        override val saveRevenuActivite = { form: IFormVentePage ->
            keyboardController?.hide()
            if (form.articleSelectionne == null ||
                (form.qte.toIntOrNull() ?: 0) <= 0
            ) {
                Toast.makeText(
                    context,
                    "Certains champs sont obligatoires",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.d("TAG", "VentePage: saving....")
                val newDAta = ActivitesModel(
                    description = form.description ?: "",
                    category = Categorie.Revenus,
                    article = form.articleSelectionne!!,
                    montant = form.montant.toDouble(),
                    quantite = form.qte.toInt() ?: 0,
                    date = dateFormatter.format(Calendar.getInstance().time),
                    identifiant = shareVM.identifiant!!
                )
                shareVM.saveActivite(newDAta)
                scope.launch {
                    val snackbarResult = snackbarHostState.showSnackbar(
                        message = "Vente enregistrée",
                        actionLabel = "Fermer",
                        duration = SnackbarDuration.Short
                    )
                    when (snackbarResult) {
                        SnackbarResult.Dismissed -> {
                            Log.d("TAG", "Dismissed: $snackbarResult")
                            naviguerVersActivitePage()
                        }
                        SnackbarResult.ActionPerformed -> {
                            Log.d("TAG", "ActionPerformed: $snackbarResult")
                            naviguerVersActivitePage()
                        }
                    }


                }


            }


        }

        override val naviguerVersActivitePage: () -> Unit = { ->
            navCB.navigate(HomeScreens.Activites.route) {
                popUpTo(navCB.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        override val retourArriere: () -> Unit = {
            navC.popBackStack()
        }
    }


    VenteBody(allArticles, scaffoldState, fn)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun VenteBody(
    allArticles: List<ArticlesModel>,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    fn: IVentePage? = null,
) {
    val context = LocalContext.current
    val form = object : IFormVentePage {
        override var articleSelectionne by remember {
            mutableStateOf<ArticlesModel?>(null)
        }

        override var prixVente by remember {
            mutableStateOf("")
        }

        override var montant by remember {
            mutableStateOf("")
        }

        override var qte by remember {
            mutableStateOf("")
        }

        override var description: String? by remember {
            mutableStateOf("")
        }
    }


    LaunchedEffect(form.articleSelectionne) {
        form.prixVente = form.articleSelectionne?.prixVente?.toString() ?: "0.0"
    }

    LaunchedEffect(form.articleSelectionne, form.qte) {
        val pv = form.articleSelectionne?.prixVente ?: 0.0
        val q = form.qte.toIntOrNull() ?: 0
        if (pv > 0) {
            form.montant = (q * pv).toString()
        }
    }



    Scaffold(scaffoldState = scaffoldState, topBar = { VenteAppBar(fn, form) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            SelectField(
                label = "Article",
                value = {
                    if (form.articleSelectionne != null) "${form.articleSelectionne!!.id}" else ""
                },
                onValueChange = { newValue ->
                    form.articleSelectionne =
                        allArticles.find { it.id.toString().equals(newValue) }
                },
                options = allArticles.map { SelectModel(label = it.nom, value = "${it.id}") })
            Spacer(modifier = Modifier.height(20.dp))

            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                MTextField(
                    "Quantité Vendue", "${form.qte}",
                    onChange = { form.qte = it },
                    kb = KeyboardType.Number, fraction = 0.5f, focus = true
                )
                Spacer(modifier = Modifier.width(20.dp))
                MTextField(
                    "Prix Vente Unit",
                    form.prixVente,
                    onChange = { form.prixVente = it }, readOnly = true,
                    kb = KeyboardType.Number, fraction = 1f
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            MTextField(
                "Montant total", form.montant, readOnly = true
            )
            Spacer(modifier = Modifier.height(20.dp))
            MTextArea("Description", form.description ?: "",
                onChange = { form.description = it })
            Spacer(modifier = Modifier.height(100.dp))


        }
    }

}

@Composable
fun VenteAppBar(fn: IVentePage?, form: IFormVentePage) {
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
                "Vente",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.topAppBarBackGroundColor,
        actions = {
            TextButton(modifier = Modifier,
                onClick = { fn?.saveRevenuActivite?.invoke(form) }) {
                Icon(Icons.Filled.Add, "", tint = MaterialTheme.colors.topAppBarContentColor)
                Text("Sauvegarder", color = MaterialTheme.colors.topAppBarContentColor)
            }
        }
    )
}


@Composable
@Preview(showBackground = true)
private fun PreviewVentePage() {
    ODCTrackingCommercialTheme {
        VenteBody(Constantes.FakeArticles)
    }
}

@Composable
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun PreviewDarkVentePage() {
    ODCTrackingCommercialTheme {
        VenteBody(Constantes.FakeArticles)
    }
}


