package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropDown(
    label: String,
    text: String,
    errorText: String? = null,
    expanded: Boolean = false,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onDropdownClicked: () -> Unit,
    content: @Composable () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth(0.9f)
    ) {
        Column(
            modifier = Modifier
    //            .background(Color.Tra)
                .fillMaxWidth()
                .border(width = 0.5.dp, color = Color.Gray, shape = RoundedCornerShape(6.dp) )
                .clickable { onDropdownClicked() }
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(0.95f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label)
                Text(" v")
            }
            Text(text)
        }

        errorText?.let { ErrorText(it) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = Modifier.background(MaterialTheme.colorScheme.onBackground)
        ) { content() }
    }
}

@Composable
fun MenuItemText(text: String) {
    Text(text = text, color = MaterialTheme.colorScheme.background)
}