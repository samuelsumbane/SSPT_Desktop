package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun AppCheckBox(
    checked: Boolean,
    text: String,
    onCheck: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val colorScheme = MaterialTheme.colorScheme
        Checkbox(
            checked = checked,
            onCheckedChange = { onCheck(it) },
            colors = CheckboxDefaults.colors(
                checkedColor = colorScheme.primary,
                uncheckedColor = colorScheme.primary
            )
        )
        Text(text)
    }
}