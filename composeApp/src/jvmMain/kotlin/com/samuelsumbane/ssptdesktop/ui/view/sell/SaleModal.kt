package com.samuelsumbane.ssptdesktop.ui.view.sell

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.samuelsumbane.ssptdesktop.ui.components.DropDown
import com.samuelsumbane.ssptdesktop.ui.components.FormColumn
import com.samuelsumbane.ssptdesktop.ui.components.InputField
import com.samuelsumbane.ssptdesktop.ui.components.NormalButton
import com.samuelsumbane.ssptdesktop.ui.components.NormalOutlineButton
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.add
import ssptdesktop.composeapp.generated.resources.arrow_forward
import ssptdesktop.composeapp.generated.resources.more_horiz
import ssptdesktop.composeapp.generated.resources.more_vert
import ssptdesktop.composeapp.generated.resources.remove

class SaleModalScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("Pagina de vendas".uppercase()) })
            }
        ) {
            val colorScheme = MaterialTheme.colorScheme
            val borderColor = colorScheme.onBackground
            
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
                            inputValue = "",
                            label = "Pesquisar producto",
                            onValueChanged = {}
                        )
                        Spacer(Modifier.height(10.dp))
                        repeat(5) {
                            Row(
                                modifier = Modifier
                                    .padding(top = 2.dp, end = 9.dp, bottom = 2.dp, start = 9.dp)
                                    .background(borderColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                    .fillMaxWidth()
                                    .padding(start = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Pro $it")
                                IconButton(
                                    {

                                    }
                                ) {
                                    Icon(painterResource(Res.drawable.arrow_forward), "Add product")
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
                            items(5) {
                                SellTableAddedProItem(
                                    name = "product $it",
                                    qtd = 3,
                                    cost = 120.0 + it,
                                    price = 200.0 + it,
                                    subTotal = 300.0 + it,
                                    availableQtd = 4,
                                )
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
                            inputValue = "",
                            label = "Desconto",
                            modifier = Modifier.width(200.dp),
                            onValueChanged = {},
                            keyboardType = KeyboardType.Decimal
                        )

                        InputField(
                            inputValue = "",
                            label = "Valor recebido",
                            modifier = Modifier.width(200.dp),
                            onValueChanged = {},
                            keyboardType = KeyboardType.Decimal
                        )

                    }
                }

                // Third column (in right side)
                Column(
                    modifier = Modifier
                        .weight(1f)
//                        .fillMaxWidth(0.3f)
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
                                "_",
                                onDismiss = {},
                                onDropdownClicked = {}
                            ) {
                                repeat(3) {
                                    DropdownMenuItem(
                                        text = {
                                            Text("Client $it")
                                        },
                                        onClick = {}
                                    )
                                }
                            }

                            DropDown(
                                label = "Metodo de pagamento",
                                "_",
                                onDismiss = {},
                                onDropdownClicked = {}
                            ) {
                                repeat(3) {
                                    DropdownMenuItem(
                                        text = {
                                            Text("Client $it")
                                        },
                                        onClick = {}
                                    )
                                }
                            }

                            HorizontalDivider()

                            Column {
                                SumirizeRow("Sub-total do pedido", "500.00 MT")
                                SumirizeRow("Desconto", "0.00 MT")
                                SumirizeRow("Troco", "0.00 MT")
                            }

                            HorizontalDivider()

                            SumirizeRow("Total do Pedido", "0.00 MT")

                            HorizontalDivider()
                        }
                    }

                    Row(
                        modifier = Modifier.padding(bottom = 12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        NormalOutlineButton(text = "Fechar", onClick = {})
                        NormalButton(text = "Submeter", onClick = {})
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
            IconButton({}) {
                Icon(
                    painterResource(Res.drawable.add),
                    "add quantity",
//                    tint = colorScheme.background
                )
            }

            IconButton(
                onClick = {}
            ) {
                Icon(
                    painterResource(Res.drawable.remove),
                    "remove quantity",
//                    tint = colorScheme.background
                )
            }
        }
    }
}

@Composable
fun SumirizeRow(first: String, second: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(text = first)
        Text(text = second)
    }
}