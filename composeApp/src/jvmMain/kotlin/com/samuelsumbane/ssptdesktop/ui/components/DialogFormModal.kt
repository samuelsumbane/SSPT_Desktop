package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogFormModal(
    title: String,
    modalSize: ModalSize = ModalSize.SMALL,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean = false,
    hideSubmitButton: Boolean = false,
    enableScroll: Boolean = true,
    modalContent: @Composable () -> Unit
) {
    val scrollbar = rememberScrollState()

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Column(
            modifier = Modifier
                .width(modalSize.widthSize)
                .heightIn(min = 300.dp)
                .fillMaxHeight(0.85f)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        onSubmit()
                        true
                    } else false
                }
                .possiblyVerticalScroll(enableScroll, scrollbar)
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title.uppercase(), fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.padding(top = 10.dp, start = 5.dp))

            modalContent()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NormalOutlineButton(text = "Fechar", onClick = onDismiss)
                if (!hideSubmitButton) {
                    NormalButton(
                        text = "Submeter",
                        isLoading = isLoading,
                        onClick = onSubmit
                    )
                }
            }
        }
    }
}

