package com.samuelsumbane.ssptdesktop.core.utils

fun String.cut(): String = if (this.length > 15) "${this.take(15)} ..." else this
