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
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveProductUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.ChangeProductPriceUseCase
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ProductsUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val addProductUseCase: AddProductsUseCase,
    private val editProductUseCase: EditProductUseCase,
    private val updateProductPriceUseCase: ChangeProductPriceUseCase,
    private val removeProductUseCase: RemoveProductUseCase,
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

    fun loadCategories() {
        viewModelScope.launch {
            getProductCategoriesUseCase().collect { categoriesList ->
                _uiState.update { it.copy(categories = categoriesList) }
            }
        }
    }

    fun loadProOwners() {
        viewModelScope.launch {
            getProOwnerUseCase().collect { proOwners ->
                _uiState.update { it.copy(owners = proOwners) }
            }
        }
    }

    fun updateProductPrice(product: ChangeProductPriceDraft) {
        viewModelScope.launch { updateProductPriceUseCase(product) }
    }

    fun startAddingProductProcess() {
        openFormDialog(true, "Adicionar producto")
        fillFormFields(proType = "UNIT")
        loadProducts()
        loadCategories()
        loadProOwners()
    }

    fun onSubmitForm() {
        if (uiState.value.proName.isBlank()) {
            setFormError(FormInputName.ProName, "O nome é obrigatorio")
            return
        }
        setFormError(FormInputName.ProName, "")

        if (uiState.value.proType !in listOf("UNIT", "PACK")) {
            setFormError(FormInputName.ProType, "O tipo é obrigatorio")
            return
        }
        setFormError(FormInputName.ProType, "")

        if (uiState.value.proStock == 0) {
            setFormError(FormInputName.ProStock, "A quantidade é obrigatoria")
            return
        }
        setFormError(FormInputName.ProStock, "")

        if (uiState.value.proCost == 0.0) {
            setFormError(FormInputName.ProCost, "O custo é obrigatorio")
            return
        }
        setFormError(FormInputName.ProCost, "")


        if (uiState.value.proPrice == 0.0) {
            setFormError(FormInputName.ProPrice, "O preço de venda é obrigatorio")
            return
        }
        setFormError(FormInputName.ProPrice, "")


        if (uiState.value.proCategoryId == 0) {
            setFormError(FormInputName.CategoryName, "A categoria é obrigatoria")
            return
        }
        setFormError(FormInputName.CategoryName, "")

//        if (uiState.value.proBarcode.isBlank()) {
//            setFormError(FormInputName.ProBarcode, "A categoria é obrigatoria")
//            return
//        }
//        setFormError(FormInputName.CategoryName, "")

        if (uiState.value.proOwnerId == 0) {
            setFormError(FormInputName.OwnerName, "Por favor, selecione um proprietaio")
            return
        }
        setFormError(FormInputName.OwnerName, "")

        val product = ProductItem(
            id = uiState.value.proId,
            name = uiState.value.proName,
            type = uiState.value.proType,
            productRelationId = uiState.value.proRelationId,
            productRelationName = uiState.value.proRelationName,
            cost = uiState.value.proCost,
            price = uiState.value.proPrice,
            stock = uiState.value.proStock,
            minStock = uiState.value.proMinStock,
            categoryId = uiState.value.proCategoryId,
            categoryName = uiState.value.proCategoryName,
            barcode = uiState.value.proBarcode,
            ownerId = uiState.value.proOwnerId,
            ownerName = uiState.value.proOwnerName
        )

        val alertText = if (uiState.value.proId != 0) {
            viewModelScope.launch { editProductUseCase(product) }
            "Producto actualizado com sucesso"
        } else {
            viewModelScope.launch { addProductUseCase(product) }
            "Producto adicionado com sucesso"
        }
        openFormDialog(false)

        showAlert("Sucesso", alertText) { openAlertDialog(false) }
    }

    fun fillFormFields(
        proName: String? = null,
        proType: String? = null,
        proStock: Int? = null,
        proCost: Double? = null,
        proPrice: Double? = null,
        proBarcode: String? = null,
        proCategoryId: Int? = null,
        proCategoryName: String? = null,
        proOwnerId: Int? = null,
        proOwnerName: String? = null,
        dropdownCategoriesExpanded: Boolean? = null,
        dropdownOwnersExpanded: Boolean? = null,
        dropdownProductsExpanded: Boolean? = null,
        dropdownProductTypeExpanded: Boolean? = null,
    ) {
        proName?.let { newValue -> _uiState.update { it.copy(proName = newValue) } }
        proType?.let { newValue -> _uiState.update { it.copy(proType = newValue) } }
        proStock?.let { newValue -> _uiState.update { it.copy(proStock = newValue) } }
        proCost?.let { newValue -> _uiState.update { it.copy(proCost = newValue) } }
        proPrice?.let { newValue -> _uiState.update { it.copy(proPrice = newValue) } }
        proBarcode?.let { newValue -> _uiState.update { it.copy(proBarcode = newValue) } }
        proCategoryId?.let { newValue -> _uiState.update { it.copy(proCategoryId = newValue) } }
        proCategoryName?.let { newValue -> _uiState.update { it.copy(proCategoryName = newValue) } }
        proOwnerId?.let { newValue -> _uiState.update { it.copy(proOwnerId = newValue) } }
        proOwnerName?.let { newValue -> _uiState.update { it.copy(proOwnerName = newValue) } }
        dropdownCategoriesExpanded?.let { newValue -> _uiState.update { it.copy(dropdownCategoriesExpanded = newValue) } }
        dropdownOwnersExpanded?.let { newValue -> _uiState.update { it.copy(dropdownOwnersExpanded = newValue) } }
        dropdownProductsExpanded?.let { newValue -> _uiState.update { it.copy(dropdownProductsExpanded = newValue) } }
        dropdownProductTypeExpanded?.let { newValue -> _uiState.update { it.copy(dropdownProductTypeExpanded = newValue) } }

    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                proName = "",
                proType = "",
                proStock = 0,
                proCost = 0.0,
                proPrice = 0.0,
                proBarcode = "",
                proOwnerId = 0,
                proOwnerName = "",
                proCategoryId = 0,
                proCategoryName = "",
            )
        }
        openFormDialog(false)
    }

    fun changeProProductExpandedState(setOpen: Boolean) = _uiState.update { it.copy(dropdownProductsExpanded = setOpen) }
    fun changePackTypeSelected(setOpen: Boolean) = _uiState.update { it.copy(isPackTypeSelected = setOpen) }

    fun openFormDialog(setOpen: Boolean, title: String = "") {
        _uiState.update {
            it.copy(
                commonUiState = it.commonUiState.copy(
                    showFormDialog = setOpen,
                    formDialogTitle = title
                )
            )
        }
    }

    fun openAlertDialog(setOpen: Boolean) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(showAlertDialog = setOpen))
        }
    }

    fun removeProduct(productId: Int) {
        viewModelScope.launch { removeProductUseCase(productId) }
    }

    fun setFormError(field: FormInputName, error: String) {
        _uiState.update {
            it.copy(commonUiState = it.commonUiState.copy(
                formErrors = it.commonUiState.formErrors.toMutableMap().apply { put(field, error) }
            ))
        }
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
}


//suspend fun getProductById(productId: Int): ProductItem?
