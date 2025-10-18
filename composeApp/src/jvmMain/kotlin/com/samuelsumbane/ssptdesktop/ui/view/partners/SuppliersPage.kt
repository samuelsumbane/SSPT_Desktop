package com.samuelsumbane.ssptdesktop.ui.view.partners

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProOwnerViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.SupplierViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.components.CardPItem
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.FormColumn
import com.samuelsumbane.ssptdesktop.ui.components.InfoCard
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete
import ssptdesktop.composeapp.generated.resources.edit

class SupplierScreen : Screen {
    @Composable
    override fun Content() {
        SupplierPage()
    }
}

@Composable
fun SupplierPage() {
    val supplierViewModel by remember { mutableStateOf(getKoin().get<SupplierViewModel>()) }
    val supplierUiState by supplierViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }

    CommonPageStructure(
        navigator = navigator,
        pageTitle = "Fornecedores",
        topBarActions = {
            NormalButton(icon = null, text = "+ Fornecedor") {
                submitButtonText = "Adicionar"
                supplierViewModel.openFormDialog(true, "Adicionar fornecedor")
            }
        }
    ) {

        FlowRow(modifier = Modifier.padding(10.dp)) {
            supplierUiState.suppliers.forEach { supplier ->
                with(supplier) {
                    InfoCard(modifier = Modifier.size(290.dp, 180.dp)) {
                        CardPItem("Nome", name)
                        CardPItem("Telefone", contact)
                        CardPItem("Endereço", address)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            IconButton(
                                onClick = {
                                    submitButtonText = "Actualizar"
                                    supplierViewModel.fillSupplierForm(id, name, contact, address)
                                    supplierViewModel.openFormDialog(true, "Actualizar fornecedor")
                                }
                            ) {
                                Icon(painterResource(Res.drawable.edit), "Edit Supplier")
                            }

                            Spacer(Modifier.width(10.dp))

                            IconButton(
                                onClick = { supplierViewModel.removeSupplier(id!!) }
                            ) {
                                Icon(painterResource(Res.drawable.delete), "Delete supplier")
                            }

                        }
                    }
                }
            }
        }

        if (supplierUiState.commonUiState.showFormDialog) {
            DialogFormModal(
                title = supplierUiState.commonUiState.formDialogTitle,
                onDismiss = { supplierViewModel.resetForm() },
                onSubmit = { supplierViewModel.onSupplierSubmit() }
            ) {
                FormColumn {
                    InputField(
                        inputValue = supplierUiState.supplierName,
                        label = "Nome",
                        errorText = supplierUiState.commonUiState.formErrors[FormInputName.Name],
                        onValueChanged = { supplierViewModel.fillSupplierForm(name = it) },
                    )

                    InputField(
                        inputValue = supplierUiState.supplierPhone,
                        label = "Telefone",
                        errorText = supplierUiState.commonUiState.formErrors[FormInputName.Phone],
                        onValueChanged = { supplierViewModel.fillSupplierForm(phone = it) },
                        keyboardType = KeyboardType.Phone
                    )

                    InputField(
                        inputValue = supplierUiState.supplierAddress,
                        label = "Endereço",
                        errorText = supplierUiState.commonUiState.formErrors[FormInputName.Address],
                        onValueChanged = { supplierViewModel.fillSupplierForm(address = it) },
                    )
                }
            }
        }

        AnimatedVisibility(supplierUiState.commonUiState.showAlertDialog) {
            AlertWidget(
                supplierUiState.commonUiState.alertTitle,
                supplierUiState.commonUiState.alertText,
                supplierUiState.commonUiState.alertType,
                onDismiss = { supplierViewModel.openAlertDialog(false) },
            ) {
                supplierUiState.commonUiState.alertOnAccept()
            }
        }
    }
}