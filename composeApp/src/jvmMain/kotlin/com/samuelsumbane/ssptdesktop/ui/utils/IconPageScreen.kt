package com.samuelsumbane.ssptdesktop.ui.utils

import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.screen.Screen


data class IconPageScreen(
    val painter: Painter? = null,
    val page: String,
    val screenDestination: Screen,
)