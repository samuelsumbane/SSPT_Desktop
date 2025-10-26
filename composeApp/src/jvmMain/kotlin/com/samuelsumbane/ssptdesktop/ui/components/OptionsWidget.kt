package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp

@Composable
fun OptionsWidget(
    buttonText: String,
    optionExpanded: Boolean,
    onExpand: () -> Unit,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(optionExpanded) }
//    val on = optionExpanded()
    Box() {
        NormalOutlineButton(icon = null, text = buttonText) {
            expanded = !expanded
        }
        AnimatedVisibility(expanded) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpand() }
            ) {
                content()
            }
        }
    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DropdownMenuItemForOptions(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier
            .width(230.dp)
            .background(if (isHovered) MaterialTheme.colorScheme.primary else Color.Transparent)
            .onPointerEvent(PointerEventType.Enter) { isHovered = true }
            .onPointerEvent(PointerEventType.Exit) { isHovered = false }
    ) {
        content()
    }
}
