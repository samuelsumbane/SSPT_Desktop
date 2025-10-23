package com.samuelsumbane.ssptdesktop.ui.view.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.core.utils.downloadFile
import com.samuelsumbane.ssptdesktop.kclient.apiPath
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.SaleReportViewModel
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DataTable
import com.samuelsumbane.ssptdesktop.ui.components.DatatableText
import com.samuelsumbane.ssptdesktop.ui.components.DropdownMenuItemForOptions
import com.samuelsumbane.ssptdesktop.ui.components.OptionsWidget
import com.samuelsumbane.ssptdesktop.ui.components.TextAndIconItem
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete

class SaleReportScreen : Screen {
    @Composable
    override fun Content() {
        SalesReports()
    }
}

@Composable
fun SalesReports() {
    val navigator = LocalNavigator.currentOrThrow
    val salesReportViewModel by remember { mutableStateOf(getKoin().get<SaleReportViewModel>()) }
    val salesReportUiState by salesReportViewModel.uiState.collectAsState()

    CommonPageStructure(
        navigator,
        pageTitle = "Relátorio de vendas",
        activePage = PageName.REPORT.itsName,
        topBarActions = {

            Row(horizontalArrangement = Arrangement.spacedBy(15.dp)) {

                OptionsWidget(buttonText = "Vendas de hoje") {
                    DropdownMenuItemForOptions(
                        {

                        }
                    ) {
                        TextAndIconItem(text = "Filtrar", icon = painterResource(Res.drawable.delete))
                    }

                    DropdownMenuItemForOptions (onClick = {}) {
                        TextAndIconItem(text = "Filtrar", icon = painterResource(Res.drawable.delete))
                    }
                }

                OptionsWidget(buttonText = "Exportar") {
                    DropdownMenuItemForOptions(
                        onClick = {
                            val url = "$apiPath/order/export/orders"
                            val destination = "orders.csv" // ou caminho completo ex.: "/home/samuel/Downloads/orders.csv"
                            downloadFile(url, destination)
                        }
                    ) {
                        TextAndIconItem(text = "CSV", icon = painterResource(Res.drawable.delete))
                    }

                    DropdownMenuItemForOptions (onClick = {}) {
                        TextAndIconItem(text = "Excel", icon = painterResource(Res.drawable.delete))
                    }

                    DropdownMenuItemForOptions (onClick = {}) {
                        TextAndIconItem(text = "CSV", icon = painterResource(Res.drawable.delete))
                    }
                }
            }
//            OptionsWidget(
//                buttonText = "Vendas de hoje"
//            ) {
//                DropdownMenuItemForOptions(
//                    {
//
//                    }
//                ) {
//                    TextAndIconItem(text = "Filtrar", icon = painterResource(Res.drawable.delete))
//                }
//
//                DropdownMenuItemForOptions (onClick = {}) {
//                    TextAndIconItem(text = "Filtrar", icon = painterResource(Res.drawable.delete))
//                }
//            }
//
//            OptionsWidget(
//                buttonText = "Exportar >"
//            ) {
//                DropdownMenuItemForOptions(
//                    {
//
//                    }
//                ) {
//                    TextAndIconItem(text = "PDF", icon = painterResource(Res.drawable.delete))
//                }
//
//                DropdownMenuItemForOptions (onClick = {}) {
//                    TextAndIconItem(text = "Excel", icon = painterResource(Res.drawable.delete))
//                }
//
//                DropdownMenuItemForOptions (onClick = {}) {
//                    TextAndIconItem(text = "CSV", icon = painterResource(Res.drawable.delete))
//                }
//            }

        }
    ) {
        DataTable(
            headers = listOf("Produto", "Quantidade", "Sub-Total", "Lucro", "Estado", "Proprietáio", "Usuário", "Data e hora"),
            rows = salesReportUiState.reportSales
        ) {
            DatatableText(it.productName)
            DatatableText(it.quantity.toString())
            DatatableText(it.subTotal.toString())
            DatatableText(it.profit.toString())
            DatatableText(it.status)
            DatatableText(it.ownerName)
            DatatableText(it.userName)
            DatatableText(it.datetime ?: "")
        }
    }

//    if (salesReportUiState.showModal) {
//        DialogFormModal(
//            title = "Notificação",
//            modalSize = ModalSize.MEDIUMN,
//            hideSubmitButton = true,
//            onDismiss = { salesReportViewModel.fillNotificationForm(showModal = false) },
//            onSubmit = {
//                salesReportViewModel.editNotification(
//                    IdAndReadState(
//                        salesReportUiState.notificationId,
//                        isRead = true
//                    )
//                )
//                salesReportViewModel.fillNotificationForm(showModal = false)
//            }
//        ) {
//            Column(
//                modifier = Modifier
////                .fillMaxHeight(1f)
//                    .fillMaxWidth(),
//                verticalArrangement = Arrangement.spacedBy(40.dp)
//            ) {
//                with(salesReportUiState) {
//                    DialogModalText(text = "Notificação para: $notificationUserName")
//
//                    DialogModalText(notificationTitle)
//
//                    DialogModalText(notificationMessage)
//
//                    DialogModalText("Tipo: $notificationType")
//                    DialogModalText("Criado em: ${salesReportUiState.notificationCreatedAt}")
//
//                    AppCheckBox(
//                        checked = false,
//                        text = "Marcar como não lida",
//                        onCheck = {
//                            salesReportViewModel.editNotification(
//                                IdAndReadState(
//                                    salesReportUiState.notificationId,
//                                    isRead = it
//                                )
//                            )
//                        }
//                    )
//                }
//            }
//        }
//    }
}