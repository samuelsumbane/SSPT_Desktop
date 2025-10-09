package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingWidget(
    size: ProgressIndicatorSize = ProgressIndicatorSize.Medium,
    alternativeText: String? = null,
) {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        val iconDefaultColor = if (isSystemInDarkTheme()) Color.LightGray else Color.DarkGray

        CircularProgressIndicator(
            modifier = Modifier.width(size.dpSize).background(Color.Red),
//            color = ColorObject.mainColor,
            color = Color.Green,
            trackColor = iconDefaultColor,
        )

        Spacer(Modifier.height(20.dp))
//        Text("${stringResource(Res.string.loading)}...")
        Text(text = alternativeText ?: "Carregando")

    }
}


enum class ProgressIndicatorSize(val dpSize: Dp) {
    Small(30.dp), Medium(54.dp)
}