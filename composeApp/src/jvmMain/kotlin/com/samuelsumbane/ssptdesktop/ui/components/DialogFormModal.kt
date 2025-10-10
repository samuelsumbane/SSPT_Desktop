package com.samuelsumbane.ssptdesktop.ui.components

//import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
    val scrollbar = rememberScrollState()

    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            modifier = Modifier
                .width(modalSize.widthSize)
                .heightIn(min = 400.dp)
                .fillMaxHeight(0.85f)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(12.dp))
                .onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        onSubmit()
                        true
                    } else false
                }
                .verticalScroll(scrollbar)
                .padding(10.dp),
//            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.padding(bottom = 50.dp)) {
              Text(title.uppercase(), fontWeight = FontWeight.SemiBold)
            }

//            Column(
//                modifier = Modifier
//                    .background(Color.Blue)
//                    .fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(30.dp)
//            ) {
                modalContent()
//            }

            Row(
                modifier = Modifier.padding(top = 50.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NormalOutlineButton(text = "Fechar", onClick = onDismiss)
                NormalButton(text = "Submeter", onClick = onSubmit)
            }
        }
    }
}
