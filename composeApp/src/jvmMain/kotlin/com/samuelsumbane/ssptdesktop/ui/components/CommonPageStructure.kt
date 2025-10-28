package com.samuelsumbane.ssptdesktop.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.NavigationRail
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
//import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import com.samuelsumbane.ssptdesktop.ui.utils.PageName
import org.jetbrains.compose.resources.painterResource
import ssptdesktop.composeapp.generated.resources.Res
import ssptdesktop.composeapp.generated.resources.arrow_back

//import org.jetbrains.skia.paragraph.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonPageStructure(
    navigator: Navigator,
    pageTitle: String = "Clientes",
    activePage: String = "",
    enableScroll: Boolean = true,
    topBarActions: @Composable RowScope.() -> Unit = {},
    onPerfomeSnackbarHost: ((SnackbarHostState) -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .possiblyVerticalScroll(enableScroll, scrollState)
            ) {
                content()
                onPerfomeSnackbarHost?.invoke(snackbarHostState)
            }

        }
    }

}