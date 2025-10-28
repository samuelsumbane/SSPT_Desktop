package com.samuelsumbane.ssptdesktop.ui.view.sell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuel.oremoschanganapt.globalComponents.showSnackbar
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.SaleViewModel
import com.samuelsumbane.ssptdesktop.ui.components.*
import com.samuelsumbane.ssptdesktop.ui.utils.ProductQuantityAction
import org.jetbrains.compose.resources.painterResource
import org.koin.java.KoinJavaComponent.getKoin
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.add
import ssptdesktop.composeapp.generated.resources.arrow_forward
import ssptdesktop.composeapp.generated.resources.remove

class SaleModalScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Pagina de vendas".uppercase()) })
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState)
            }
        ) {
            val colorScheme = MaterialTheme.colorScheme
            val borderColor = colorScheme.onBackground
            val salesViewModel by remember { mutableStateOf(getKoin().get<SaleViewModel>()) }
            val saleModalUiState = salesViewModel.uiState.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            val navigator = LocalNavigator.currentOrThrow

            Row(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
//                    .background(Color.Red)
                    .padding(5.dp)
            ) {
                // First column
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.23f)
                        .fillMaxHeight()
//                        .background(Color.Green)
                        .border(width = 1.dp, color = borderColor, RoundedCornerShape(12.dp))

                ) {
                    Text("Lista de productos", modifier = Modifier.padding(10.dp))
                    HorizontalDivider()

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        InputField(
                            inputValue = saleModalUiState.value.searchProductsValue,
                            label = "Pesquisar producto",
                            onValueChanged = { searchValue -> salesViewModel.filterProducts(searchValue) }
                        )
                        Spacer(Modifier.height(10.dp))
//                        repeat(5) {
                        LazyColumn {
                            items(saleModalUiState.value.products.filter { it.name.contains(saleModalUiState.value.searchProductsValue, ignoreCase = true)}) { product ->
                                Row(
                                    modifier = Modifier
                                        .padding(top = 2.dp, end = 9.dp, bottom = 2.dp, start = 9.dp)
                                        .background(borderColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                        .fillMaxWidth()
                                        .padding(start = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(product.name)
                                    IconButton(
                                        onClick = { salesViewModel.addProductToCard(product) }
                                    ) {
                                        Icon(painterResource(Res.drawable.arrow_forward), "Add product")
                                    }
                                }
                            }
                        }
                        }

                }

                // Second column (in center)
                Column(
                    modifier = Modifier
                        .padding(start = 6.dp, end = 6.dp)
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight()
//                        .background(Color.Blue, RoundedCornerShape(12.dp))
                        .border(width = 1.dp, color = borderColor, RoundedCornerShape(12.dp))

                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
//                            .background(Color.Cyan)
                            .background(Color.Transparent, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(top = 10.dp, bottom = 10.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            SellTableAddedProTitle("Nome")
                            SellTableAddedProTitle("Qtd")
                            SellTableAddedProTitle("Custo")
                            SellTableAddedProTitle("Preço")
                            SellTableAddedProTitle("Sub-total")
                            SellTableAddedProTitle("Qtd. Dispo.")
                            SellTableAddedProTitle("Ações")
                        }
                        LazyColumn {
                            items(saleModalUiState.value.cardProducts) {
                                with (it) {
                                    SellTableAddedProItem(
                                        name = product.name,
                                        qtd = product.stock,
                                        cost = product.cost,
                                        price = product.price,
                                        subTotal = subTotal,
                                        availableQtd = productsOnCard,
                                        onIncreaseAction = { salesViewModel.changeBuyingProductQuantity(
                                            ProductQuantityAction.Increase, product.id) },
                                        onDecreaseAction = { salesViewModel.changeBuyingProductQuantity(
                                            ProductQuantityAction.Decrease, product.id) }
                                    )
                                }
                            }
                        }
                    }
                    // Descont and received value
                    Row(
                        modifier = Modifier
                            .padding(bottom = 12.dp)
//                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InputField(
                            inputValue = saleModalUiState.value.discount.toString(),
                            label = "Desconto",
                            modifier = Modifier.width(200.dp),
                            onValueChanged = { salesViewModel.fillFormFields(discont = it.toDouble()) },
                            keyboardType = KeyboardType.Decimal
                        )

                        InputField(
                            inputValue = saleModalUiState.value.receivedAmount.toString(),
                            label = "Valor recebido",
                            modifier = Modifier.width(200.dp),
                            onValueChanged = { salesViewModel.fillFormFields(receivedValueFromBuyer = it.toDouble()) },
                            keyboardType = KeyboardType.Decimal
                        )
                    }
                }

                // Third column (in right side)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(width = 1.dp, color = borderColor, RoundedCornerShape(12.dp))
                ) {
                    Column(
                        Modifier.weight(1f)
                    ) {
                        Text("Resumo e Pagamento", modifier = Modifier.padding(10.dp))
                        HorizontalDivider()

                        FormColumn(modifier = Modifier.padding(top = 15.dp)) {
                            DropDown(
                                label = "Cliente",
                                text = saleModalUiState.value.clientName,
                                onDismiss = {  },
                                onDropdownClicked = {}
                            ) {
                                repeat(3) {
                                    DropdownMenuItem(
                                        text = { Text("$it") },
                                        onClick = {
                                            salesViewModel.fillFormFields(
                                                clientId = 1,
                                                clientName = "unknown"
                                            )
                                        }
                                    )
                                }
                            }

                            DropDown(
                                label = "Metodo de pagamento",
                                text = saleModalUiState.value.paymentMethod,
                                onDismiss = {},
                                onDropdownClicked = {}
                            ) {
                                listOf("Dinheiro").forEach {
                                    DropdownMenuItem(
                                        text = { Text(it) },
                                        onClick = {
                                            salesViewModel.fillFormFields(
                                                paymentMethod = it
                                            )
                                        }
                                    )
                                }
                            }

                            HorizontalDivider()

                            Column {
                                SumirizeRow("Sub-total do pedido", "${saleModalUiState.value.saleSubTotal} MT")
                                SumirizeRow("Desconto", "${saleModalUiState.value.discount} MT")
                                SumirizeRow("Troco", composableSecond = {
                                    Text(
                                        text = "${saleModalUiState.value.buyerCharge} MT",
                                        color = Color(0xFFE21111),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                })
                            }

                            HorizontalDivider()

                            SumirizeRow("Total do Pedido", composableSecond = {
                                Text(
                                    text = "${saleModalUiState.value.saleTotal} MT",
                                    color = Color(0xFF15D542),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            })

                            HorizontalDivider()
                        }
                    }

                    Row(
                        modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        NormalOutlineButton(text = "Fechar", onClick = { navigator.pop() } )
                        NormalButton(
                            enabled = !saleModalUiState.value.cardProducts.isEmpty(),
                            text = "Submeter",
                            onClick = { salesViewModel.onSubmitSaleForm() }
                        )
                    }

                    if (!saleModalUiState.value.snackBarText.isBlank()) {
                        showSnackbar(
                            scope = coroutineScope,
                            snackbarHostState = snackbarHostState,
                            message = saleModalUiState.value.snackBarText
                        )
                    }
                }
            }

        }
    }
}


