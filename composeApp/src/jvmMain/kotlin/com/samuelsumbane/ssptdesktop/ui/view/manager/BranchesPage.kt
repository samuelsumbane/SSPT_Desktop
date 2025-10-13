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
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
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
    val clientViewModel by remember { mutableStateOf(getKoin().get<ClientViewModel>()) }
    val clientUIStates by clientViewModel.uiStates.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }
//    var clientTelephone by remember { mutableStateOf("") }

    CommonPageStructure(
        navigator,
        topBarActions = {
            NormalButton(icon = null, text = "+ Cliente") {
                submitButtonText = "Adicionar"
                clientViewModel.openFormDialog(true, "Adicionar cliente")
            }
        }
    ) {


        FlowRow(
            modifier = Modifier.padding(10.dp)
        ) {
            clientUIStates.clients.forEach { client ->
                with(client) {
                    InfoCard() {
                        Text("Nome: $name ")
                        Text("Telefone: $telephone ")

                        Row(
                            modifier = Modifier.padding(top = 30.dp)
                        ) {
                            NormalButton(text = "Editar") {
                                submitButtonText = "Actualizar"
                                clientViewModel.fillAllForm(id!!, name, telephone)
                                clientViewModel.openFormDialog(true, "Actualizar cliente")
                            }
                        }
                    }
                }
            }
        }

        if (clientUIStates.common.showFormDialog) {
            DialogFormModal(
                title = clientUIStates.common.formDialogTitle,
                onDismiss = { clientViewModel.resetForm() },
                onSubmit = {
                    clientViewModel.onSubmitClientForm()
                }
            ) {
                InputField(
                    inputValue = clientUIStates.clientName,
                    label = "Nome da branch",
                    errorText = clientUIStates.common.formErrors[FormInputName.BranchName],
                    onValueChanged = { clientViewModel.setClientNameData(it) },
                )

                InputField(
                    inputValue = clientUIStates.clientPhone,
                    label = "Endereço de branch",
                    errorText = clientUIStates.common.formErrors[FormInputName.Address],
                    onValueChanged = { clientViewModel.setClientPhoneData(it) },
                )
            }
        }

        AnimatedVisibility(clientUIStates.common.showAlertDialog) {
            AlertWidget(
                clientUIStates.common.alertTitle,
                clientUIStates.common.alertText,
                clientUIStates.common.alertType,
                onDismiss = { clientViewModel.openAlertDialog(false) },
            ) {
                clientUIStates.common.alertOnAccept()
            }
        }

    }
}
