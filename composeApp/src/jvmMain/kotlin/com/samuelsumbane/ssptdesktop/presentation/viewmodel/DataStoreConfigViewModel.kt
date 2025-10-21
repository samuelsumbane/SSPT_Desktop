package com.samuelsumbane.ssptdesktop.presentation.viewmodel

//import com.samuel.oremoschanganapt.AppSettings
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.toArgb
import com.russhwolf.settings.Settings
import com.samuelsumbane.ssptdesktop.apprepository.FontSizeName

//import com.samuel.oremoschanganapt.AppConfigs

enum class OremosLangs(val string: String) {
    ChanganaPT("ChanganaPT")
}

val OremosLangsMap = mapOf(
    OremosLangs.ChanganaPT.string to "Changana - Português"
)


sealed class ConfigEntry<T>(
    val key: String,
    val default: T,
    val saver: (Settings, String, T) -> Unit,
    val loader: (Settings, String, T) -> T
) {
    object ThemeColor : ConfigEntry<Int>(
        key = "theme_color",
        default = Blue.toArgb(),
        saver = { settings, k, v -> settings.putInt(k, v) },
        loader = { settings, k, d -> settings.getInt(k, d) }
    )

    object ThemeMode : ConfigEntry<String>(
        key = "theme_mode",
        default = "System",
        saver = { settings, k, v -> settings.putString(k, v) },
        loader = { settings, k, d -> settings.getString(k, d)}
    )

    object Locale : ConfigEntry<String>(
        key = "locale",
        default = "pt",
        saver = { settings, k, v -> settings.putString(k, v) },
        loader = { settings, k, d -> settings.getString(k, d)}
    )

    object ConfigFontSize : ConfigEntry<String>(
        key = "font_scale",
        default = FontSizeName.NORMAL.string,
        saver = { settings, k, v -> settings.putString(k, v) },
        loader = { settings, k, d -> settings.getString(k, d)}
    )

    object AppConfigLocationId : ConfigEntry<Int>(
        key = "system_location_id",
        default = 1,
        saver = { settings, k, v -> settings.putInt(k, v) },
        loader = { settings, k, d -> settings.getInt(k, d) }
    )
}


data class AppSettingsConfigs(
    val themeColor: Int,
    val themeMode: String,
    val locale: String,
    val fontSize: String,
    val systemLocationId: Int,
)

class ConfigScreenViewModel(private val settings: Settings) {

    fun loadConfigurations(): AppSettingsConfigs {
        return AppSettingsConfigs(
            themeColor = ConfigEntry.ThemeColor.loader(settings, ConfigEntry.ThemeColor.key,
                ConfigEntry.ThemeColor.default),
            themeMode = ConfigEntry.ThemeMode.loader(settings, ConfigEntry.ThemeMode.key,
                ConfigEntry.ThemeMode.default),
            locale = ConfigEntry.Locale.loader(settings, ConfigEntry.Locale.key,
                ConfigEntry.Locale.default),
            fontSize = ConfigEntry.ConfigFontSize.loader(settings, ConfigEntry.ConfigFontSize.key,
                ConfigEntry.ConfigFontSize.default),
            systemLocationId = ConfigEntry.AppConfigLocationId.loader(settings, ConfigEntry.AppConfigLocationId.key, ConfigEntry.AppConfigLocationId.default)
        )
    }

    fun <T> saveConfiguration(entry: ConfigEntry<T>, newValue: T) {
        entry.saver(settings, entry.key, newValue)
    }
}
