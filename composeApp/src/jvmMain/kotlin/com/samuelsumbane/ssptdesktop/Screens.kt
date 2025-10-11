package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.ui.utils.IconPageScreen
import com.samuelsumbane.ssptdesktop.ui.view.AgroupedPages
import com.samuelsumbane.ssptdesktop.ui.view.Products.CategoriesPage
import com.samuelsumbane.ssptdesktop.ui.view.Products.CategoriesScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.ClientsPage
import com.samuelsumbane.ssptdesktop.ui.view.Products.ProductsPage
import com.samuelsumbane.ssptdesktop.ui.view.Products.ProductsScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.ClientsScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.OwnersScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.ProOwnersPage
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.account_circle
import ssptdesktop.composeapp.generated.resources.branch
import ssptdesktop.composeapp.generated.resources.details
import ssptdesktop.composeapp.generated.resources.help_24

class HomeScreen : Screen {
    @Composable
    override fun Content() {

    }
}



// Groupped

class FinanceModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Finanças",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "C. Pagar", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.branch), "C. Receber", OwnersScreen()),
            )
        )
    }
}

class ManagerModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Gestão",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Usuários", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.branch), "Sucursais", OwnersScreen()),
            )
        )
    }
}

class PartnerModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Parceiros",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.details), "Clientes", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.help_24), "Proprietarios", OwnersScreen()),
            )
        )
    }
}

class ProductsModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Productos",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.details), "Productos", ProductsScreen()),
                IconPageScreen(painterResource(Res.drawable.help_24), "Categorias", CategoriesScreen()),
            )
        )
    }
}

class ReportModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Inventários",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Inv. de Vendas", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.branch), "Inv. de Estoques", OwnersScreen()),
            )
        )
    }
}

class SellModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Configurações",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Notificações", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.account_circle), "Conf. do sistema", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.account_circle), "Logs", ClientsScreen()),
            )
        )
    }
}

class SettingsModuleScreen : Screen {
    @Composable
    override fun Content() {
        TODO("Not yet implemented")
    }
}