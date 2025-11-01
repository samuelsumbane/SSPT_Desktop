package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun InputNum(
//    inputValue: String,
//    label: String? = null,
//    keyboardType: KeyboardType = KeyboardType.Number,
//    onValueChanged: (String) -> Unit,
//) {
//    BasicTextField(
//        value = TextFieldValue,
//        onValueChange = {
//
//        }
//    )
//}




@Composable
fun InputField(
    inputValue: String,
    label: String? = null,
    errorText: String? = null,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: (() -> Unit)? = null
) {
    // toggle para mostrar/ocultar senha (se aplicável)
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation: VisualTransformation = when {
        isPassword && !passwordVisible -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    Column(
//        modifier = Modifier.background(Color.Blue)
    ) {
        OutlinedTextField(
            value = inputValue,
            onValueChange = { newValue ->
                // filtragem simples por tipo de teclado (útil no Desktop)
                val filtered = filterByKeyboardType(newValue, keyboardType)
                onValueChanged(filtered)
            },
            label = { label?.let { Text(it) } },
            singleLine = singleLine,
            enabled = enabled,
            visualTransformation = visualTransformation,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
    //                    val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
    //                    Icon(imageVector = icon, contentDescription = if (passwordVisible) "Ocultar senha" else "Mostrar senha")
                    }
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            keyboardActions = KeyboardActions(
                onDone = { onImeAction?.invoke() }
            ),
            modifier = modifier.fillMaxWidth(0.9f)
                .padding(0.dp)
        )

        errorText?.let { ErrorText(errorText) }
    }
}

private fun filterByKeyboardType(value: String, keyboardType: KeyboardType): String {
    return when (keyboardType) {
        KeyboardType.Number -> value.filter { it.isDigit() }
        KeyboardType.Decimal -> value.filter { it.isDigit() || it == '.' || it == ',' }
        KeyboardType.Phone -> value.filter { it.isDigit() || it == '+' || it == '-' || it == ' ' || it == '(' || it == ')' }
        KeyboardType.Email -> value.replace(" ", "") // só removemos espaços (não filtramos demais)
        else -> value
    }
}