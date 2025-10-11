package com.samuelsumbane.ssptdesktop.ui.view.Products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
//import com.samuelsumbane.ssptdesktop.presentation.viewmodel.categoryViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProductCategoryViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.InfoCard
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.koin.java.KoinJavaComponent.getKoin

class CategoriesScreen : Screen {
    @Composable
    override fun Content() {
        CategoriesPage()
    }
}

@Composable
fun CategoriesPage() {
    val categoryViewModel by remember { mutableStateOf(getKoin().get<ProductCategoryViewModel>()) }
    val categoryUIStates by categoryViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }

    CommonPageStructure(
        navigator = navigator,
        pageTitle = "Categorias",
        topBarActions = {
            NormalButton(icon = null, text = "+ Categoria") {
                submitButtonText = "Adicionar"
                categoryViewModel.openFormDialog(true, "Adicionar categoria")
            }
        }
    ) {

        FlowRow(modifier = Modifier.padding(10.dp)) {
            categoryUIStates.proCategories.forEach { client ->
                with(client) {
                    InfoCard(modifier = Modifier.size(260.dp, 150.dp)) {
                        Text("Categoria: $name ")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            NormalButton(text = "Editar", enabled = !isDefault) {
                                submitButtonText = "Actualizar"
                                categoryViewModel.fillCategoryForm(id, name)
                                categoryViewModel.openFormDialog(true, "Actualizar categoria")
                            }
                            Spacer(Modifier.width(10.dp))
                            NormalButton(text = "Deletar", enabled = !isDefault) {
                                categoryViewModel.removeProductCategory(id)
                            }
                        }
                    }
                }
            }
        }


        if (categoryUIStates.commonStates.showFormDialog) {
            DialogFormModal(
                title = categoryUIStates.commonStates.formDialogTitle,
                onDismiss = { categoryViewModel.resetForm() },
                onSubmit = { categoryViewModel.onSubmitForm() }
            ) {
                InputField(
                    inputValue = categoryUIStates.categoryName,
                    label = "Categoria",
                    errorText = categoryUIStates.commonStates.formErrors[FormInputName.CategoryName],
                    onValueChanged = { categoryViewModel.setCategoryNameData(it) },
                )

            }
        }

        AnimatedVisibility(categoryUIStates.commonStates.showAlertDialog) {
            AlertWidget(
                categoryUIStates.commonStates.alertTitle,
                categoryUIStates.commonStates.alertText,
                categoryUIStates.commonStates.alertType,
                onDismiss = { categoryViewModel.openAlertDialog(false) },
            ) {
                categoryUIStates.commonStates.alertOnAccept()
            }
        }

    }
}
