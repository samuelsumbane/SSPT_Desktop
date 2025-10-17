package com.samuelsumbane.ssptdesktop.ui.view.manager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.BranchViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.koin.java.KoinJavaComponent.getKoin

class BranchScreen : Screen {
    @Composable
    override fun Content() {
        BranchPage()
    }
}

@Composable
fun BranchPage() {
    val branchViewModel by remember { mutableStateOf(getKoin().get<BranchViewModel>()) }
    val branchUiStates by branchViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }
//    var clientTelephone by remember { mutableStateOf("") }

    CommonPageStructure(
        navigator,
        topBarActions = {
            NormalButton(icon = null, text = "+ Cliente") {
                submitButtonText = "Adicionar"
                branchViewModel.openFormDialog(true, "Adicionar cliente")
            }
        }
    ) {


        FlowRow(
            modifier = Modifier.padding(10.dp)
        ) {
            branchUiStates.branches.forEach { client ->
                with(client) {
                    InfoCard() {
                        Text("Nome: $name ")
                        Text("Endereço: $address ")

                        Row(
                            modifier = Modifier.padding(top = 30.dp)
                        ) {
                            NormalButton(text = "Editar") {
                                submitButtonText = "Actualizar"
                                branchViewModel.fillFormFields(id, name, address)
                                branchViewModel.openFormDialog(true, "Actualizar sucursal")
                            }
                        }
                    }
                }
            }
        }

        if (branchUiStates.commonUiState.showFormDialog) {
            DialogFormModal(
                title = branchUiStates.commonUiState.formDialogTitle,
                onDismiss = { branchViewModel.resetForm() },
                onSubmit = { branchViewModel }
            ) {
                InputField(
                    inputValue = branchUiStates.branchName,
                    label = "Nome da branch",
                    errorText = branchUiStates.commonUiState.formErrors[FormInputName.BranchName],
                    onValueChanged = { branchViewModel.fillFormFields(branchName = it) },
                )

                InputField(
                    inputValue = branchUiStates.branchAddress,
                    label = "Endereço de branch",
                    errorText = branchUiStates.commonUiState.formErrors[FormInputName.Address],
                    onValueChanged = { branchViewModel.fillFormFields(branchAddress = it) },
                )
            }
        }

        AnimatedVisibility(branchUiStates.commonUiState.showAlertDialog) {
            AlertWidget(
                branchUiStates.commonUiState.alertTitle,
                branchUiStates.commonUiState.alertText,
                branchUiStates.commonUiState.alertType,
                onDismiss = { branchViewModel.openAlertDialog(false) },
            ) {
                branchUiStates.commonUiState.alertOnAccept()
            }
        }

    }
}
