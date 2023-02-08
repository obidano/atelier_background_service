package com.odc.odctrackingcommercial.vues


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.composants.*
import com.odc.odctrackingcommercial.lib.utils.*
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.*

interface IActivitesPage {
    val naviguerVersDetail: (Int) -> Unit
    val naviguerVersOperation: () -> Unit
    val onOpenSearchClick: () -> Unit
    val onFiltreClick: (Categorie) -> Unit
    val onTextSearchChange: (String) -> Unit
    val onSearchCloseClick: () -> Unit
}

@Composable
fun ActivitesPage(navC: NavHostController, shareVM: SharedVueModel) {
    val allActivites by shareVM.allActivites.collectAsState()
    val fn = object : IActivitesPage {
        override val naviguerVersDetail = { id: Int ->
            navC.navigate("${Urls.DetailActivitePage.name}/${id}")
        }
        override val naviguerVersOperation = {
            navC.navigate(Urls.TypeOperationsPage.name)
        }
        override val onOpenSearchClick = {
            shareVM.searchAppBarState.value = SearchAppBarState.Opened
        }
        override val onFiltreClick: (Categorie) -> Unit = { p: Categorie -> }
        override val onTextSearchChange: (String) -> Unit = { text: String ->
            shareVM.searchText.value = text
        }

        override val onSearchCloseClick = {
            shareVM.searchText.value = ""
            shareVM.searchAppBarState.value = SearchAppBarState.Closed
        }

    }




    ActivitesBody(
        allActivites,
        shareVM.searchText.value,
        shareVM.searchAppBarState.value,
        fn
    )

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun ActivitesBody(
    listActivites: RequestState<List<ActivitesModel>>,
    searchText: String,
    searchState: SearchAppBarState = SearchAppBarState.Closed,
    fn: IActivitesPage? = null,
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            when (searchState) {
                SearchAppBarState.Opened -> SearchHomeAppBar(
                    searchText,
                    fn
                )
                else -> DefaultHomeAppBar(fn, listActivites)
            }
        },
        //floatingActionButton = { FloatingBtn(fn) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (listActivites is RequestState.Success) {
                if (listActivites.data.isEmpty()) {
                    EmptyActivites()
                } else {
                    ListActivitesVue(listActivites.data, fn)
                }
            }


        }
    }

}


@Composable
@Preview(showBackground = true)
private fun PreviewActivitesPage() {
    ODCTrackingCommercialTheme {
        ActivitesBody(
            listActivites = RequestState.Success(Constantes.FakeActivites),
            searchText = "",
        )
    }
}


