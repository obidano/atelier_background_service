package com.odc.odctrackingcommercial.ui.theme


import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Indigo300 = Color(0xFF7986CB)
val Indigo600 = Color(0xFF3949AB)
val Red400 = Color(0xFFFF1744)

val Orange300 = Color(0xFFFFB74D)
val Orange600 = Color(0xFFEF6C00)

val LowPColor = Color(0xFF00C980)
val MediumPColor = Color(0xFFFFC114)
val HighPColor = Color(0xFFFF4646)
val NonePColor = Color(0xFFFFFFFF)

val AllActivitesColor = Color(0xFFFFFFFF)
val DepensesColor = Color(0xFFF44336)
val RevenusColor = Color(0xFF00C980)

val LightGray = Color(0xFFFCFCFC)
val MediumGray = Color(0xFF9C9C9C)
val MediumGray2 = Color(0xBC000000)
val DarkGray = Color(0xFF141414)

val Colors.topAppBarContentColor: Color
    @Composable
    get() = if (isLight) Color.Black else LightGray

val Colors.topAppBarBackGroundColor: Color
    @Composable
    get() = if (isLight) Color.White else Color.Black

val Colors.fabBackGroundColor: Color
    @Composable
    get() = if (isLight) Color.Black else Orange600

val Colors.iconTintColor: Color
    @Composable
    get() = if (isLight) Color.White else LightGray

val Colors.rowBackgroundColor: Color
    @Composable
    get() = if (isLight) Color.White else DarkGray

val Colors.rowBackgroundColor2: Color
    @Composable
    get() = if (isLight) LightGray else MediumGray2

val Colors.rowTextColor: Color
    @Composable
    get() = if (isLight) Color.Black else Color.White

val Colors.rowIconColor: Color
    @Composable
    get() = if (isLight) MediumGray else Color.White

val Colors.confirmButtonBgColor: Color
    @Composable
    get() = if (isLight) MediumGray2 else Orange600

val Colors.confirmButtonTextColor: Color
    @Composable
    get() = if (isLight) Color.White else Color.Black

val Colors.bottomBarBgColor: Color
    @Composable
    get() = if (isLight) Color.White else MaterialTheme.colors.primarySurface

val Colors.bottomBarTextColor: Color
    @Composable
    get() = if (isLight) Color.Black else Color.White

val Colors.cancelButtonBgColor: Color
    @Composable
    get() = Color.Transparent