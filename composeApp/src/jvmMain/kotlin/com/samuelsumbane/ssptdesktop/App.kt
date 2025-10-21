package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.ssptdesktop.kclient.UserSession
import com.samuelsumbane.ssptdesktop.ui.view.manager.BranchScreen
import com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage.UsersScreen
import com.samuelsumbane.ssptdesktop.ui.view.sell.SaleModalScreen
import com.samuelsumbane.ssptdesktop.ui.view.sell.SalesScreen
import com.samuelsumbane.ssptdesktop.ui.view.settings.NotificationsScreen
import com.samuelsumbane.ssptdesktop.ui.view.settings.SettingsPage
import com.samuelsumbane.ssptdesktop.ui.view.settings.SettingsScreen

//import ssptdesktop.composeapp.generated.resources.compose_multiplatform

@Composable
//@Preview
fun App() {
//    val clientViewModel: ClientViewModel = getKoin().get()

    SSPTTheme(darkTheme = true) {
//        Navigator(ProductsScreen())
//        Navigator(SaleModalScreen())
        Navigator(NotificationsScreen())
    }
}