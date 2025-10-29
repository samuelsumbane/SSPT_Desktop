package com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuel.oremoschanganapt.globalComponents.showSnackbar
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.UserProfileViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.koin.java.KoinJavaComponent.getKoin


class UserProfileScreen : Screen {
    @Composable
    override fun Content() {
        UserProfile()
    }
}

@Composable
fun UserProfile() {
    val navigator = LocalNavigator.currentOrThrow
    val userProfileViewModel by remember { mutableStateOf(getKoin().get<UserProfileViewModel>()) }
    val userProfileUiState by userProfileViewModel.uiState.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()


    CommonPageStructure(
        navigator,
        pageTitle = "Perfil",
        activePage = PageName.MANAGER.itsName,
        enableScroll = false,
        onPerfomeSnackbarHost = {
            if (userProfileUiState.commonUiState.showSnackbar) {
                showSnackbar(
                    scope = coroutineScope,
                    snackbarHostState = it,
                    message = userProfileUiState.commonUiState.snackbarMessage
                )
                userProfileViewModel.displaySnackbar(false)
            }
        }
    ) {
        with(userProfileUiState) {
            // Personal data
            val personalData = mapOf(
                "Nome" to userData.name,
                "Email" to userData.email
            )

            // Account data
            val afData = mapOf(
                "Papel" to userData.role,
                "Estado" to userData.status,
                "Último login" to userData.lastLogin,
            )

            // Security ------->>
            val afSec = mapOf(
                "Senha" to "********",
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight(0.85f)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
//                    .align(androidx.compose.ui.Alignment.CenterHorizontally)
                        .padding(20.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    ItemsGroupTitle("Dados pessoais")
                    personalData.forEach { TextRow(it.key, it.value) }

                    NormalOutlineButton(text = "Editar dados pessoais") {
                        userProfileViewModel.openFormDialog(true, "Editar dados pessoais", true)
                        userProfileViewModel.fillFields(
                            userId = userData.id,
                            userName = userData.name,
                            userEmail = userData.email
                        )
                    }

                    HorizontalDivider()

                    ItemsGroupTitle("Dados da conta")
                    afData.forEach { TextRow(it.key, it.value) }
                    HorizontalDivider()

                    ItemsGroupTitle("Segurança")
                    afSec.forEach { TextRow(it.key, it.value) }

                    NormalOutlineButton(text = "Editar a senha") {
                        userProfileViewModel.openFormDialog(true, "Editar a senha", true)
                        userProfileViewModel.fillFields(userId = userData.id)
                    }

                    NormalOutlineButton(text = "Encerar a sensão") {

                    }
                }
            }

            if (commonUiState.showFormDialog) {
                DialogFormModal(
                    title = commonUiState.formDialogTitle,
                    onDismiss = { userProfileViewModel.openFormDialog(false) },
                    onSubmit = { userProfileViewModel.onSubmitForm() },
                    isSubmitEnabled = if (showUserPersonalDataModal) !newUserName.isBlank() && !newUserName.isBlank() else !actualPassword.isBlank() && !newPassword.isBlank() && !confirmationPassword.isBlank()
                ) {
                    FormColumn {
                        if (showUserPersonalDataModal) {
                            InputField(
                                inputValue = newUserName,
                                label = "Nome do usuário",
                                errorText = commonUiState.formErrors[FormInputName.Name],
                                onValueChanged = { userProfileViewModel.fillFields(userName = it) }
                            )

                            InputField(
                                inputValue = newUserEmail,
                                label = "Email",
                                errorText = commonUiState.formErrors[FormInputName.Email],
                                onValueChanged = { userProfileViewModel.fillFields(userEmail = it) }
                            )
                        } else {
                            InputField(
                                inputValue = actualPassword,
                                label = "Senha actual",
                                errorText = commonUiState.formErrors[FormInputName.CurrentPassword],
                                onValueChanged = { userProfileViewModel.fillFields(actualPassword = it) }
                            )

                            InputField(
                                inputValue = newPassword,
                                label = "Nova senha",
                                errorText = commonUiState.formErrors[FormInputName.NewPassword],
                                onValueChanged = { userProfileViewModel.fillFields(newPassword = it) }
                            )

                            InputField(
                                inputValue = confirmationPassword,
                                label = "Confirmar a senha",
                                errorText = commonUiState.formErrors[FormInputName.ConfirmationPassword],
                                onValueChanged = { userProfileViewModel.fillFields(confirmationPassword = it) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemsGroupTitle(text: String) = Text(text = text, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(top = 20.dp))
// was 4224