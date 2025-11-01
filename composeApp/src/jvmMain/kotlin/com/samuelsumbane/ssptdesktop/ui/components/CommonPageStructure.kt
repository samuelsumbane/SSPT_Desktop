package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
//import androidx.compose.material.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.arrow_back


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonPageStructure(
    navigator: Navigator,
    pageTitle: String = "Clientes",
    activePage: String = "",
    topBarActions: @Composable RowScope.() -> Unit = {},
    onPerfomeSnackbarHost: ((SnackbarHostState) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    ShowContentIfConnectedToServer {
        Row(modifier = Modifier) {
            SideBar(navigator, activePage)
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = pageTitle.uppercase(), fontWeight = FontWeight.Bold)
                        },
                        navigationIcon = {
                            if (activePage != PageName.HOME.itsName) {
                                IconButton(
                                    onClick = { navigator.pop() }
                                ) {
                                    Icon(painterResource(Res.drawable.arrow_back), contentDescription = "Go back")
                                }
                            }
                        },
                        actions = { topBarActions() },
                    )
                },
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                }
            ) {

                Column(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                ) {
                    content()
                    onPerfomeSnackbarHost?.invoke(snackbarHostState)
                }

            }
        }
    }
}