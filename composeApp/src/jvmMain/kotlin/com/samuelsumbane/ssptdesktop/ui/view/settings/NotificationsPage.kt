package com.samuelsumbane.ssptdesktop.ui.view.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.core.utils.cut
import com.samuelsumbane.ssptdesktop.kclient.IdAndReadState
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.NotificationViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AppCheckBox
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DataTable
import com.samuelsumbane.ssptdesktop.ui.components.DatatableText
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.components.FormColumn
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.edit

class NotificationsScreen : Screen {
    @Composable
    override fun Content() {
        NotificationPage()
    }
}

@Composable
fun NotificationPage() {
    val navigator = LocalNavigator.currentOrThrow
    val notificationViewModel by remember { mutableStateOf(getKoin().get<NotificationViewModel>()) }
    val notificationUiState by notificationViewModel.uiState.collectAsState()

    CommonPageStructure(
        navigator,
        pageTitle = "Notificações",
    ) {
        DataTable(
            headers = listOf("Usuário", "Titulo", "Mensagem", "Tipo", "Criado em", "Vista", "Ações"),
            rows = notificationUiState.notifications
        ) {
            DatatableText(it.userName ?: "")
            DatatableText(it.title)
            DatatableText(it.message.cut())
            DatatableText(it.type)
            DatatableText(it.createdAt)
            DatatableText(text = if (it.read) "Vista" else "Não vista")
            Row() {
                IconButton(
                    onClick = {
                        notificationViewModel.fillNotificationForm(
                            id = it.id,
                            userName = it.userName,
                            title = it.title,
                            message = it.message,
                            type = it.type,
                            createdAt = it.type,
                            showModal = true
                        )
                    }
                ) {
                    Icon(painterResource(Res.drawable.edit), "Set read or not read")
                }
            }
        }
    }

    if (notificationUiState.showModal) {
        DialogFormModal(
            title = "Notificação",
            modalSize = ModalSize.MEDIUMN,
            hideSubmitButton = true,
            onDismiss = { notificationViewModel.fillNotificationForm(showModal = false) },
            onSubmit = {
                notificationViewModel.editNotification(
                    IdAndReadState(
                        notificationUiState.notificationId,
                        isRead = true
                    )
                )
                notificationViewModel.fillNotificationForm(showModal = false)
            }
        ) {
            Column(
                modifier = Modifier
//                .fillMaxHeight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                with(notificationUiState) {
                    DialogModalText(text = "Notificação para: $notificationUserName")

                    DialogModalText(notificationTitle)

                    DialogModalText(notificationMessage)

                    DialogModalText("Tipo: $notificationType")
                    DialogModalText("Criado em: ${notificationUiState.notificationCreatedAt}")

                    AppCheckBox(
                        checked = false,
                        text = "Marcar como não lida",
                        onCheck = {
                            notificationViewModel.editNotification(
                                IdAndReadState(
                                    notificationUiState.notificationId,
                                    isRead = it
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun DialogModalText(text: String) {
    Text(text, color = MaterialTheme.colorScheme.onBackground)
}

