package com.samuelsumbane.ssptdesktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import cafe.adriel.voyager.navigator.Navigator
import com.samuelsumbane.ssptdesktop.core.utils.generateId
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.ClientViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.java.KoinJavaComponent.getKoin

import ssptdesktop.composeapp.generated.resources.Res
import kotlin.random.Random

//import ssptdesktop.composeapp.generated.resources.compose_multiplatform

@Composable
//@Preview
fun App() {
//    val clientViewModel: ClientViewModel = getKoin().get()
    val clientViewModel by remember { mutableStateOf(getKoin().get<ClientViewModel>()) }
    val allClients by clientViewModel.allClients.collectAsState()

    MaterialTheme {

        Navigator(ClientsScreen(clientViewModel))

//        var showContent by remember { mutableStateOf(false) }
//        Column(
//            modifier = Modifier
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .safeContentPadding()
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Button(
//                onClick = {
//                    val newId = generateId()
//                    clientViewModel.addTheClient(
//                        ClientItem(
//                            id = newId,
//                            name = "Client $newId",
//                            telephone = "Phone $newId"
//                        )
//                    )
//                }
//            ) {
//                Text("Add Client")
//            }
//
//
//            LazyColumn {
//                items(allClients) { client ->
//                    Row(
//                        modifier = Modifier.fillMaxWidth(0.8f),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        with(client) {
//                            Text(name)
//
//                            Button(onClick = {
//                                clientViewModel.editTheClient(
//                                    ClientItem(
//                                        id = id,
//                                        name = "Updated name $id",
//                                        telephone = "Updated phone $id"
//                                    )
//                                )
//                            }) {
//                                Text("Edit")
//                            }
//                        }
//                    }
//                }
//            }
//
//        }
    }
}