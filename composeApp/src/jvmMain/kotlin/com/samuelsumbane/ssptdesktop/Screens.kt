package com.samuelsumbane.ssptdesktop

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.ui.utils.IconPageScreen
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import com.samuelsumbane.ssptdesktop.ui.view.AgroupedPages
import com.samuelsumbane.ssptdesktop.ui.view.Products.CategoriesScreen
import com.samuelsumbane.ssptdesktop.ui.view.Products.ProductsScreen
import com.samuelsumbane.ssptdesktop.ui.view.manager.BranchScreen
import com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage.UsersScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.ClientsScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.OwnersScreen
import com.samuelsumbane.ssptdesktop.ui.view.partners.SupplierScreen
import com.samuelsumbane.ssptdesktop.ui.view.report.SaleReportScreen
import com.samuelsumbane.ssptdesktop.ui.view.report.StocksScreen
import com.samuelsumbane.ssptdesktop.ui.view.sell.SalesScreen
import com.samuelsumbane.ssptdesktop.ui.view.settings.LogsScreen
import com.samuelsumbane.ssptdesktop.ui.view.settings.NotificationsScreen
import com.samuelsumbane.ssptdesktop.ui.view.settings.SettingsScreen
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.*

// Groupped

class FinanceModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Finanças",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "C. Pagar", ClientsScreen()),
                IconPageScreen(painterResource(Res.drawable.branch), "C. Receber", OwnersScreen()),
            ),
            activePage = PageName.FINANCE.itsName
        )
    }
}

class ManagerModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Gestão",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Usuários", UsersScreen()),
                IconPageScreen(painterResource(Res.drawable.branch), "Sucursais", BranchScreen()),
            ),
            activePage = PageName.MANAGER.itsName
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
                IconPageScreen(painterResource(Res.drawable.help_24), "Fornecedores", SupplierScreen()),
            ),
            activePage = PageName.PARTNERS.itsName
        )
    }
}

class ProductsModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Productos",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.shopping_basket), "Productos", ProductsScreen()),
                IconPageScreen(painterResource(Res.drawable.help_24), "Categorias", CategoriesScreen()),
            ),
            activePage = PageName.PRODUCTS.itsName
        )
    }
}

class ReportModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Inventários",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Inv. de Vendas", SaleReportScreen()),
                IconPageScreen(painterResource(Res.drawable.branch), "Inv. de Estoques", StocksScreen()),
            ),
            activePage = PageName.REPORT.itsName
        )
    }
}

class SellModuleScreen : Screen{
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Vendas",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Vendas", SalesScreen()),
            ),
            activePage = PageName.SELL.itsName
        )
    }
}

class SettingsModuleScreen : Screen {
    @Composable
    override fun Content() {
        AgroupedPages(
            title = "Configurações",
            pageLists = listOf(
                IconPageScreen(painterResource(Res.drawable.account_circle), "Notificações", NotificationsScreen()),
                IconPageScreen(painterResource(Res.drawable.account_circle), "Conf. do sistema", SettingsScreen()),
                IconPageScreen(painterResource(Res.drawable.account_circle), "Logs", LogsScreen()),
            ),
            activePage = PageName.SETTINGS.itsName
        )
    }
}