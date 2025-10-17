package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun InfoCard(
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .padding(10.dp)
            .width(200.dp),
//            .background(MaterialTheme.colorScheme.secondary),
        elevation = CardDefaults.cardElevation(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color.Blue else colorScheme.secondary
        )
    ) {
        Column(
            modifier = Modifier.padding(10.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            content()
        }
    }
}