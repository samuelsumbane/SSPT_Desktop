package com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.kclient.ChangeStatusDC
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.UserViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete
import ssptdesktop.composeapp.generated.resources.edit

class UsersScreen : Screen {
    @Composable
    override fun Content() {
        UsersPage()
    }
}

@Composable
fun UsersPage() {
    val userViewModel by remember { mutableStateOf(getKoin().get<UserViewModel>()) }
    val userUIStates by userViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow
    var submitButtonText by remember { mutableStateOf("") }

    CommonPageStructure(
        navigator = navigator,
        pageTitle = "Usuários",
        activePage = PageName.MANAGER.itsName,
        topBarActions = {
            NormalButton(icon = null, text = "+ Usuário") {
                submitButtonText = "Adicionar"
                userViewModel.openFormDialog(true, "Adicionar usuário")
            }
        }
    ) {

        CustomFlowRow {
            userUIStates.users.forEach { user ->
                with(user) {
                    InfoCard(modifier = Modifier.size(260.dp, 220.dp)) {
                        CardPItem("Usuário", name)
                        CardPItem("Telefone", "")
                        CardPItem("Papel", role)
                        CardPItem("Estado", status)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = {
                                    userViewModel.changeUserStatus(
                                        ChangeStatusDC(
                                            status = 1, // Come back here.
                                            userId = id
                                        )
                                    )
                                },
                            ) {
                                Icon(painterResource(Res.drawable.edit), contentDescription = "Edit product")
                            }

                            Spacer(Modifier.width(10.dp))

                            IconButton(
                                onClick = { userViewModel.removeUser(id) },
                            ) {
                                Icon(painterResource(Res.drawable.delete), contentDescription = "Delete product")
                            }
                        }
                    }
                }
            }
        }


        if (userUIStates.commonUiState.showFormDialog) {
            DialogFormModal(
                title = userUIStates.commonUiState.formDialogTitle,
                onDismiss = { userViewModel.resetForm() },
                onSubmit = { userViewModel.onUserFormSubmit() }
            ) {
                FormColumn {
                    InputField(
                        inputValue = userUIStates.userName,
                        label = "Nome",
                        errorText = userUIStates.commonUiState.formErrors[FormInputName.Name],
                        onValueChanged = { userViewModel.fillUserForm(name = it) },
                    )

                    InputField(
                        inputValue = userUIStates.userEmail,
                        label = "Email",
                        errorText = userUIStates.commonUiState.formErrors[FormInputName.Email],
                        onValueChanged = { userViewModel.fillUserForm(email = it) },
                    )

                    DropDown(
                        label = "Papel",
                        text = userUIStates.userRole,
                        errorText = "",
                        expanded = userUIStates.roleDropdownExpanded,
                        onDismiss = { userViewModel.fillUserForm(roleDropdownExpanded = false) },
                        onDropdownClicked = { userViewModel.fillUserForm(roleDropdownExpanded = !userUIStates.roleDropdownExpanded) }
                    ) {
                        listOf("Vendedor/Caixa", "Gerente", "Administrador", "Estoquista").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = { userViewModel.fillUserForm(role = it) }
                            )
                        }
                        if (userUIStates.userRole.isBlank()) userViewModel.fillUserForm(role = "Vendedor/Caixa")
                    }
                }
            }
        }

        AnimatedVisibility(userUIStates.commonUiState.showAlertDialog) {
            AlertWidget(
                userUIStates.commonUiState.alertTitle,
                userUIStates.commonUiState.alertText,
                userUIStates.commonUiState.alertType,
                onDismiss = { userViewModel.openAlertDialog(false) },
            ) {
                userUIStates.commonUiState.alertOnAccept()
            }
        }

    }
}