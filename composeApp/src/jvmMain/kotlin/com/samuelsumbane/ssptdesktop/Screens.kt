package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.ui.ClientsPage

class HomePage : Screen {
    @Composable
    override fun Content() {

    }
}


class ClientsScreen(
    val clientViewModel: ClientViewModel
) : Screen {
    @Composable
    override fun Content() {
        ClientsPage(clientViewModel)
    }
}