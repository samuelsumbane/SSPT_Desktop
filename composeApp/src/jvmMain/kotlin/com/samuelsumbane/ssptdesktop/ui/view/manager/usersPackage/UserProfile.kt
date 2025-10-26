package com.samuelsumbane.ssptdesktop.ui.view.manager.usersPackage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.NormalOutlineButton
import com.samuelsumbane.ssptdesktop.ui.components.TextRow
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import kotlinx.coroutines.launch
import org.jetbrains.skia.paragraph.Alignment
import org.jetbrains.skia.paragraph.TextStyle


class UserProfileScreen : Screen {
    @Composable
    override fun Content() {
        UserProfile()
    }
}

@Composable
fun UserProfile() {
    val navigator = LocalNavigator.currentOrThrow

    CommonPageStructure(
        navigator,
        pageTitle = "Perfil",
        activePage = PageName.MANAGER.itsName,
        enableScroll = false
    ) {

        // Personal data ------->>
        val personalData = mapOf(
            "Nome" to "",
            "Email" to ""
        )

        // Account data ------->>
        val afData = mapOf(
            "Papel" to "",
            "Estado" to "",
            "Último login" to "",
        )

        // Security ------->>
        val afSec = mapOf(
            "Senha" to "********",
        )


//
//        Div(attrs = { classes("div-item", "no-border") }) {
//            P {}
//            C.outlineButton("Editar dados pessoais") {
////                    minModalState = "open-min-modal"
////                    console.log("clicked")
//                showMinModal = true
//            }
//        }
//        Br()
//
//        C.h3("Dados da conta")
//        afData.forEach { pItem(it.key, it.value) }
//        Br()
//
//        C.h3("Segurança")
//        afSec.forEach { pItem(it.key, it.value) }
//
//        Div(attrs = { classes("div-item", "no-border") }) {
//            P {}
//            C.outlineButton("Editar senha") {
//                securityModalState = "open-min-modal"
//                afPasscode = ""
//                errors["actualPassword"] = ""
//                afNewPassword = ""
//                afNewPasswordError = ""
//                afConfirmPassword = ""
//                afConfirmPasswordError = ""
//            }
//        }
//        Div(attrs = { classes("div-item", "no-border") }) {
//            P {}
//            C.outlineButton("Encerrar a sessão") {
//                coroutineScope.launch {
//                    val (status, _) = users.logout()
//                    sessionStorage.removeItem("jwt_token")
////                        router.navigate("/")
//                    window.location.href = "/"
//                }
//            }
//        }

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .fillMaxHeight(0.85f)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp))
                .align(androidx.compose.ui.Alignment.CenterHorizontally)
                .padding(20.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Text("Dados pessoais",)
            personalData.forEach {
                TextRow(it.key, it.value)
            }

            NormalOutlineButton(text = "Editar dados pessoais") {

            }

            Text("Dados da conta")
            afData.forEach {
                TextRow(it.key, it.value)
            }

            Text("Segurança")
            afSec.forEach {
                TextRow(it.key, it.value)
            }

            NormalOutlineButton(text = "Editar a senha") {

            }

            NormalOutlineButton(text = "Encerar a sensão") {

            }
        }


    }
}

@Composable
fun ItemsGroupTitle() {
//    Text
}