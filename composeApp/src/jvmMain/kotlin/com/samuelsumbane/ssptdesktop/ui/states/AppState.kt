package com.samuelsumbane.ssptdesktop.ui.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object AppState {
    var formErrors = mutableStateMapOf<String, String>()
}
