package com.samuelsumbane.ssptdesktop.ui.view.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.core.utils.cut
import com.samuelsumbane.ssptdesktop.kclient.IdAndReadState
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.NotificationViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete
import ssptdesktop.composeapp.generated.resources.eye_small

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
        activePage = PageName.SETTINGS.itsName,
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
                    Icon(painterResource(Res.drawable.eye_small), "Set read or not read")
                }

                IconButton(
                    onClick = {
                        notificationViewModel.removeNotification(it.id)
                    }
                ) {
                    Icon(painterResource(Res.drawable.delete), "Delete notification")
                }
            }
        }
    }

    if (notificationUiState.showModal) {
        DialogFormModal(
            title = "Notificação",
            modalSize = ModalSize.MEDIUMN,
            hideSubmitButton = true,
            onDismiss = { notificationViewModel.onDismissNotificationModal() },
            onSubmit = {}
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                with(notificationUiState) {
                    DialogModalText(text = "Notificação para: $notificationUserName")

                    DialogModalText(notificationTitle)

                    DialogModalText(notificationMessage)

                    DialogModalText("Tipo: $notificationType")
                    DialogModalText("Criado em: ${notificationUiState.notificationCreatedAt}")

                    AppCheckBox(
                        checked = markASNotReadCheckBox,
                        text = "Marcar como não lida",
                        onCheck = { notificationViewModel.fillNotificationForm(markASNotReadCheckBox = it, setNotificationReadValue = it) }
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

