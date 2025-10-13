package com.samuelsumbane.ssptdesktop.ui.view.partners

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ProOwnerViewModel
//import com.samuelsumbane.ssptdesktop.presentation.viewmodel.proOwnerViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AlertWidget
import com.samuelsumbane.ssptdesktop.ui.components.CardPItem
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.FormColumn
import com.samuelsumbane.ssptdesktop.ui.components.InfoCard
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete
import ssptdesktop.composeapp.generated.resources.edit

class OwnersScreen : Screen {
    @Composable
    override fun Content() {
        ProOwnersPage()
    }
}

@Composable
fun ProOwnersPage() {
    val proOwnerViewModel by remember { mutableStateOf(getKoin().get<ProOwnerViewModel>()) }
    val proOwnerUIStates by proOwnerViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }

    CommonPageStructure(
        navigator = navigator,
        pageTitle = "Proprietarios",
        topBarActions = {
            NormalButton(icon = null, text = "+ Proprietario") {
                submitButtonText = "Adicionar"
                proOwnerViewModel.openFormDialog(true, "Adicionar proprietario")
            }
        }
    ) {

        FlowRow(modifier = Modifier.padding(10.dp)) {
            proOwnerUIStates.allProOwners.forEach { client ->
                with(client) {
                    InfoCard(modifier = Modifier.size(290.dp, 180.dp)) {
                        Text("proprietario: $name ")
                        CardPItem("Telefone", "$telephone")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {

                            IconButton(
                                onClick = {
                                    submitButtonText = "Actualizar"
                                    proOwnerViewModel.fillFormFields(id, name, telephone)
                                    proOwnerViewModel.openFormDialog(true, "Actualizar proprietario")
                                }
                            ) {
                                Icon(painterResource(Res.drawable.edit), "Edit owner")
                            }

                            Spacer(Modifier.width(10.dp))

                            IconButton(
                                onClick = { proOwnerViewModel.removeProOwner(id) }
                            ) {
                                Icon(painterResource(Res.drawable.delete), "Delete owner")
                            }

                        }
                    }
                }
            }
        }

        if (proOwnerUIStates.commonUiState.showFormDialog) {
            DialogFormModal(
                title = proOwnerUIStates.commonUiState.formDialogTitle,
                onDismiss = { proOwnerViewModel.resetForm() },
                onSubmit = { proOwnerViewModel.onSubmitForm() }
            ) {
                FormColumn {
                    InputField(
                        inputValue = proOwnerUIStates.proOwnerName,
                        label = "proprietario",
                        errorText = proOwnerUIStates.commonUiState.formErrors[FormInputName.OwnerName],
                        onValueChanged = { proOwnerViewModel.fillFormFields(name = it) },
                    )

                    InputField(
                        inputValue = proOwnerUIStates.proOwnerTelephone,
                        label = "Telefone",
                        errorText = proOwnerUIStates.commonUiState.formErrors[FormInputName.OwnerPhone],
                        onValueChanged = { proOwnerViewModel.fillFormFields(telephone = it) },
                        keyboardType = KeyboardType.Phone
                    )
                }
            }
        }

        AnimatedVisibility(proOwnerUIStates.commonUiState.showAlertDialog) {
            AlertWidget(
                proOwnerUIStates.commonUiState.alertTitle,
                proOwnerUIStates.commonUiState.alertText,
                proOwnerUIStates.commonUiState.alertType,
                onDismiss = { proOwnerViewModel.openAlertDialog(false) },
            ) {
                proOwnerUIStates.commonUiState.alertOnAccept()
            }
        }

    }
}
