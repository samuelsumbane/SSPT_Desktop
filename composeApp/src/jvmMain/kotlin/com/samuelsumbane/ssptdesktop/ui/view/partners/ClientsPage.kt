package com.samuelsumbane.ssptdesktop.ui.view.partners

//import com.samuelsumbane.ssptdesktop.ui.states.AppState.formErrors
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.koin.java.KoinJavaComponent.getKoin


class ClientsScreen : Screen {
    @Composable
    override fun Content() {
        ClientsPage()
    }
}

@Composable
fun ClientsPage() {
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


        FlowRow(modifier = Modifier.padding(10.dp)) {
            clientUIStates.clients.forEach { client ->
                with(client) {
                    InfoCard(modifier = Modifier.size(300.dp, 200.dp)) {
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
                FormColumn(modifier = Modifier.fillMaxWidth()) {
                    InputField(
                        inputValue = clientUIStates.clientName,
                        label = "Nome",
                        errorText = clientUIStates.common.formErrors[FormInputName.ClientName],
                        onValueChanged = { clientViewModel.setClientNameData(it) },
                    )

                    InputField(
                        inputValue = clientUIStates.clientPhone,
                        label = "Telefone",
                        errorText = clientUIStates.common.formErrors[FormInputName.ClientPhone],
                        onValueChanged = { clientViewModel.setClientPhoneData(it) },
                        keyboardType = KeyboardType.Phone
                    )
                }
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
