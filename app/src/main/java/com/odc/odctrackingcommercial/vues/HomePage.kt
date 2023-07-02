@file:OptIn(ExperimentalMaterialApi::class)

package com.odc.odctrackingcommercial.vues


import android.annotation.SuppressLint
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.odc.odctrackingcommercial.MainActivity
import com.odc.odctrackingcommercial.composants.*
import com.odc.odctrackingcommercial.lib.navigation.HomeScreens
import com.odc.odctrackingcommercial.lib.utils.*
import com.odc.odctrackingcommercial.vue_models.SharedVueModel
import com.odc.odctrackingcommercial.ui.theme.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "NewApi")
@Composable
fun HomePage(navC: NavHostController, shareVM: SharedVueModel) {
    // https://www.devbitsandbytes.com/configuring-snackbar-jetpack-compose-using-scaffold-with-bottom-navigation/
    val navCB = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    AddUserNameDialogVue(shareVM, scaffoldState)
    val context=LocalContext.current

    HandleLocalisationPermisssion(shareVM) {
        shareVM.locationH.checkLocationPermission( context as MainActivity)
    }
    HandleNotificationPermisssion {

    }

    Scaffold(
        bottomBar = { BottomNavigation(navCB) },
        scaffoldState = scaffoldState,
        snackbarHost = { scaffoldState.snackbarHostState }) {

        Box(Modifier.padding(it)) {
            NavHost(
                navController = navCB,
                startDestination = HomeScreens.Activites.route, route = "bottombar"
            ) {
                composable(HomeScreens.Activites.route) { ActivitesPage(navC, shareVM) }
                composable(HomeScreens.Articles.route) {
                    ArticlesPage(
                        navC,
                        shareVM,
                        navCB,
                        scaffoldState
                    )
                }
                composable(HomeScreens.Carte.route) {
                    CartePage(
                        navC,
                        shareVM,
                        navCB,
                        scaffoldState
                    )
                }
                composable(HomeScreens.Achat.route) {
                    AchatPage(
                        navC,
                        shareVM,
                        navCB,
                        scaffoldState
                    )
                }
                composable(HomeScreens.Vente.route) {
                    VentePage(
                        navC,
                        shareVM,
                        navCB,
                        scaffoldState
                    )
                }
            }

            DefaultSnackbar(
                snackbarHostState = scaffoldState.snackbarHostState,
                onDismiss = { scaffoldState.snackbarHostState.currentSnackbarData?.dismiss() },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }

    }

}


@Composable
fun BottomNavigation(navController: NavHostController = rememberNavController()) {
    val screens =
        listOf(
            HomeScreens.Activites,
            HomeScreens.Articles,
            HomeScreens.Carte,
            HomeScreens.Achat,
            HomeScreens.Vente
        )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.bottomBarBgColor,
        contentColor = MaterialTheme.colors.bottomBarTextColor
    ) {
        screens.forEach { screen ->
            AddItem(screen, currentDestination, navController)
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: HomeScreens,
    currentDestinaton: NavDestination?,
    navController: NavHostController,
) {
    BottomNavigationItem(label = { Text(screen.title) },
        icon = { Icon(screen.icon, "") },
        selected = currentDestinaton?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            if (currentDestinaton?.hierarchy?.any { it.route == screen.route } == true) return@BottomNavigationItem
            navController.navigate(screen.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }
        })
}

@Preview(showBackground = true)
@Composable
fun PreviewBottomNavigation() {
    BottomNavigation()
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DarkPreviewBottomNavigation() {
    BottomNavigation()
}
