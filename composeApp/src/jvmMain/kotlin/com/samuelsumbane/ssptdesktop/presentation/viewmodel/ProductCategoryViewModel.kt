package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.samuelsumbane.ssptdesktop.domain.repository.ProductCategoryRepository
import com.samuelsumbane.ssptdesktop.kclient.CategoryItem
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.ProCategoryUiState
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductCategoryViewModel(
    private val repo: ProductCategoryRepository
) : ViewModel() {

    val _uiState = MutableStateFlow(ProCategoryUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadCategories()
    }

    fun loadCategories() {
        viewModelScope.launch {
            val newCategoriesList = repo.getProductCategories()
            _uiState.update { it.copy(proCategories = newCategoriesList) }
            println("data are: $newCategoriesList")
        }
    }

    fun addProductCategory(productCategory: CategoryItem) =
        viewModelScope.launch {
            val (status, message) = repo.addProductCategory(productCategory)
        }

    fun removeProductCategory(productCategoryId: Int) {
        viewModelScope.launch { repo.removeProductCategory(productCategoryId) }
    }


    fun setCategoryNameData(name: String) {
        _uiState.update { it.copy(categoryName = name) }
    }

    fun onSubmitForm() {
        viewModelScope.launch {
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
                return@launch
            }

            val category = CategoryItem(uiState.value.categoryId, uiState.value.categoryName, isDefault = false)

            val (status, message) = if (uiState.value.categoryId != 0) repo.editProductCategory(category) else repo.addProductCategory(category)

            val alertTitle = when (status) {
                200 -> "Categoria actualizada"
                201 -> "Categoria adicionada"
                else -> ""
            }

            resetForm()

            showAlert(alertTitle, message) {
                openAlertDialog(false)
            }
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
        loadCategories()
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