@Composable
fun SellTableAddedProTitle(text: String) {
    Text(text = text, fontWeight = FontWeight.SemiBold)
}

@Composable
fun SellTableAddedProItem(
    name: String,
    qtd: Int,
    cost: Double,
    price: Double,
    subTotal: Double,
    availableQtd: Int,
    onIncreaseAction: () -> Unit,
    onDecreaseAction: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .padding(start = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = name)
        Text(text = "$qtd")
        Text(text = "$cost")
        Text(text = "$price")
        Text(text = "$subTotal")
        Text(text = "$availableQtd")
        Row(
            modifier = Modifier
                .padding(top = 5.dp, end = 5.dp)
                .background(colorScheme.background.copy(red = 0.85f, green = 0.85f, blue = 0.85f), RoundedCornerShape(50))
        ) {
            IconButton( onClick = onIncreaseAction) {
                Icon(
                    painterResource(Res.drawable.add),
                    "add quantity",
                    tint = colorScheme.background
                )
            }

            IconButton(onClick = onDecreaseAction) {
                Icon(
                    painterResource(Res.drawable.remove),
                    "remove quantity",
                    tint = colorScheme.background
                )
            }
        }
    }
}

@Composable
fun SumirizeRow(
    first: String,
    second: String? = null,
    composableSecond: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = first)
        second?.let {
            Text(text = it)
        } ?: run {
            composableSecond?.invoke()
        }
    }
}