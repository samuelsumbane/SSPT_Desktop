package com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates

import com.samuelsumbane.ssptdesktop.kclient.CategoryItem

data class ProCategoryUiState(
    val proCategories: List<CategoryItem> = emptyList(),
    val categoryId: Int = 0,
    val categoryName: String = "",
    val commonStates: CommonUiState = CommonUiState()
)
