package com.samuelsumbane.ssptdesktop.ui.view.sell

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.OrdersViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.SaleViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DataTable
import com.samuelsumbane.ssptdesktop.ui.components.DatatableText
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import org.koin.java.KoinJavaComponent.getKoin

class SalesScreen : Screen {
    @Composable
    override fun Content() {
        SalesPage()
    }
}


@Composable
fun SalesPage() {
    val salesViewModel by remember { mutableStateOf(getKoin().get<SaleViewModel>()) }
    val  ordersViewModel by remember { mutableStateOf(getKoin().get<OrdersViewModel>()) }
    val salesUIStates by salesViewModel.uiState.collectAsState()
    val ordersUIStates by ordersViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }
//    var clientTelephone by remember { mutableStateOf("") }


    CommonPageStructure(
        navigator,
        pageTitle = "Vendas",
        topBarActions = {
            NormalButton(icon = null, text = "Nova Venda") {
                navigator.push(SaleModalScreen())
            }
        }
    ) {

        DataTable(
            headers = listOf("Cliente", "Total", "Data e hora", "Status", "Usuário", "Sucursal"),
            rows = ordersUIStates.orders
        ) { order ->
            DatatableText(order.clientName ?: "Sem cliente")
            DatatableText(order.total.toString())
            DatatableText(order.orderDateTime ?: "")
            DatatableText(order.status)
            DatatableText(order.userName)
            DatatableText(order.branchName)
        }

//        AnimatedVisibility(clientUIStates.common.showAlertDialog) {
//            AlertWidget(
//                clientUIStates.common.alertTitle,
//                clientUIStates.common.alertText,
//                clientUIStates.common.alertType,
//                onDismiss = { salesViewModel.openAlertDialog(false) },
//            ) {
//                clientUIStates.common.alertOnAccept()
//            }
//        }
    }
}