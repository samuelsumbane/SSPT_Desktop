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
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.DropDown
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.koin.java.KoinJavaComponent.getKoin

class PayableScreen : Screen {
    @Composable
    override fun Content() {
        PayablePage()
    }
}

@Composable
private fun PayablePage() {
    val financeViewModel by remember { mutableStateOf(getKoin().get<FinanceViewModel>()) }
    val financeUiState by financeViewModel.uistate.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    CommonPageStructure(
        navigator,
        pageTitle = "Contas a pagar",
        topBarActions = {
            NormalButton(icon = null, text = "+ C. Pagar") {
//                submitButtonText = "Adicionar"
//                branchViewModel.openFormDialog(true, "Adicionar conta a pagar")
            }
        }
    ) {
        DataTable(
            headers = listOf("Fornecedor", "Descrição", "Valor", "Vencimento", "D. Pagamento", "M. Pagamento", "Estado"),
            rows = financeUiState.payables
        ) {
            DatatableText(it.supplier)
            DatatableText(it.description.cut())
            DatatableText(it.value.toString())
            DatatableText(it.expiration_date.cut())
            DatatableText(it.payment_date)
            DatatableText(it.payment_method)
            DatatableText(it.status)
        }

        // minModal
        if (financeUiState.commonUiState.showFormDialog) {
            for((label, value) in mapOf("Fornecedor" to financeUiState.supplier, "Descrição" to financeUiState.description)) {
                InputField(
                    label = "$label (Apenas leitura)",
                    inputValue = value,
                    enabled = false,
                    onValueChanged = {}
                )
            }

            InputField(
                inputValue = financeUiState.amount.toString(),
                label = "Valor a pagar",
                errorText = financeUiState.commonUiState.formErrors[FormInputName.Amount],
                onValueChanged = { financeViewModel.fillFormFields(amount = it.toDouble()) }
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

        // Add payable ------->>
        if (financeUiState.showSecondaryModal) {
            DialogFormModal(
                title = "Adicionar a conta a pagar",
                onDismiss = { financeViewModel.openFormDialog(false) },
                onSubmit = { financeViewModel.onAddPayableSubmit() }
            ) {
                InputField(
                    inputValue = financeUiState.supplier,
                    label = "Fornecedor",
                    errorText = financeUiState.commonUiState.formErrors[FormInputName.Supplier],
                    onValueChanged = { financeViewModel.fillFormFields(supplier = it) }
                )

                InputField(
                    inputValue = financeUiState.description,
                    label = "Descrição",
                    errorText = financeUiState.commonUiState.formErrors[FormInputName.Description],
                    onValueChanged = { financeViewModel.fillFormFields(description = it) }
                )

                InputField(
                    inputValue = financeUiState.amount.toString(),
                    label = "Valor a pegar",
                    errorText = financeUiState.commonUiState.formErrors[FormInputName.Amount],
                    onValueChanged = { financeViewModel.fillFormFields(amount = it.toDouble()) }
                )

                InputField(
                    inputValue = financeUiState.expirationDate,
                    label = "Data expiração",
                    errorText = financeUiState.commonUiState.formErrors[FormInputName.ExpirationDate],
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
}

