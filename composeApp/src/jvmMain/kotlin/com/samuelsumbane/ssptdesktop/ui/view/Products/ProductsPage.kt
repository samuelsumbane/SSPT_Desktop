package com.samuelsumbane.ssptdesktop.ui.view.Products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductsViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete
import ssptdesktop.composeapp.generated.resources.details
import ssptdesktop.composeapp.generated.resources.edit


class ProductsScreen : Screen {
    @Composable
    override fun Content() {
        ProductsPage()
    }
}


@Composable
fun ProductsPage() {
    val productsViewModel by remember { mutableStateOf(getKoin().get<ProductsViewModel>()) }
    val productsUiState by productsViewModel.uiState.collectAsState()
    var productStockTypeMenuExpanded by remember { mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow

    CommonPageStructure(
        navigator = navigator,
        pageTitle = "Produtos",
        activePage = PageName.PRODUCTS.itsName,
        topBarActions = {
            NormalButton(icon = null, text = "+ Producto") {
//                submitButtonText = "Adicionar"
                productsViewModel.startAddingProductProcess()
            }
        }
    ) {

         CustomFlowRow {
            productsUiState.products.forEach {
                with(it) {
                    InfoCard(modifier = Modifier.size(300.dp, 350.dp)) {
                        Text(text = name.uppercase(), fontWeight = FontWeight.SemiBold)
                        CardPItem("Custo", "$cost MT")
                        CardPItem("Estoque", "$stock")
                        CardPItem("Categoria", categoryName)
                        CardPItem("C. Barras", barcode)
                        CardPItem("P. Relacionado", productRelationName)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = { productsViewModel.removeProduct(id) }) {
                                Icon(painterResource(Res.drawable.details), contentDescription = "View product details")
                            }

                            Spacer(Modifier.width(10.dp))

                            IconButton(
                                onClick = {
                                    productsViewModel.fillFormFields(
                                        proName = name,
                                        proStock = stock,
                                        proCost = cost,
                                        proPrice = price.toString(),
                                        proBarcode = barcode
                                    )
                                    productsViewModel.openFormDialog(true, "Actualizar producto")
                                }
                            ) {
                                Icon(painterResource(Res.drawable.edit), contentDescription = "Edit product")
                            }

                            Spacer(Modifier.width(10.dp))

                            IconButton(onClick = { productsViewModel.removeProduct(id) }) {
                                Icon(painterResource(Res.drawable.delete), contentDescription = "Delete product")
                            }
                        }
                    }
                }
            }
        }

        if (productsUiState.commonUiState.showFormDialog) {
            DialogFormModal(
                title = productsUiState.commonUiState.formDialogTitle,
                modalSize = ModalSize.MEDIUMN,
                onDismiss = { productsViewModel.apply { resetForm(); openFormDialog(false) } },
                onSubmit = { productsViewModel.onSubmitForm() }
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FormColumn(Modifier.fillMaxWidth(0.5f)){
                        InputField(
                            inputValue = productsUiState.proName,
                            label = "Nome",
                            errorText = productsUiState.commonUiState.formErrors[FormInputName.ProName],
                            onValueChanged = { productsViewModel.fillFormFields(proName = it) },
                        )

                        DropDown(
                            label = "Tipo",
                            text = if (productsUiState.proType == "UNIT") "Unitário" else if (productsUiState.proType == "PACK") "Embalagem" else "",
                            errorText = productsUiState.commonUiState.formErrors[FormInputName.ProType],
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
                                    DropdownMenuItem(
                                        onClick = {
                                            productsViewModel.fillFormFields(dropdownProductsExpanded = false)
                                        }
                                    ) {
                                        MenuItemText(product.name)
                                    }
                                }
                            }
                        }

                        InputField(
                            inputValue = productsUiState.proStock.toInputValue(),
                            label = "Quantidade",
                            errorText = productsUiState.commonUiState.formErrors[FormInputName.ProStock],
                            onValueChanged = { productsViewModel.fillFormFields(proStock = if(it == "") 0 else it.toInt()) },
                            keyboardType = KeyboardType.Number
                        )

//                        InputField(
//                            inputValue = productsUiState.proMinStock.toString(),
//                            label = "Minima quantidade",
////                            errorText = productsUiState.commonUiState.formErrors[FormInputName.],
//                            onValueChanged = { productsViewModel.fillFormFields(proMinStock = it.toInt()) },
//                            keyboardType = KeyboardType.Number
//                        )
                    }

                    // Left side
                    FormColumn(Modifier.weight(1f)) {
                        InputField(
                            inputValue = productsUiState.proCost.toInputValue(),
                            label = "Custo",
                            errorText = productsUiState.commonUiState.formErrors[FormInputName.ProCost],
                            onValueChanged = { productsViewModel.fillFormFields(proCost = if (it.isBlank()) 0.0 else it.toDouble()) },
                            keyboardType = KeyboardType.Decimal
                        )

                        InputField(
                            inputValue = productsUiState.proPrice.toInputValue(),
                            label = "Preço",
                            errorText = productsUiState.commonUiState.formErrors[FormInputName.ProPrice],
                            onValueChanged = { productsViewModel.fillFormFields(proPrice = it) },
                            keyboardType = KeyboardType.Decimal
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
                            errorText = productsUiState.commonUiState.formErrors[FormInputName.ProBarcode],
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