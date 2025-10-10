package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun ErrorText(errorText: String) {
    Text(text = errorText, fontSize = 12.sp, color = Color.Red)
}

@Composable
fun CardPItem(key: String, value: String) {
    Text("$key: $value")
}