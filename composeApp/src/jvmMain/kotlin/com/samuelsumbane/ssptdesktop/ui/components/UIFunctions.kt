package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier

fun Modifier.possiblyVerticalScroll(enableScroll: Boolean, scrollState: ScrollState): Modifier {
    return if (enableScroll) {
        this.verticalScroll(scrollState)
    } else this
}