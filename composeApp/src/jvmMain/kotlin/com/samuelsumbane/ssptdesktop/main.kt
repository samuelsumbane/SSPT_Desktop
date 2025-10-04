package com.samuelsumbane.ssptdesktop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SSPT Desktop",
        alwaysOnTop = true
    ) {
        startKoin {
            modules(appModule)
        }
        App()
    }
}




@Composable
fun SPPTTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF027EB1),
            secondary = Color.DarkGray,
            tertiary = Color.White
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF0788C9),
            secondary = Color.Gray,
            tertiary = Color.Black
        )
    }

    MaterialTheme(colorScheme, content = content)
}