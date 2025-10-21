package com.samuelsumbane.ssptdesktop.ui.view.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ConfigViewModel
import com.samuelsumbane.ssptdesktop.ui.components.AppCheckBox
import com.samuelsumbane.ssptdesktop.ui.components.CommonPageStructure
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.koin.java.KoinJavaComponent.getKoin


data class ConfigDetailsDc( // Dc (Data class) ------->>
    val id: Int,
    val title: String,
    val description: String,
    val inputName: String,
    val readOnly: Boolean = false
)

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        SettingsPage()
    }
}

@Composable
fun SettingsPage() {
    val navigator = LocalNavigator.currentOrThrow
    val configViewModel by remember { mutableStateOf(getKoin().get<ConfigViewModel>())}
    val configUiState by configViewModel.uiState.collectAsState()
    var sysConfigDetails by remember { mutableStateOf(mutableListOf<ConfigDetailsDc>()) }

    CommonPageStructure(
        navigator,
        pageTitle = "Configurações",
        activePage = PageName.SETTINGS.itsName
    ) {
        LaunchedEffect(Unit) {
            configViewModel.setLoading(true)
            try {
                configUiState.configs.also{
                    it.forEach { sys ->
                        if (sys.key != "notifyMaxSellValue") {
                            sysConfigDetails.add(
                                ConfigDetailsDc(
                                    sys.id,
                                    title = when (sys.key) {
                                        "percentual_iva" -> "Percentagem de IVA"
                                        "active_package" -> "Pacote do sistema (Apenas leitura)"
                                        "alert_min_pro_quantity" -> "Alerta de productos"
                                        "notifyMaxSell" -> "Notificar vendas elevadas"
                                        else -> ""
                                    },
                                    description = when (sys.key) {
                                        "percentual_iva" -> "Será calculada nas vendas"
                                        "active_package" -> "Sistema executa as funcionalidades do pacote"
                                        "alert_min_pro_quantity" -> "Alertar quando o producto atingir a sua quantidade minima"
                                        "notifyMaxSell" -> "Receber notificação de vendas elevadas feitas no sistema"
                                        else -> ""
                                    },
                                    inputName = sys.key,
                                    readOnly = sys.key == "percentual_iva"
                                )
                            )
                        }

                        when (sys.key) {
                            "notifyMaxSellValue" -> configViewModel.editConfiguration(maxSellValue = sys.doubleValue ?: 0.0)
                            "percentual_iva" -> configViewModel.editConfiguration(percentualIva = sys.intValue ?: 0)
                            "alert_min_pro_quantity" -> configViewModel.editConfiguration(alertMinProQuantity = sys.boolValue ?: false)
                            "notifyMaxSell" -> configViewModel.editConfiguration(isNotifyMaxSellChecked = sys.boolValue ?: false)
                            "active_package" -> configViewModel.editConfiguration(activePackage = sys.stringValue ?: "")
                        }
                    }
                }

            } catch (e: Exception) {
                println("Error: $e")
            } finally {
                configViewModel.setLoading(false)
            }
//        /basicSettingsPage //178
        }

//        AppCheckBox(
//            checked = configUiState.isNotifyMaxSellChecked,
//            text = "Limpar o formulario quando submeter",
//            onCheck = { configViewModel.editConfiguration(isNotifyMaxSellChecked = it) }
//        )

        LazyColumn {
            items(sysConfigDetails) {
//                NormalButton(text = "dfas") {}
                Column(

                ) {
                    Text(text = it.title)
                    Text(text = it.description)
                    when (it.inputName) {
                        "notifyMaxSellValue" -> {
                            InputField(
                                inputValue = configUiState.notifyMaxSellValue.toString(),
                                label = "Valor maximo da compra",
                                onValueChanged = { configViewModel.editConfiguration(maxSellValue = it.toDouble()) }
                            )
                        }

                        "percentual_iva" -> {
                            InputField(
                                inputValue = configUiState.percentualIva.toString(),
                                label = "Percentual de IVA",
                                onValueChanged = { configViewModel.editConfiguration(percentualIva = it.toInt()) }
                            )
                        }

                        "alert_min_pro_quantity" -> {
                            AppCheckBox(
                                checked = configUiState.isNotifyMaxSellChecked,
                                text = "Limpar o formulario quando submeter",
                                onCheck = { configViewModel.editConfiguration(isNotifyMaxSellChecked = it) }
                            )
                        }

                        "notifyMaxSell" -> {
                            AppCheckBox(
                                checked = configUiState.isNotifyMaxSellChecked,
                                text = "Limpar o formulario quando submeter",
                                onCheck = { configViewModel.editConfiguration(isNotifyMaxSellChecked = it) }
                            )
                        }

                        "active_package" -> {
                            InputField(
                                inputValue = configUiState.packageName,
                                label = "Pacote do sistema",
                                enabled = false,
                                onValueChanged = {}
                            )
                        }
                    }
                }
            }
        }

    }
}