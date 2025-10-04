package com.samuelsumbane.ssptdesktop.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.samuelsumbane.ssptdesktop.ClientItem
import com.samuelsumbane.ssptdesktop.core.utils.generateId
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.showFormDialog
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.InfoCard
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.states.AppState.formErrors
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertAcceptFun
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertText
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertTitle
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertType
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.showAlertDialog
//import com.samuelsumbane.ssptdesktop.ui.states.AppState.formErrors
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName

@Composable
fun ClientsPage(clientViewModel: ClientViewModel) {
    CommonPageStructure(
        clientViewModel = clientViewModel,
        topBarActions = {
            NormalButton(icon = null, text = "+ Cliente") {
                showFormDialog = true
            }
        }
    ) {

        var clientId by remember { mutableStateOf(0) }
        var clientName by remember { mutableStateOf("") }
        var clientTelephone by remember { mutableStateOf("") }
        val allClients by clientViewModel.allClients.collectAsState()

        FlowRow(
            modifier = Modifier
                .padding(10.dp)
        ) {
            allClients.forEach {client ->
                with(client) {
                    InfoCard() {
                        Text("Nome: $name ")
                        Text("Telefone: $telephone ")

                        Row(
                            modifier = Modifier.padding(top = 30.dp)
                        ) {
                            NormalButton(
                                text = "Editar",
                            ) {
                                clientId = id!! /** Now id in ClientItem is nullable */
                                clientName = name
                                clientTelephone = telephone
                                showFormDialog = true
                            }
                        }
                    }
                }
            }
        }

        fun cleanFormAndCloseModal() {
            clientName = ""
            clientTelephone = ""
            showFormDialog = false
        }


        AnimatedVisibility (showFormDialog) {
            DialogFormModal("Adicionar cliente",
                onDismiss = { cleanFormAndCloseModal() },
                onSubmit = {
                    if (clientName.isBlank()) {
                        formErrors[FormInputName.ClientName.inString] = "Nome do cliente é obrigatório"
                        return@DialogFormModal
                    }
                    formErrors[FormInputName.ClientName.inString] = ""

                    if (clientTelephone.isBlank()) {
                        formErrors[FormInputName.ClientPhone.inString] = "Telefone do cliente é obrigatório"
                        return@DialogFormModal
                    }
                    formErrors[FormInputName.ClientPhone.inString]

                    val clientData = ClientItem(
                        id = if (clientId != 0) clientId else generateId(2),
                        name = clientName,
                        telephone = clientTelephone
                    )

                    alertText = if (clientId != 0) {
                        clientViewModel.editTheClient(clientData)
                        "Cliente editado com sucesso."
                    } else {
                        clientViewModel.addTheClient(clientData)
                        "Cliente adicionado com sucesso."
                    }
                    alertTitle = "Sucesso"

                    cleanFormAndCloseModal()
                    showAlertDialog = true
                    alertAcceptFun = { showAlertDialog = false }
                }
            ) {
                InputField(
                    inputValue = clientName,
                    label = "Nome do cliente",
                    errorText = formErrors[FormInputName.ClientName.inString],
                    onValueChanged = { clientName = it },
                )

                InputField(
                    inputValue = clientTelephone,
                    label = "Telefone",
                    errorText = formErrors[FormInputName.ClientPhone.inString],
                    onValueChanged = { clientTelephone = it },
                    keyboardType = KeyboardType.Phone
                )
            }
        }

        AnimatedVisibility(showAlertDialog) {
            AlertWidget()
        }

    }
}
