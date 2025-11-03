package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier

fun Modifier.possiblyVerticalScroll(
    enableScroll: Boolean,
    scrollState: ScrollState
): Modifier  = then(if (enableScroll) verticalScroll(scrollState) else this)


/**
 * Returns empty if String value in ("0", "0.0")
 * if not, returns the value itself
 */
fun <T: Number> T.toInputValue() = if (this in listOf(0, 0.0)) "" else this.toString()