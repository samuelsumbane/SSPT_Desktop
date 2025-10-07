package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.ChangeProductPriceDraft
import com.samuelsumbane.ssptdesktop.ProductItem
import com.samuelsumbane.ssptdesktop.domain.usecase.AddProductsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditProductUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProOwnerUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProductCategoriesUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProductsUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.UpdateProductPriceUseCase
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ProductsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductsUseCase,
    private val editProductUseCase: EditProductUseCase,
    private val updateProductPriceUseCase: UpdateProductPriceUseCase,
    private val getProductCategoriesUseCase: GetProductCategoriesUseCase,
    private val getProOwnerUseCase : GetProOwnerUseCase
) : ViewModel() {

    val _uiState = MutableStateFlow(ProductsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            getProductsUseCase().collect { products ->
                _uiState.update { it.copy(products = products) }
            }
        }
    }

    fun addProduct(product: ProductItem) {
        viewModelScope.launch { addProductUseCase(product) }
    }

    fun editProduct(product: ProductItem) {
        viewModelScope.launch { editProductUseCase(product) }
    }

    fun updateProductPrice(product: ChangeProductPriceDraft) {
        viewModelScope.launch { updateProductPriceUseCase(product) }
    }

    fun onSubmitForm() {

    }

    fun fillFormFields() {

    }

    fun changeProProductExpandedState(setOpen: Boolean) = _uiState.update { it.copy(proProductsExpanded = setOpen) }
    fun changeProTypeExpandedState(setOpen: Boolean) = _uiState.update { it.copy(proTypeExpanded = setOpen) }
    fun changePackTypeSelected(setOpen: Boolean) = _uiState.update { it.copy(isPackTypeSelected = setOpen) }

}


//suspend fun getProductById(productId: Int): ProductItem?
