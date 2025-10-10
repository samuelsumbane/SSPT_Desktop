package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.NavigationRail
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.samuelsumbane.ssptdesktop.ClientsScreen
import com.samuelsumbane.ssptdesktop.HomeScreen
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
//import org.jetbrains.compose.resources.painterResource
//import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.adjust_24
import ssptdesktop.composeapp.generated.resources.details
import ssptdesktop.composeapp.generated.resources.home

@Composable
fun SideBar() {
    val colorScheme = MaterialTheme.colorScheme

    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp),
        backgroundColor = colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(Color.Gray, RoundedCornerShape(12.dp)),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val homePainter by mutableStateOf(painterResource(Res.drawable.home))
                val clientPainter by mutableStateOf(painterResource(Res.drawable.adjust_24))
                val productPainter by mutableStateOf(painterResource(Res.drawable.details))

                val pageButtons by remember { mutableStateOf(
                    mapOf(
                        PageName.HOME.itsName to homePainter,
                        PageName.CLIENTS.itsName to clientPainter,
                        PageName.PRODUCTS.itsName to productPainter
                    )
                )}

                for ((text, painter) in pageButtons) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                when (text) {
                                    PageName.HOME.itsName -> HomeScreen()
                                    PageName.CLIENTS.itsName -> ClientsScreen()
//                                    PageName.PRODUCTS.itsName
                                    else -> {}
                                }
                            }
                    ) {
                        Icon(
                            painter, contentDescription = text,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Text(text, fontSize = 12.sp)
                    }
                }

            }
        }
    }
}

