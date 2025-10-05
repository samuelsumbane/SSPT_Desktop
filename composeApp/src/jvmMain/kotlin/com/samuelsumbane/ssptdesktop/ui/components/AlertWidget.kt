package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertAcceptFun
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertText
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertTitle
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.alertType
import com.samuelsumbane.ssptdesktop.ui.states.UIStates.showAlertDialog
import com.samuelsumbane.ssptdesktop.ui.utils.AlertType
import com.samuelsumbane.ssptdesktop.ui.utils.ModalSize
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.checkCircle
import ssptdesktop.composeapp.generated.resources.info_circle
import ssptdesktop.composeapp.generated.resources.warning_circle
import ssptdesktop.composeapp.generated.resources.xcircle

@Composable
@Preview
fun AlertWidget() {
    Dialog(
        onDismissRequest = { showAlertDialog = false },
    ) {
        Column(
            modifier = Modifier
                .width(ModalSize.SMALL.widthSize)
                .background(MaterialTheme.colorScheme.background, RoundedCornerShape(14.dp))
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(15.dp))
            when (alertType) {
                AlertType.SUCCESS -> Icon(
                    painterResource(Res.drawable.checkCircle),
                    contentDescription = "Success icon",
                    tint = Color.Green,
                    modifier = Modifier.size(24.dp)
                )
                AlertType.INFO -> Icon(
                    painterResource(Res.drawable.info_circle),
                    contentDescription = "Info icon",
                    tint = Color.Blue
                )
                AlertType.WARNING -> Icon(
                    painterResource(Res.drawable.warning_circle),
                    contentDescription = "Warning icon",
                    tint = Color.Yellow
                )
                AlertType.ERROR -> Icon(
                    painterResource(Res.drawable.xcircle),
                    contentDescription = "Error icon",
                    tint = Color.Red
                )
            }
            Spacer(Modifier.height(15.dp))
            Text(alertTitle, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(10.dp))
            Text(text = alertText)

            Spacer(Modifier.height(25.dp))
            Row(
               horizontalArrangement = Arrangement.End
            ) {
                NormalButton(text = "OK", onClick = alertAcceptFun)
            }

        }
    }
}

