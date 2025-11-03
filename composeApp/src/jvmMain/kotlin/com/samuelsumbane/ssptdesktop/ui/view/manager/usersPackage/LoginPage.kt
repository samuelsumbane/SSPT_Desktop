package com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.LoginUserViewModel
import com.samuelsumbane.ssptdesktop.ui.components.FormColumn
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import com.samuelsumbane.ssptdesktop.ui.view.HomeScreen
import org.koin.java.KoinJavaComponent.getKoin


class LoginScreen : Screen {
    @Composable
    override fun Content() {
        LoginPage()
    }
}

@Composable
fun LoginPage() {
    val userViewModel by remember { mutableStateOf(getKoin().get<LoginUserViewModel>()) }
    val userUiState by userViewModel.uiState.collectAsState()
    val navigator = LocalNavigator.currentOrThrow

    userUiState.redirectTo?.let { navigator.push(HomeScreen()) }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter) {
                    userViewModel.onSubmitLoginUserForm()
                    true
                } else false
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FormColumn(
            modifier = Modifier
                .width(400.dp)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(15.dp))
                .padding(10.dp)
        ) {
            Text("SSPT", fontWeight = FontWeight.Bold)

            InputField(
                inputValue = userUiState.userEmail,
                label = "Email",
                errorText = userUiState.formErrors[FormInputName.Email],
                onValueChanged = { userViewModel.fillLoginUserForm(userEmail = it) }
            )

            InputField(
                inputValue = userUiState.userPassword,
                label = "Senha",
                isPassword = true,
                errorText = userUiState.formErrors[FormInputName.CurrentPassword],
                onValueChanged = { userViewModel.fillLoginUserForm(userPassword = it) },
                keyboardType = KeyboardType.Password
            )

            NormalButton(text = "Login") { userViewModel.onSubmitLoginUserForm() }

            Text(userUiState.errorMessage, color = Color(red = 205, green = 5, blue = 5, alpha = 255))
        }
    }
}