package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator

import com.samuelsumbane.ssptdesktop.ui.view.sell.SalesScreen

//import ssptdesktop.composeapp.generated.resources.compose_multiplatform

@Composable
//@Preview
fun App() {
//    val clientViewModel: ClientViewModel = getKoin().get()

    SSPTTheme(darkTheme = false) {
        Navigator(SalesScreen())
//        Navigator(SaleModalScreen())
//        Navigator(NotificationsScreen())
    }
}