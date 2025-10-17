package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.ProductItem

data class SalesUiState(
    val products: List<ProductItem> = emptyList(),
    val cardProducts: List<CardProduct> = emptyList(),
    val searchProductsValue: String = "",
    val descont: Double = 0.0,
    val receivedAmount: Double = 0.0,/** Amount which buyer gave */
    val clientId: Int = 0,
    val clientName: String = "",
    val paymentMethod: String = "",
    val buyerCharge: Double = 0.0,
    val saleTotal: Double = 0.0,
    val saleSubTotal: Double = 0.0,
    val saleReason: String = "",
    val commonUiState: CommonUiState = CommonUiState()
)

data class CardProduct(
    val product: ProductItem,
    val productsOnCard: Int,
    val productId: Int = 0,
    val quantity: Int = 0,
    val productCost: Double = 0.0,
    val productPrice: Double = 0.0,
    val availableQuantity: Int = 0,
    val subTotal: Double,
)