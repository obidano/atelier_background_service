package com.odc.odctrackingcommercial.lib.utils

import androidx.compose.ui.graphics.Color
import com.odc.odctrackingcommercial.ui.theme.*

enum class Categorie(val color: Color, val display: String) {
    All(AllActivitesColor, "Tous"),
    Depenses(DepensesColor, "Depenses"),
    Revenus(RevenusColor, "Revenus"),
}