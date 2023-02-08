package com.odc.odctrackingcommercial.lib.utils

import androidx.compose.ui.graphics.Color
import com.odc.odctrackingcommercial.ui.theme.HighPColor
import com.odc.odctrackingcommercial.ui.theme.LowPColor
import com.odc.odctrackingcommercial.ui.theme.MediumPColor
import com.odc.odctrackingcommercial.ui.theme.NonePColor

enum class Priority(val color: Color) {
    High(HighPColor),
    Medium(MediumPColor),
    Low(LowPColor),
    None(NonePColor)
}