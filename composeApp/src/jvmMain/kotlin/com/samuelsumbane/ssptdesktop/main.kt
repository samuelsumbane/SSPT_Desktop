package com.samuelsumbane.ssptdesktop

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.Settings
import org.koin.core.context.startKoin
import java.util.prefs.Preferences

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "SSPT Desktop",
        alwaysOnTop = true,
        state = WindowState(
            size = DpSize(1080.dp , 700.dp)
        )
    ) {
        startKoin {
            modules(appModule)
        }
        App()
    }
}



@Composable
fun SSPTTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF0AB40E),
            secondary = Color.DarkGray,
            onSecondary = Color.White,
            tertiary = Color.White
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF0CE83C),
            secondary = Color.Gray,
            tertiary = Color.Black
        )
    }

    MaterialTheme(colorScheme, content = content)
}

 fun createSettings(): Settings {
    val prefs = Preferences.userRoot().node("sspt_desktop")
    return PreferencesSettings(prefs)
}

//val configViewModel = remember { ConfigScreenViewModel(createSettings()) }
//
//LaunchedEffect(Unit) {
//    val configurations = configViewModel.loadConfigurations()
//
//    appLocale = configurations.locale
//    Locale.setDefault(Locale(appLocale))
