package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.ProductRepository
import com.samuelsumbane.ssptdesktop.domain.repository.SalesRepository
import com.samuelsumbane.ssptdesktop.kclient.OrderItemDraft
import com.samuelsumbane.ssptdesktop.kclient.ProductItem
import com.samuelsumbane.ssptdesktop.kclient.SaleItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.CardProduct
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.SalesUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.ProductQuantityAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

class SaleViewModel(
    private val salesRepo: SalesRepository,
    private val proRepo: ProductRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(SalesUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
//            val products = proRepo.getProducts()
            val products = listOf(
                ProductItem(1, "Fanta", "UNIT", 1, "", 12.0,
                    price = 20.0, 4, 1, 2, "Sapatos", "", 1, "s"),
                ProductItem(2, "Teclado", "UNIT", 1, "", 12.0,
                    price = 20.0, 6, 1, 2, "Sapatos", "", 1, "s"),
                ProductItem(3, "Telefone", "UNIT", 1, "", 12.0,
                    price = 20.0, 10, 1, 2, "Sapatos", "", 1, "s"),
            )
            _uiState.update { it.copy(products = products) }
        }
    }

    fun saleProducts(saleItem: SaleItem) {
        viewModelScope.launch {
            val (status, message) = salesRepo.saleProducts(saleItem)
        }
    }

    fun addProductToCard(productItem: ProductItem) {
        val cardProduct = CardProduct(
            product = productItem,
            productsOnCard = 1,
            /** Product is added in cardProducts with 1 quantity */
            productPrice = productItem.price,
            subTotal = productItem.price

        )

        uiState.value.cardProducts
            .firstOrNull { it.product.id == productItem.id }
            ?.let {
                openAlertDialog(true)
                showAlert(
                    title = "Producto não adicionado",
                    text = "O producto já está no carrinho",
                    alertType = AlertType.INFO,
                ) {
                    openAlertDialog(false)
                }
            }
            ?: run {
                _uiState.update {
                    it.copy(cardProducts = it.cardProducts.toMutableList().apply { add(cardProduct) })
                }
            }

        updateSaleAmount()
    }


    fun changeBuyingProductQuantity(action: ProductQuantityAction, productId: Int) {
        _uiState.update {
            val product = uiState.value.cardProducts.first { pr -> pr.product.id == productId }

            if (product.productsOnCard == 1 && action == ProductQuantityAction.Decrease) {
                it.copy(
                    cardProducts = it.cardProducts.toMutableList().apply { remove(product) },
                )

            } else {
                it.copy(
                    cardProducts = uiState.value.cardProducts
                        .map { cardPro ->
                            if (cardPro.product.id == productId) {
                                val newProductsOnCardQuantity = if (action == ProductQuantityAction.Increase) cardPro.productsOnCard + 1 else cardPro.productsOnCard - 1
                                cardPro.copy(
                                    productsOnCard = newProductsOnCardQuantity,
                                    subTotal = cardPro.productPrice * newProductsOnCardQuantity
                                )
                            } else cardPro
                        }
                )
            }
        }
        updateSaleAmount()
    }

    fun updateSaleAmount() {
        _uiState.update {
            val calculedSaleSubTotal =  uiState.value.cardProducts.sumOf { cardProducts -> cardProducts.subTotal }

            val newSaleTotal = calculedSaleSubTotal - uiState.value.descont
            println("newsaletotal was $newSaleTotal")
            if (newSaleTotal < 0) { // -3
                it.copy(
                    saleSubTotal = calculedSaleSubTotal,
                    buyerCharge = uiState.value.buyerCharge + abs(newSaleTotal),
                    saleTotal = 0.0,
                )
            } else {
                it.copy(
                    saleSubTotal = calculedSaleSubTotal,
                    saleTotal = newSaleTotal,
                )
            }

        }
    }

    fun filterProducts(value: String) { _uiState.update { it.copy(searchProductsValue = value) } }

    fun fillFormFields(
        discont: Double? = null,
        receivedValueFromBuyer: Double? = null,
        clientId: Int? = null,
        clientName: String? = null,
        paymentMethod: String? = null
    ) {
        discont?.let { newValue ->
            _uiState.update { it.copy(descont = newValue) }
            updateSaleAmount()
        }
        receivedValueFromBuyer?.let { newValue ->
            _uiState.update { it.copy(receivedAmount = newValue) }
            if (newValue > uiState.value.saleTotal) {
                _uiState.update { it.copy(buyerCharge = newValue - uiState.value.saleTotal) }
            } else {
                _uiState.update { it.copy(buyerCharge = uiState.value.saleTotal - uiState.value.saleSubTotal) }

            }

        }
        clientId?.let { newValue -> _uiState.update { it.copy(clientId = newValue) } }
        clientName?.let { newValue -> _uiState.update { it.copy(clientName = newValue) } }
        paymentMethod?.let { newValue -> _uiState.update { it.copy(paymentMethod = newValue) } }
    }

    fun onSubmit() {
        if (uiState.value.cardProducts.isEmpty()) {
            // Nenhum producto encontrado no carinho
            return
        }

//        val orderDraft = OrderItemDraft(
//            clientId = null,
//            total = uiState.value.saleTotal,
//            status = uiState.value.saleReason,
//            userId = 0,
//            branchId = 1
//        )
//
//        val (status, message) = salesRepo.saleProducts(
//            SaleItem(
//
//            )
//        )
    }


    fun showAlert(
        title: String,
        text: String,
        alertType: AlertType = AlertType.SUCCESS,
        onAccept: () -> Unit
    ) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                alertTitle = title,
                alertText = text,
                alertType = alertType,
                showAlertDialog = true,
                alertOnAccept = onAccept
            ))
        }
    }


    fun openAlertDialog(setOpen: Boolean) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(showAlertDialog = setOpen))
        }
    }

}


