package com.samuelsumbane.ssptdesktop.kclient

object AppConfig {
    // Lite ------>>
//    private val enabledModules = setOf("sales", "stock")

    // Premium ------->>
    private val enabledModules = setOf(
        "sales",
        "stock",
        "categories",
        "clients",
        "products",
        "suppliers",
        "branches",
        "payables",
        "receivables",
        "owners",
        "notifications",
        "logs"
    )

    fun hasModule(module: String): Boolean {
        return enabledModules.contains(module)
    }
}