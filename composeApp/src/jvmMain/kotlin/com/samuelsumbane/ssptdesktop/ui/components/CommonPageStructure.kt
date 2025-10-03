package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.NavigationRail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
//import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.arrow_back

//import org.jetbrains.skia.paragraph.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonPageStructure(
    clientViewModel: ClientViewModel,
    pageTitle: String = "Clientes",
    topBarActions: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        modifier = Modifier
//            .background(Color.Blue)
    ) {
        SideBar(clientViewModel)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = pageTitle.uppercase(), fontWeight = FontWeight.Bold)
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(painterResource(Res.drawable.arrow_back), contentDescription = "Go back")
                        }
                    },
                    actions = { topBarActions() }
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
//                    .background(Color.Gray)
            ) {
                content()
            }
        }
    }

}