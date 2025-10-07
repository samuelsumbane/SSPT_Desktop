package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DropDown(
    text: String,
    expanded: Boolean = false,
    onDismiss: () -> Unit,
    onDropdownClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable { onDropdownClicked() }
    ) {
        Text(text)
        Text(" v")
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
    ) {

        content()
    }
}

@Composable
fun MenuItemText(text: String) {
    Text(text = text, color = MaterialTheme.colorScheme.background)
}