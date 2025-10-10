package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.ui.view.AgroupedPages
import com.samuelsumbane.ssptdesktop.ui.view.IconAndPageName
import com.samuelsumbane.ssptdesktop.ui.view.Products.CategoriesPage
import com.samuelsumbane.ssptdesktop.ui.view.partners.ClientsPage
import com.samuelsumbane.ssptdesktop.ui.view.Products.ProductsPage
import com.samuelsumbane.ssptdesktop.ui.view.partners.ProOwnersPage
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.details
import ssptdesktop.composeapp.generated.resources.help_24

class HomeScreen : Screen {
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

class OwnersScreen : Screen {
    @Composable
    override fun Content() {
        ProOwnersPage()
    }
}

class CategoriesScreen : Screen {
    @Composable
    override fun Content() {
        CategoriesPage()
    }
}

class ProductsScreen : Screen {
    @Composable
    override fun Content() {
        ProductsPage()
    }
}


// Groupped

class FinanceModuleScreen : Screen {
    @Composable
    override fun Content() {

    }
}

class ManagerModuleScreen : Screen {
    @Composable
    override fun Content() {

    }
}

class PartnerModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Parceiros",
            pageLists = listOf(
                IconAndPageName(painterResource(Res.drawable.details), "Clientes", ClientsScreen()),
                IconAndPageName(painterResource(Res.drawable.help_24), "Proprietarios", OwnersScreen()),
            )
        )
    }
}

class ProductsModuleScreen : Screen {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }
}

class ReportModuleScreen : Screen {
    @Composable
    override fun Content() {

    }
}

class SellModuleScreen : Screen {
    @Composable
    override fun Content() {

    }
}

class SettingsModuleScreen : Screen {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }
}