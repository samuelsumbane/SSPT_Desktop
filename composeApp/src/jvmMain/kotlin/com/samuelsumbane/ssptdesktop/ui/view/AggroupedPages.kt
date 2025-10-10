package com.samuelsumbane.ssptdesktop.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure

data class IconAndPageName(
    val painter: Painter,
    val page: String,
    val screenDestination: Screen,
)

@Composable
fun AgroupedPages(title: String, pageLists: List<IconAndPageName>) {
    val navigator = LocalNavigator.currentOrThrow
    CommonPageStructure(navigator, pageTitle = title) {
        FlowRow(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 4
        ) {
            for((buttonIcon, buttonText, screenDestination) in pageLists) {
                Column(
                    modifier = Modifier
                        .padding(30.dp)
                        .size(150.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
                        .clickable { navigator.push(screenDestination) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    Icon(buttonIcon, contentDescription = buttonText)
                    Text(text = buttonText, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}