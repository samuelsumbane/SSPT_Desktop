package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.NavigationRail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.ssptdesktop.*
import com.samuelsumbane.ssptdesktop.ui.utils.IconPageScreen
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import com.samuelsumbane.ssptdesktop.ui.view.Products.ProductsScreen
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.*

@Composable
fun SideBar(navigator: Navigator, activePage: String = "") {
    val colorScheme = MaterialTheme.colorScheme

    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .width(85.dp),
        backgroundColor = colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .height(600.dp)
                    .background(Color.Gray, RoundedCornerShape(12.dp)),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val pageButtons = listOf(
                    IconPageScreen(painterResource(Res.drawable.home), PageName.HOME.itsName, HomeScreen()),
                    IconPageScreen(painterResource(Res.drawable.adjust_24), PageName.PARTNERS.itsName, PartnerModuleScreen()),
                    IconPageScreen(painterResource(Res.drawable.shopping_basket), PageName.SELL.itsName, SellModuleScreen()),
                    IconPageScreen(painterResource(Res.drawable.finance), PageName.FINANCE.itsName, FinanceModuleScreen()),
                    IconPageScreen(painterResource(Res.drawable.shopping_basket), PageName.PRODUCTS.itsName,
                        ProductsModuleScreen()),
                    IconPageScreen(painterResource(Res.drawable.manager), PageName.MANAGER.itsName, ManagerModuleScreen()),
                    IconPageScreen(painterResource(Res.drawable.bar_chart), PageName.REPORT.itsName, ReportModuleScreen()),
                    IconPageScreen(painterResource(Res.drawable.settings), PageName.SETTINGS.itsName, SettingsModuleScreen()),
                )

                for ((painter, text, screen) in pageButtons) {
                    val columnColors = if (activePage == text) colorScheme.primary else Color.Black
                    Column(
                        modifier = Modifier.clickable { navigator.push(screen) }
                    ) {
                        painter?.let {
                            Icon(
                                it, contentDescription = text,
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                tint = columnColors
                            )
                        }
                        Text(text, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
