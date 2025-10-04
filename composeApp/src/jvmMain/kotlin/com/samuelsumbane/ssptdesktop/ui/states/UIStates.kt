package com.samuelsumbane.ssptdesktop.ui.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType

object UIStates {
    var showFormDialog by mutableStateOf(false)
    var showAlertDialog by mutableStateOf(false)
    var alertTitle by mutableStateOf("")
    var alertText by mutableStateOf("")
    var alertType by mutableStateOf(AlertType.SUCCESS)
//    var alertAcceptFun by mutableStateOf( Unit )
    var alertAcceptFun: () -> Unit = {}

}