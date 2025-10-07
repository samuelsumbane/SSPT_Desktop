package com.samuelsumbane.ssptdesktop.ui.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductsViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.DropDown
import com.samuelsumbane.ssptdesktop.ui.components.InfoCard
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.MenuItemText
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.koin.java.KoinJavaComponent.getKoin

@Composable
fun ProductsPage() {
    val productsViewModel by remember { mutableStateOf(getKoin().get<ProductsViewModel>()) }
    val productsUiState by productsViewModel.uiState.collectAsState()
    var productStockTypeMenuExpanded by remember { mutableStateOf(false) }

    CommonPageStructure(
        pageTitle = "Productos",
        topBarActions = {
            NormalButton(icon = null, text = "+ Categoria") {
//                submitButtonText = "Adicionar"
//                productsViewModel.openFormDialog(true, "Adicionar producto")
            }
        }
    ) {

        FlowRow(modifier = Modifier.padding(10.dp)) {
            productsUiState.products.forEach {
                with(it) {
                    InfoCard(modifier = Modifier.size(260.dp, 150.dp)) {
                        Text("producto: $name ")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            NormalButton(text = "Editar") {
//                                submitButtonText = "Actualizar"
//                                productsViewModel.fillFormFields(id, name, telephone)
//                                productsViewModel.openFormDialog(true, "Actualizar producto")
                            }
                            Spacer(Modifier.width(10.dp))
                            NormalButton(text = "Deletar") {
//                                productsViewModel.removeProOwner(id)
                            }
                        }
                    }
                }
            }
        }

        if (productsUiState.commonUiState.showFormDialog) {
            DialogFormModal(
                title = productsUiState.commonUiState.formDialogTitle,
                onDismiss = { productsViewModel.resetForm() },
                onSubmit = { productsViewModel.onSubmitForm() }
            ) {
                InputField(
                    inputValue = productsUiState.proName,
                    label = "Nome",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerName],
                    onValueChanged = { productsViewModel.fillFormFields(name = it) },
                )

                DropDown(
                    text = "sdas",
                    expanded = productsUiState.proTypeExpanded,
                    onDismiss = { productsViewModel.changeProTypeExpandedState(false) },
                    onDropdownClicked = { productsViewModel.changeProTypeExpandedState(!productStockTypeMenuExpanded) }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            productsViewModel.changePackTypeSelected(false)
                        }
                    ) {
                        MenuItemText("Unitário")
                    }

                    DropdownMenuItem(onClick = { productsViewModel.changePackTypeSelected(true) }) {
                        MenuItemText("Embalagem")
                    }
                }

//                AnimatedVisibility(productsUiState.proProductsExpanded) {
                    DropDown(
                        text = "Producto relacionado",
                        expanded = productsUiState.proProductsExpanded,
                        onDismiss = { productsViewModel.changeProProductExpandedState(false) },
                        onDropdownClicked = { productsViewModel.changeProProductExpandedState(!productsUiState.proProductsExpanded) }
                    ) {
                        productsUiState.products.forEach { product ->
                            DropdownMenuItem(onClick = {}) {
                                MenuItemText(product.name)
                            }
                        }
                    }
//                }

                InputField(
                    inputValue = productsUiState.proStock.toString(),
                    label = "Quantidade",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerPhone],
                    onValueChanged = {
//                        productsViewModel.fillFormFields(telephone = it)
                                     },
                )


//                InputField(
//                    inputValue = productsUiState.proMinStock.toString(),
//                    label = "Quantidade Minima",
//                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerPhone],
//                    onValueChanged = {
////                        productsViewModel.fillFormFields(telephone = it)
//                    },
//                )

                InputField(
                    inputValue = productsUiState.proCost.toString(),
                    label = "Custo",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerPhone],
                    onValueChanged = {
//                        productsViewModel.fillFormFields(telephone = it)
                    },
                )

                InputField(
                    inputValue = productsUiState.proPrice.toString(),
                    label = "Preço",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerPhone],
                    onValueChanged = {
//                        productsViewModel.fillFormFields(telephone = it)
                    },
                )

                DropDown(
                    text = "Categoria",
                    expanded = productsUiState.proProductsExpanded,
                    onDismiss = { productsViewModel.changeProProductExpandedState(false) },
                    onDropdownClicked = { productsViewModel.changeProProductExpandedState(!productsUiState.proProductsExpanded) }
                ) {
                    productsUiState.products.forEach { product ->
                        DropdownMenuItem(onClick = {}) {
                            MenuItemText(product.name)
                        }
                    }
                }

                InputField(
                    inputValue = productsUiState.proBarcode,
                    label = "Código de barras",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerPhone],
                    onValueChanged = {
//                        productsViewModel.fillFormFields(telephone = it)
                    },
                )

                DropDown(
                    text = "Proprietário",
                    expanded = productsUiState.proProductsExpanded,
                    onDismiss = { productsViewModel.changeProProductExpandedState(false) },
                    onDropdownClicked = { productsViewModel.changeProProductExpandedState(!productsUiState.proProductsExpanded) }
                ) {
                    productsUiState.products.forEach { product ->
                        DropdownMenuItem(onClick = {}) {
                            MenuItemText(product.name)
                        }
                    }
                }

            }
        }

        AnimatedVisibility(productsUiState.commonUiState.showAlertDialog) {
            AlertWidget(
                productsUiState.commonUiState.alertTitle,
                productsUiState.commonUiState.alertText,
                productsUiState.commonUiState.alertType,
                onDismiss = { productsViewModel.openAlertDialog(false) },
            ) {
                productsUiState.commonUiState.alertOnAccept()
            }
        }

    }
}