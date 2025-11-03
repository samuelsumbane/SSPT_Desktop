package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.ssptdesktop.ui.view.HomeScreen
import com.samuelsumbane.ssptdesktop.ui.view.Products.ProductsScreen
import com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage.LoginScreen

//import ssptdesktop.composeapp.generated.resources.compose_multiplatform

@Composable
//@Preview
fun App() {
//    val clientViewModel: ClientViewModel = getKoin().get()

    SSPTTheme(darkTheme = true) {
//        Navigator(SalesScreen())
//        Navigator(SaleModalScreen())

//        Navigator(ProductsScreen())
        Navigator(LoginScreen())
//        NotConnected()
    }
}