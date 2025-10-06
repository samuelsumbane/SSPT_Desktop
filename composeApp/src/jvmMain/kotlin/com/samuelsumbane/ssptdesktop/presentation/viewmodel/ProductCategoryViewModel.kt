package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.CategoryItem
import com.samuelsumbane.ssptdesktop.domain.usecase.AddProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.EditProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.GetProductCategoriesUseCase
import com.samuelsumbane.ssptdesktop.domain.usecase.RemoveProductCategoryUseCase
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ProCategoryUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductCategoryViewModel(
    private val getProductCategoriesUseCase: GetProductCategoriesUseCase,
    private val addProductCategoryUseCase: AddProductCategoryUseCase,
    private val editProductCategoryUseCase: EditProductCategoryUseCase,
    private val removeProductCategoryUseCase: RemoveProductCategoryUseCase
) : ViewModel() {

    val _uiState = MutableStateFlow(ProCategoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            getProductCategoriesUseCase().collect { newCategoriesList ->
                _uiState.update { it.copy(proCategories = newCategoriesList) }
            }
        }
    }

    fun addProductCategory(productCategory: CategoryItem) =
        viewModelScope.launch {
            addProductCategoryUseCase(productCategory)
        }


    fun editProductCategory(productCategory: CategoryItem) = viewModelScope.launch {
        editProductCategoryUseCase(productCategory)
    }

    fun removeProductCategory(idProductCategory: Int) = viewModelScope.launch { removeProductCategoryUseCase(idProductCategory) }

    fun setCategoryNameData(name: String) {
        _uiState.update { it.copy(categoryName = name) }
    }

    fun onSubmitForm() {
        if (uiState.value.categoryName.isBlank()) {
            _uiState.update {
                it.copy(
                    commonStates = it.commonStates.copy(
                        formErrors = it.commonStates.formErrors.toMutableMap().apply {
                            put(FormInputName.CategoryName, "O nome da categoria é obrigatório")
                        }
                    )
                )
            }
            return
        }

        val category = CategoryItem(uiState.value.categoryId, uiState.value.categoryName, isDefault = false)

        val alertText = if (uiState.value.categoryId != 0) {
            editProductCategory(category)
            "Categoria actualizada com sucesso"
        } else {
            addProductCategory(category)
            "Categoria adicionada com sucesso"
        }
        resetForm()

        showAlert("Sucesso", alertText) {
            openAlertDialog(false)
        }

    }

    fun showAlert(
        title: String,
        text: String,
        alertType: AlertType = AlertType.SUCCESS,
        onAccept: () -> Unit
    ) {
        _uiState.update {
            it.copy(commonStates = it.commonStates.copy(
                alertTitle = title,
                alertText = text,
                alertType = alertType,
                showAlertDialog = true,
                alertOnAccept = onAccept
            ))
        }
    }

    fun resetForm() {
        _uiState.update {
            it.copy(categoryName = "")
        }
        openFormDialog(false)
        /** Clear all erros */
        _uiState.update {
            it.copy(commonStates = it.commonStates.copy(
                formErrors = emptyMap()
            ))
        }
    }

    fun openFormDialog(setOpen: Boolean, title: String = "") {
        _uiState.update {
            it.copy(
                commonStates = it.commonStates.copy(
                    showFormDialog = setOpen,
                    formDialogTitle = title
                )
            )
        }
    }

    fun openAlertDialog(setOpen: Boolean) {
        _uiState.update {
            it.copy(
                commonStates = it.commonStates.copy(showAlertDialog = setOpen)
            )
        }
    }

    fun fillCategoryForm(categoryId: Int, categoryName: String) {
        _uiState.update {
            it.copy(
                categoryId = categoryId,
                categoryName = categoryName
            )
        }
    }

}