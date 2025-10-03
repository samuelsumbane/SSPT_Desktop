package com.samuelsumbane.ssptdesktop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform