package com.samuelsumbane.ssptdesktop.kclient

data class StatusAndMessage(
    val status: Int,
    val message: String,
)

data class StatusAndMessage2<T>(
    val status: Int,
    val message: T,
)
