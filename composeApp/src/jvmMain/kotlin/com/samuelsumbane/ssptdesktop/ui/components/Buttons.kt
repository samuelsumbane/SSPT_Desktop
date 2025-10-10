package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ButtonContent(
    icon: (@Composable () -> Unit)? = null,
    text: String,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        icon?.invoke()
        Text(text)
    }
}

@Composable
fun NormalButton(
    icon: (@Composable () -> Unit)? = null,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        enabled = enabled
    ) {
        ButtonContent(icon, text)
    }
}

@Composable
fun NormalOutlineButton(
    icon: (@Composable () -> Unit)? = null,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = { onClick() },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
    ) {
        ButtonContent(icon, text)
    }
}

