package com.odc.odctrackingcommercial.lib

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.odc.odctrackingcommercial.lib.navigation.Screens
import com.odc.odctrackingcommercial.lib.utils.Urls
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.vues.*


@Composable
fun BackHandler(enabled: Boolean = true, onBack: () -> Unit = {}) {
    // Safely update the current `onBack` lambda when a new one is provided
    val currentOnBack by rememberUpdatedState(onBack)
    // Remember in Composition a back callback that calls the `onBack` lambda
    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                currentOnBack()
            }
        }
    }
    // On every successful composition, update the callback with the `enabled` value
    SideEffect {
        backCallback.isEnabled = enabled
    }
    val backDispatcher = checkNotNull(LocalOnBackPressedDispatcherOwner.current) {
        "No OnBackPressedDispatcherOwner was provided via LocalOnBackPressedDispatcherOwner"
    }.onBackPressedDispatcher
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, backDispatcher) {
        // Add callback to the backDispatcher
        backDispatcher.addCallback(lifecycleOwner, backCallback)
        // When the effect leaves the Composition, remove the callback
        onDispose {
            backCallback.remove()
        }
    }
}

@Composable
fun Navigation(navC: NavHostController, shareVM: SharedVueModel) {


    NavHost(navController = navC, startDestination = Urls.IntroPage.name, route="root") {

        composable(Urls.IntroPage.name) {
            IntroPage(navC)
        }

        composable(Urls.HomePage.name) {
            HomePage(navC, shareVM)
        }

        composable(
            route = Urls.DetailActivitePage.name + "/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.IntType
            })
        ) {
            Log.d("", "Navigation: ${it.arguments?.getInt("id")}")
            val id = it.arguments!!.getInt("id")
            ActivitesDetailsPage(navC, shareVM, id)
        }

        composable(Urls.TypeOperationsPage.name) {
            TypesOperationPage(navC)
        }

      /*  composable(Urls.ArticlesPage.name) {
            ArticlesPage(navC, shareVM)
        }

        composable(Urls.AchatPage.name) {
            AchatPage(navC, shareVM)
        }

        composable(Urls.VentePage.name) {
            VentePage(navC, shareVM,)
        }*/

    }

}