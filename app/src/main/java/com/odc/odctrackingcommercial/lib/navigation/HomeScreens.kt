package com.odc.odctrackingcommercial.lib.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.odc.odctrackingcommercial.lib.utils.Urls

sealed class HomeScreens(val route: String, val title: String, val icon: ImageVector) {
    object Activites : HomeScreens(Urls.ActivitesPage.name, "Activités", Icons.Default.Home)
    object Articles : HomeScreens(Urls.ArticlesPage.name, "Articles", Icons.Default.Storage)
    object Carte : HomeScreens(Urls.CartePage.name, "Carte", Icons.Filled.PinDrop)
    object Achat : HomeScreens(Urls.AchatPage.name, "Achat", Icons.Default.ShoppingCart)
    object Vente : HomeScreens(Urls.VentePage.name, "Vente", Icons.Default.Wallet)
}