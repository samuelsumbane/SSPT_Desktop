package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomFlowRow(
    maxItemsInEachRow: Int? = null,
    content: @Composable FlowRowScope.() -> Unit,
) {
    val scrollState = rememberScrollState()

    FlowRow(
        maxItemsInEachRow = maxItemsInEachRow ?: Int.MAX_VALUE,
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(scrollState)
    ) {
        content()
    }
}
