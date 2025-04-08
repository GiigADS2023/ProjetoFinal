package com.example.projetofinal.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projetofinal.R
import com.example.projetofinal.data.Travel
import com.example.projetofinal.viewmodel.TravelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: TravelViewModel, userId: Int) {
    var travels by remember { mutableStateOf<List<Travel>>(emptyList()) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Verifica se o ID é válido antes de buscar
    LaunchedEffect(userId) {
        if (userId > 0) {
            travels = viewModel.getTravelsByUser(userId)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {

            Text("Minhas Viagens", style = MaterialTheme.typography.headlineMedium)

            if (travels.isEmpty()) {
                Text("Nenhuma viagem cadastrada.", modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)) {

                    items(travels, key = { it.id }) { travel ->
                        val dismissState = rememberSwipeToDismissBoxState()

                        SwipeToDismissBox(
                            state = dismissState,
                            enableDismissFromStartToEnd = true,
                            enableDismissFromEndToStart = false,
                            backgroundContent = {
                                if (dismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 8.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Deletar"
                                        )
                                    }
                                }
                            },
                            content = {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        val imageRes = if (travel.travelType == "Lazer") {
                                            R.drawable.leisure_image
                                        } else {
                                            R.drawable.business_image
                                        }

                                        Image(
                                            painter = painterResource(id = imageRes),
                                            contentDescription = "Imagem do tipo de viagem",
                                            modifier = Modifier
                                                .size(48.dp)
                                                .padding(end = 8.dp)
                                        )

                                        Column {
                                            Text("Destino: ${travel.destination}", style = MaterialTheme.typography.bodyLarge)
                                            Text("Período: ${travel.startDate} - ${travel.endDate}", style = MaterialTheme.typography.bodyMedium)
                                            Text("Orçamento: R$${travel.budget}", style = MaterialTheme.typography.bodyMedium)
                                        }
                                    }
                                }
                            }
                        )

                        // Lógica de exclusão com Snackbar de confirmação
                        LaunchedEffect(dismissState.currentValue) {
                            if (dismissState.currentValue == SwipeToDismissBoxValue.StartToEnd) {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Excluir viagem?",
                                    actionLabel = "Sim",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short
                                )

                                if (result == SnackbarResult.ActionPerformed) {
                                    viewModel.deleteTravel(travel.id)
                                    travels = travels.filter { it.id != travel.id }
                                } else {
                                    dismissState.snapTo(SwipeToDismissBoxValue.Settled)
                                }
                            }
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
