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
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.ui.ClientsPage
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import kotlin.collections.listOf

@Composable
fun SideBar(
    clientViewModel: ClientViewModel
) {
    NavigationRail(
        modifier = Modifier
            .fillMaxHeight()
            .width(80.dp)
//                .background(Color.Magenta),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
//                    .background(Color.Green),
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

                val pageNames by remember { mutableStateOf(
                    listOf(
                        PageName.HOME.itsName,
                        PageName.CLIENTS.itsName,
                        PageName.PRODUCTS.itsName
                    )
                )}

                pageNames.forEach {
                    Column(
                        modifier = Modifier
                            .clickable {
                                when (it) {
//                                    PageName.HOME.itsName ->
                                    PageName.CLIENTS.itsName -> ClientsScreen(clientViewModel)
//                                    PageName.PRODUCTS.itsName
                                    else -> {}
                                }
                            }
                    ) {
                        Text(it, fontSize = 12.sp)
                    }
                }

            }
        }
    }
}

