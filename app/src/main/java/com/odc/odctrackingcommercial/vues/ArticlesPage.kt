package com.odc.odctrackingcommercial.vues

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.ui.theme.ODCTrackingCommercialTheme
import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.composants.AddArticleButtonVue
import com.odc.odctrackingcommercial.composants.EmptyArticles
import com.odc.odctrackingcommercial.composants.ListArticlesVue
import com.odc.odctrackingcommercial.composants.ShowSnackBar
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.topAppBarBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

interface IArticlesPage {
    val ajouterArticle: () -> Unit
    val retourArriere: () -> Unit
}

interface IFormArticlesPage {
    var nom: String
    var prixVente: String
}

@Composable
fun ArticlesPage(
    navC: NavHostController,
    shareVM: SharedVueModel,
    navCB: NavHostController,
    scaffoldState: ScaffoldState,
) {
    val allArticles by shareVM.allArticles.collectAsState()
    val snackbarHostState = scaffoldState.snackbarHostState

    var isSuccess by remember {
        mutableStateOf(false)
    }
    var notifMsg by remember {
        mutableStateOf("")
    }


    val form = object : IFormArticlesPage {
        override var nom by remember {
            mutableStateOf("")
        }
        override var prixVente by remember {
            mutableStateOf("1")
        }
    }

    val fn = object : IArticlesPage {
        override val ajouterArticle = { ->
            val newData =
                ArticlesModel(
                    nom = form.nom,
                    prixVente = form.prixVente.toDouble()
                )

            shareVM.saveArticle(newData)
            notifMsg = "Article enregistré"
            isSuccess = true
            form.nom = ""
            form.prixVente = ""
        }

        override val retourArriere: () -> Unit = {
            navC.popBackStack()
        }

    }

    ShowSnackBar(snackbarHostState, notifMsg, isSuccess) {
        notifMsg = ""
        isSuccess = false
    }

    ArticlesBody(allArticles, fn, form)

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ArticlesBody(
    allArticles: List<ArticlesModel>,
    fn: IArticlesPage? = null,
    form: IFormArticlesPage? = null,
) {

    val context = LocalContext.current
    Scaffold(topBar = {
        ArticleAppBar(allArticles, fn, form)
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            if (allArticles.isEmpty()) {
                EmptyArticles()
            } else {
                ListArticlesVue(allArticles)
            }
        }
    }

}

@Composable
fun ArticleAppBar(
    allArticles: List<ArticlesModel>,
    fn: IArticlesPage?,
    form: IFormArticlesPage?,

    ) {
    TopAppBar(
       /* navigationIcon = {
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
                "Articles (${allArticles.size})",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        elevation = 2.dp, backgroundColor = MaterialTheme.colors.topAppBarBackGroundColor,
        actions = {
            AddArticleButtonVue(fn?.ajouterArticle ?: {}, form)
        }
    )
}


@Composable
@Preview(showBackground = true)
private fun PreviewArticlesPage() {
    ODCTrackingCommercialTheme {
        ArticlesBody(Constantes.FakeArticles)
    }
}


