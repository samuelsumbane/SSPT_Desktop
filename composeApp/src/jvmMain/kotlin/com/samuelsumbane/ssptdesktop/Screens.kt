package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.ui.view.CategoriesPage
import com.samuelsumbane.ssptdesktop.ui.view.ClientsPage

class HomePage : Screen {
    @Composable
    override fun Content() {

    }
}


class ClientsScreen : Screen {
    @Composable
    override fun Content() {
        ClientsPage()
    }
}

class CategoriesScreen : Screen {
    @Composable
    override fun Content() {
        CategoriesPage()
    }
}