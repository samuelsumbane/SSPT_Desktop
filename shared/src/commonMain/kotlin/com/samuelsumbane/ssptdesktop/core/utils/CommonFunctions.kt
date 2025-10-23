package com.samuelsumbane.ssptdesktop.core.utils
import java.io.FileOutputStream
import java.net.URL


fun String.cut(): String = if (this.length > 15) "${this.take(15)} ..." else this

fun downloadFile(url: String, destinationPath: String) {
    URL(url).openStream().use { input ->
        FileOutputStream(destinationPath).use { output ->
            input.copyTo(output)
        }
    }
}