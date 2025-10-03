package com.samuelsumbane.ssptdesktop

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