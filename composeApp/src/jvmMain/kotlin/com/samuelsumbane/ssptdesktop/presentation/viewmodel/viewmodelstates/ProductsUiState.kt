package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.ProductItem

data class ProductsUiState(
    val products: List<ProductItem> = emptyList(),
    val proId: Int = 0,
    val proName: String = "",
    val proType: String = "",
    val proRelationId: Int? = null,
    val proRelationName: String = "",
    val proCost: Double = 0.0,
    val proPrice: Double = 0.0,
    val proStock: Int = 0,
    val proMinStock: Int? = null,
    val proCategoryId: Int = 0,
    val proCategoryName: String? = null,
    val proBarcode: String = "",
    val proOwnerId: Int = 0,
    val proOwnerName: String = "",
    val proTypeExpanded: Boolean = false,
    val proProductsExpanded: Boolean = false,
    val isPackTypeSelected: Boolean = false,
    val commonUiState: CommonUiState = CommonUiState(),
)
