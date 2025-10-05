package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize

@Composable
fun DialogFormModal(
    title: String,
    modalSize: ModalSize = ModalSize.SMALL,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    modalContent: @Composable () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .width(modalSize.widthSize)
                .heightIn(min = 400.dp)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        onSubmit()
                        true
                    } else false
                }
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row() {
              Text(title.uppercase(), fontWeight = FontWeight.SemiBold)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                modalContent()
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NormalOutlineButton(text = "Fechar", onClick = onDismiss)
                NormalButton(text = "Submeter", onClick = onSubmit)
            }
        }
    }
}
