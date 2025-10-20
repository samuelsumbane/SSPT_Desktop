package com.samuelsumbane.ssptdesktop.ui.view.finance

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.core.utils.cut
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.FinanceViewModel
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DataTable
import com.samuelsumbane.ssptdesktop.ui.components.DatatableText
import com.samuelsumbane.ssptdesktop.ui.components.DropDown
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.koin.java.KoinJavaComponent.getKoin

class ReceivableScreen : Screen {
    @Composable
    override fun Content() {
        ReceivablePage()
    }
}

@Composable
private fun ReceivablePage() {
    val financeViewModel by remember { mutableStateOf(getKoin().get<FinanceViewModel>()) }
    val financeUiState by financeViewModel.uistate.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    CommonPageStructure(
        navigator,
        pageTitle = "Cotas a receber",
        topBarActions = {
            NormalButton(icon = null, text = "+ C. Receber") {
//                submitButtonText = "Adicionar"
//                branchViewModel.openFormDialog(true, "Adicionar conta a pagar")
            }
        }
    ) {
        DataTable(
            headers = listOf("Cliente", "Descrição", "Valor", "Vencimento", "M. Pagamento"),
            rows = financeUiState.receivables
        ) {
            DatatableText(it.client)
            DatatableText(it.description.cut())
            DatatableText(it.value.toString())
            DatatableText(it.expiration_data.cut())
            DatatableText(it.received_method)
        }

        if (financeUiState.commonUiState.showFormDialog) {
            for ((label, value) in mapOf(
                "Cliente" to financeUiState.client,
                "Descrição" to financeUiState.description
            )) {
                InputField(
                    inputValue = value,
                    label = "$label (Apenas leitura)",
                    enabled = false,
                    onValueChanged = {}
                )
            }

            DropDown(
                label = "Metôdo de pagamento",
                text = financeUiState.paymentMethod,
                onDismiss = { financeViewModel.fillFormFields(paymentDropdownExpanded = false) },
                onDropdownClicked = {
                    financeViewModel.fillFormFields(paymentDropdownExpanded = !financeUiState.paymentDropdownExpanded)
                }
            ) {
                listOf("Dinheiro").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            financeViewModel.fillFormFields(paymentMethod = it)
                        }
                    )
                }
            }
        }

        if (financeUiState.showSecondaryModal) {
            InputField(
                inputValue = financeUiState.client,
                label = "Cliente",
                errorText = financeUiState.commonUiState.formErrors[FormInputName.ClientName],
                onValueChanged = { financeViewModel.fillFormFields(client = it) }
            )

            InputField(
                inputValue = financeUiState.description,
                label = "Descrição",
                errorText = financeUiState.commonUiState.formErrors[FormInputName.Description],
                onValueChanged = { financeViewModel.fillFormFields(description = it) }
            )

            InputField(
                inputValue = financeUiState.amount.toString(),
                label = "Valor a pagar",
                errorText = financeUiState.commonUiState.formErrors[FormInputName.Amount],
                onValueChanged = { financeViewModel.fillFormFields(amount = it.toDouble()) }
            )

            InputField(
                inputValue = financeUiState.expirationDate,
                label = "Data de expiração",
                onValueChanged = { financeViewModel.fillFormFields(expirationDate = it) }
            )

            DropDown(
                label = "Metôdo de pagamento",
                text = financeUiState.paymentMethod,
                onDismiss = { financeViewModel.fillFormFields(paymentDropdownExpanded = false) },
                onDropdownClicked = {
                    financeViewModel.fillFormFields(paymentDropdownExpanded = !financeUiState.paymentDropdownExpanded)
                }
            ) {
                listOf("Dinheiro").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            financeViewModel.fillFormFields(paymentMethod = it)
                        }
                    )
                }
            }

        }
    }
}

