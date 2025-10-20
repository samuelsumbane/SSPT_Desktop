package com.samuelsumbane.ssptdesktop.ui.view.manager

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.createSettings
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.BranchViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ConfigEntry
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ConfigScreenViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.add
import ssptdesktop.composeapp.generated.resources.current_location
import ssptdesktop.composeapp.generated.resources.edit
import ssptdesktop.composeapp.generated.resources.not_current_location

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
    var systemLocationId by remember { mutableStateOf(0) }
//    var clientTelephone by remember { mutableStateOf("") }
    val configViewModel = remember { ConfigScreenViewModel(createSettings()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val defaultConfigs = configViewModel.loadConfigurations()
        systemLocationId = defaultConfigs.systemLocationId
    }

    CommonPageStructure(
        navigator,
        pageTitle = "Sucursais",
        topBarActions = {
            NormalButton(icon = null, text = "+ Sucursal") {
                submitButtonText = "Adicionar"
                branchViewModel.openFormDialog(true, "Adicionar sucursal")
            }
        }
    ) {


        CustomFlowRow {
            branchUiStates.branches.forEach { branch ->
                with(branch) {
                    InfoCard(
                        isActive = systemLocationId == id,
                        modifier = Modifier.size(310.dp, 210.dp)
                    ) {
                        Text("Nome: $name ")
                        Text("Endereço: $address ")

                        Row(modifier = Modifier.padding(top = 30.dp)) {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        configViewModel.saveConfiguration(ConfigEntry.AppConfigLocationId, id)
                                        systemLocationId = id
                                    }
                                }
                            ) {
                                if (id == systemLocationId) {
                                    Icon(painterResource(Res.drawable.current_location), "System location defined on this device")
                                } else {
                                    Icon(painterResource(Res.drawable.not_current_location), "System location that is not defined on this device")
                                }
                            }

                            IconButton(
                                onClick = {
                                    submitButtonText = "Actualizar"
                                    branchViewModel.fillFormFields(id, name, address)
                                    branchViewModel.openFormDialog(true, "Actualizar sucursal")
                                }
                            ) {
                                Icon(painterResource(Res.drawable.edit), "Edit branch")
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
                onSubmit = { branchViewModel.onBranchSubmitForm() }
            ) {
                FormColumn {
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
