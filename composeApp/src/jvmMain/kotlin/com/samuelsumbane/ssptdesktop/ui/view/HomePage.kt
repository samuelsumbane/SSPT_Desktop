package com.samuelsumbane.ssptdesktop.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.CustomFlowRow
import com.samuelsumbane.ssptdesktop.ui.components.TextRow
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage.UserProfileScreen
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.account_circle
import ssptdesktop.composeapp.generated.resources.home
import ssptdesktop.composeapp.generated.resources.notifications

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        HomePage()
    }
}


@Composable
fun HomePage() {

    val navigator = LocalNavigator.currentOrThrow

    CommonPageStructure(
        navigator,
        pageTitle = "Dashboard",
        activePage = PageName.HOME.itsName,
        topBarActions = {
            Row(Modifier.padding(end = 10.dp)) {
                IconButton(
                    onClick = {
                        navigator.push(UserProfileScreen())
                    }
                ) {
                    Icon(painterResource(Res.drawable.account_circle), "")
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(painterResource(Res.drawable.notifications), "")
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(painterResource(Res.drawable.home), "")
                }



            }
        }
    ) {
        val scrollState = rememberScrollState()

        Row(
            modifier = Modifier
                .padding(top = 50.dp, bottom = 50.dp)
                .fillMaxWidth(0.7f)
                .height(160.dp)
                .align(Alignment.CenterHorizontally)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            val data = mapOf(
                "Usuários" to mapOf(
                    "Ativos" to "2",
                    "Todos" to "10"
                ),
                "Parceiros" to mapOf(
                    "Clientes" to "2",
                    "Fornecedores" to "0"
                ),
                "Ganhos Totais" to mapOf(
                    "Vendas" to "2.0 MT",
                    "Lucros" to "0.0 MT"
                )
            )

            for ((title, dataItems) in data) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color.Transparent.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                        .padding(10.dp) // Internal padding
                ) {
                    Text(title, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 20.dp))

                    TextRow(dataItems.keys.first(), dataItems.values.first())
                    TextRow(dataItems.keys.last(), dataItems.values.last())
                }
            }

        }


        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            repeat(4) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .size(600.dp, 500.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(15.dp))
                ) {

                }
            }
        }
    }

}



//C.div {
//    chartDiv("chart1", theme) {
//        Canvas(attrs = { id("monthlySalesQuantities") })
//    }
//
//    chartDiv("chart2", theme) {
//        Canvas(attrs = { id("topSales") })

//        Canvas(attrs = { id("topUsers") })
//    }
//
//    chartDiv("chart4", theme) {
//        Canvas(attrs = { id("monthlyProfits") })
//    }
