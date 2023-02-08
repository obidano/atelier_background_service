package com.odc.odctrackingcommercial.composants

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.odc.odctrackingcommercial.lib.utils.RequestState
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.vues.IActivitesPage
import com.odc.odctrackingcommercial.ui.theme.topAppBarBackGroundColor
import com.odc.odctrackingcommercial.ui.theme.topAppBarContentColor

@Composable
//@Preview(showBackground = false)
fun DefaultHomeAppBar(
    fn: IActivitesPage? = null,
    listActivites: RequestState<List<ActivitesModel>>,
) {
    var total by remember {
        mutableStateOf(0)
    }
    if (listActivites is RequestState.Success) {
        total = listActivites.data.size
    }

    TopAppBar(
        title = { Text("Activités ($total)", color = MaterialTheme.colors.topAppBarContentColor) },
        elevation = 2.dp, backgroundColor = MaterialTheme.colors.topAppBarBackGroundColor,
        actions = {
            SearchActionBtn(fn?.onOpenSearchClick ?: {})
            FiltreCategorieAction(fn?.onFiltreClick ?: {})
            MenuActionBtn()
        }
    )
}
