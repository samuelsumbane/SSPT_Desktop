package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.samuelsumbane.ssptdesktop.kclient.KClientRepository
import com.samuelsumbane.ssptdesktop.ui.utils.ConnectionType
import kotlinx.coroutines.launch

@Composable
fun ShowContentIfConnectedToServer(content: @Composable () -> Unit) {
    var connectionType by remember { mutableStateOf(ConnectionType.Stabilished) }
    val coroutineScope = rememberCoroutineScope()
    val kClientRepo = KClientRepository()

    LaunchedEffect(Unit) {
        connectionType = kClientRepo.checkConnection()
    }

    if (connectionType == ConnectionType.Refused) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f, red = 0.4f), RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Falha ao conectar com o servidor", color = MaterialTheme.colorScheme.onSecondary)

                NormalButton(text = "Actualizar") {
                    coroutineScope.launch {
                        connectionType = kClientRepo.checkConnection()
                    }
                }
            }
        }
    } else content()

}
