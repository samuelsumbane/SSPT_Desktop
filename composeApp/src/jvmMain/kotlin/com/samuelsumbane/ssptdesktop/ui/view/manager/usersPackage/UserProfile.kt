package com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.UserProfileViewModel
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.FormColumn
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalOutlineButton
import com.samuelsumbane.ssptdesktop.ui.components.TextRow
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import kotlinx.coroutines.launch
import org.jetbrains.skia.paragraph.Alignment
import org.jetbrains.skia.paragraph.TextStyle
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

    CommonPageStructure(
        navigator,
        pageTitle = "Perfil",
        activePage = PageName.MANAGER.itsName,
        enableScroll = false
    ) {
        with(userProfileUiState) {
            // Personal data ------->>
            val personalData = mapOf(
                "Nome" to userName,
                "Email" to userEmail
            )

            // Account data ------->>
            val afData = mapOf(
                "Papel" to userRole,
                "Estado" to userState,
                "Último login" to lastLogin,
            )

            // Security ------->>
            val afSec = mapOf(
                "Senha" to "********",
            )


//
//        Div(attrs = { classes("div-item", "no-border") }) {
//            P {}
//            C.outlineButton("Editar dados pessoais") {
////                    minModalState = "open-min-modal"
////                    console.log("clicked")
//                showMinModal = true
//            }
//        }
//        Br()
//
//        C.h3("Dados da conta")
//        afData.forEach { pItem(it.key, it.value) }
//        Br()
//
//        C.h3("Segurança")
//        afSec.forEach { pItem(it.key, it.value) }
//
//        Div(attrs = { classes("div-item", "no-border") }) {
//            P {}
//            C.outlineButton("Editar senha") {
//                securityModalState = "open-min-modal"
//                afPasscode = ""
//                errors["actualPassword"] = ""
//                afNewPassword = ""
//                afNewPasswordError = ""
//                afConfirmPassword = ""
//                afConfirmPasswordError = ""
//            }
//        }
//        Div(attrs = { classes("div-item", "no-border") }) {
//            P {}
//            C.outlineButton("Encerrar a sessão") {
//                coroutineScope.launch {
//                    val (status, _) = users.logout()
//                    sessionStorage.removeItem("jwt_token")
////                        router.navigate("/")
//                    window.location.href = "/"
//                }
//            }
//        }

            val scrollState = rememberScrollState()

            Row(
                modifier = Modifier
                    .fillMaxSize(),
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
                    personalData.forEach {
                        TextRow(it.key, it.value)
                    }

                    NormalOutlineButton(text = "Editar dados pessoais") {

                        userProfileViewModel.apply {
                            fillFields(showUserPersonalDataModal = true)
                            openFormDialog(true, "Editar dados pessoais")
                        }

                    }
                    HorizontalDivider()

                    ItemsGroupTitle("Dados da conta")
                    afData.forEach {
                        TextRow(it.key, it.value)
                    }
                    HorizontalDivider()

                    ItemsGroupTitle("Segurança")
                    afSec.forEach {
                        TextRow(it.key, it.value)
                    }

                    NormalOutlineButton(text = "Editar a senha") {
                        userProfileViewModel.apply {
                            fillFields(showUserPersonalDataModal = false)
                            userProfileViewModel.openFormDialog(true, "Editar a senha")
                        }
                    }

                    NormalOutlineButton(text = "Encerar a sensão") {

                    }
                }
            }

            if (commonUiState.showFormDialog) {
                DialogFormModal(
                    title = commonUiState.formDialogTitle,
                    onDismiss = { userProfileViewModel.openFormDialog(false) },
                    onSubmit = {},
                    isSubmitEnabled = if (showUserPersonalDataModal) userName.isBlank() && userEmail.isBlank() else actualPassword.isBlank() && newPassword.isBlank() && confirmationPassword.isBlank()
                ) {
                    FormColumn {
                        if (showUserPersonalDataModal) {
                            InputField(
                                inputValue = userName,
                                label = "Nome do usuário",
                                errorText = commonUiState.formErrors[FormInputName.Name],
                                onValueChanged = { userProfileViewModel.fillFields(userName = it) }
                            )

                            InputField(
                                inputValue = userEmail,
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
