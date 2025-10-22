package com.samuelsumbane.ssptdesktop.ui.view.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.core.utils.cut
import com.samuelsumbane.ssptdesktop.kclient.IdAndReadState
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.LogViewModel
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.NotificationViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AppCheckBox
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.DataTable
import com.samuelsumbane.ssptdesktop.ui.components.DatatableText
import com.samuelsumbane.ssptdesktop.ui.components.DialogFormModal
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.delete
import ssptdesktop.composeapp.generated.resources.edit

class LogsScreen : Screen {
    @Composable
    override fun Content() {
        LogsPage()
    }
}

@Composable
fun LogsPage() {
    val navigator = LocalNavigator.currentOrThrow
    val logViewModel by remember { mutableStateOf(getKoin().get<LogViewModel>()) }
    val logUiState by logViewModel.uiState.collectAsState()

    CommonPageStructure(
        navigator,
        pageTitle = "Logs",
    ) {
        DataTable(
            headers = listOf("Nível", "Mensagem", "Modulo", "Criado em", "Usuário", "Ações"),
            rows = logUiState.logs
        ) {
            DatatableText(it.level)
            DatatableText(it.message.cut())
            DatatableText(it.module ?: "")
            DatatableText(it.datetime)
            DatatableText(text = it.userName ?: "")
            Row() {
                IconButton(
                    onClick = {
                        logViewModel.fillLogs(
                            message = it.message,
                            module = it.module,
                            level = it.level,
                            createdAt = it.datetime,
                            userName = it.userName,
                            metadata = it.metadataJson,
                            showModal = true
                        )
                    }
                ) {
                    Icon(painterResource(Res.drawable.edit), "Ver")
                }
            }
        }
    }

    if (logUiState.showModal) {
        DialogFormModal(
            title = "Detalhes do log",
            modalSize = ModalSize.MEDIUMN,
            hideSubmitButton = true,
            onDismiss = { logViewModel.fillLogs(showModal = false) },
            onSubmit = { logViewModel.fillLogs(showModal = false) }
        ) {
            Column(
                modifier = Modifier
//                .fillMaxHeight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                with(logUiState) {
                    DialogModalText(text = "Modulo: $logModule")
                    DialogModalText("Nível: $logLevel")
                    DialogModalText("Conteudo: $logMessage")
                    DialogModalText("Metadados: $logMetadata")
                    DialogModalText("Criado em: $createdLogAt")
                    DialogModalText("Usuário: $logUserName")
                }
            }
        }
    }
}
