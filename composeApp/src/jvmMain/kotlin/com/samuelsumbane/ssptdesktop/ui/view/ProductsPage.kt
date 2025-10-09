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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductsViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.DropDown
import com.samuelsumbane.ssptdesktop.ui.components.InfoCard
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.LoadingWidget
import com.samuelsumbane.ssptdesktop.ui.components.MenuItemText
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.components.ProgressIndicatorSize
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
            NormalButton(icon = null, text = "+ Producto") {
//                submitButtonText = "Adicionar"
                productsViewModel.openFormDialog(true, "Adicionar producto")
                productsViewModel.loadProducts()
                productsViewModel.loadCategories()
                productsViewModel.loadProOwners()
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
                                productsViewModel.fillFormFields(
                                    proName = name,
                                    proStock = stock,
                                    proCost = cost,
                                    proPrice = price,
                                    proBarcode = barcode
                                )
                                productsViewModel.openFormDialog(true, "Actualizar producto")
                            }
                            Spacer(Modifier.width(10.dp))
                            NormalButton(text = "Deletar") {
                                productsViewModel.removeProduct(id)
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
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.ProName],
                    onValueChanged = { productsViewModel.fillFormFields(proName = it) },
                )
                
                DropDown(
                    label = "Tipo",
                    text = if (productsUiState.proType == "UNIT") "Unitário" else "Embalagem",
                    expanded = productsUiState.dropdownProductTypeExpanded,
                    onDismiss = { productsViewModel.fillFormFields(dropdownProductTypeExpanded = false) },
                    onDropdownClicked = { productsViewModel.fillFormFields(dropdownProductTypeExpanded = !productStockTypeMenuExpanded) }
                ) {
                    for((key, value) in mapOf("UNIT" to "Unitário", "PACK" to "Embalagem")) {
//                        if (!productsUiState.products.isEmpty() && key == "PACK") {
                            DropdownMenuItem(
                                onClick = {
                                    productsViewModel.changePackTypeSelected(false)
                                    productsViewModel.fillFormFields(
                                        proType = key,
                                        dropdownProductTypeExpanded = false
                                    )
                                }
                            ) { MenuItemText(value) }
//                        }
                    }

                }

//                AnimatedVisibility(productsUiState.proProductsExpanded) {
                if (productsUiState.proType == "PACK") {
                    DropDown(
                        label = "Producto relacionado",
                        text = productsUiState.proRelationName,
                        expanded = productsUiState.dropdownProductsExpanded,
                        onDismiss = { productsViewModel.fillFormFields(dropdownProductsExpanded = false) },
                        onDropdownClicked = { productsViewModel.fillFormFields(dropdownProductsExpanded = !productsUiState.dropdownProductsExpanded) }
                    ) {
                        productsUiState.products.forEach { product ->
                            DropdownMenuItem(onClick = {
//                                productsViewModel.fillFormFields()
                                productsViewModel.fillFormFields(dropdownProductsExpanded = false)
                            }) {
                                MenuItemText(product.name)
                            }
                        }
                    }
                }


                InputField(
                    inputValue = productsUiState.proStock.toString(),
                    label = "Quantidade",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.ProStock],
                    onValueChanged = { productsViewModel.fillFormFields(proStock = it.toInt()) },
                    keyboardType = KeyboardType.Number
                )


//                InputField(
//                    inputValue = productsUiState.proMinStock.toString(),
//                    label = "Quantidade Minima",
//                    errorText = productsUiState.commonUiState.formErrors[FormInputName.ProMinStock],
//                    onValueChanged = {
////                        productsViewModel.fillFormFields(telephone = it)
//                    },
//                )

                InputField(
                    inputValue = productsUiState.proCost.toString(),
                    label = "Custo",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.ProCost],
                    onValueChanged = { productsViewModel.fillFormFields(proCost = it.toDouble()) },
                    keyboardType = KeyboardType.Number
                )

                InputField(
                    inputValue = productsUiState.proPrice.toString(),
                    label = "Preço",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.ProPrice],
                    onValueChanged = { productsViewModel.fillFormFields(proPrice = it.toDouble()) },
                    keyboardType = KeyboardType.Phone
                )

                if (productsUiState.categories.isEmpty()) {
                    LoadingWidget(ProgressIndicatorSize.Small, "Carregando categoria")
                } else {
                    DropDown(
                        label = "Categoria",
                        text = productsUiState.proCategoryName,
                        errorText = productsUiState.commonUiState.formErrors[FormInputName.CategoryName],
                        expanded = productsUiState.dropdownCategoriesExpanded,
                        onDismiss = { productsViewModel.fillFormFields(dropdownCategoriesExpanded = false) },
                        onDropdownClicked = { productsViewModel.fillFormFields(dropdownCategoriesExpanded = !productsUiState.dropdownCategoriesExpanded) }
                    ) {
                        productsUiState.categories.forEach { category ->
                            DropdownMenuItem(
                                onClick = {
                                    productsViewModel.fillFormFields(
                                        proCategoryId = category.id,
                                        proCategoryName = category.name,
                                        dropdownCategoriesExpanded = false
                                    )
                                }
                            ) {
                                MenuItemText(category.name)
                            }
                        }
                    }
                }

                InputField(
                    inputValue = productsUiState.proBarcode,
                    label = "Código de barras",
                    errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerPhone],
                    onValueChanged = { productsViewModel.fillFormFields(proBarcode = it) },
                )

                if (productsUiState.owners.isEmpty()) {
                    LoadingWidget(ProgressIndicatorSize.Small, "Carregando proprietários")
                } else {
                    DropDown(
                        label = "Proprietário",
                        text = productsUiState.proOwnerName,
                        errorText = productsUiState.commonUiState.formErrors[FormInputName.OwnerName],
                        expanded = productsUiState.dropdownOwnersExpanded,
                        onDismiss = { productsViewModel.fillFormFields(dropdownOwnersExpanded = false) },
                        onDropdownClicked = { productsViewModel.fillFormFields(dropdownOwnersExpanded = !productsUiState.dropdownOwnersExpanded) }
                    ) {
                        productsUiState.owners.forEach { owner ->
                            DropdownMenuItem(onClick = {
                                productsViewModel.fillFormFields(
                                    proOwnerId = owner.id,
                                    proOwnerName = owner.name,
                                    dropdownOwnersExpanded = false
                                )
                            }) {
                                MenuItemText(owner.name)
                            }
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