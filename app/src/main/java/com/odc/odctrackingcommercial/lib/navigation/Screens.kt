package com.odc.odctrackingcommercial.lib.navigation

import androidx.navigation.NavHostController
import com.odc.odctrackingcommercial.lib.utils.Action
import com.odc.odctrackingcommercial.lib.utils.Constantes

class Screens(navC: NavHostController) {

    val list: (Action) -> Unit = { action ->
        navC.navigate("list/${action.name}") {
            popUpTo(Constantes.LIST_SCREEN) { inclusive = true }
        }
    }

    val task: (Int) -> Unit = {
        navC.navigate("task/${it}")
    }


}