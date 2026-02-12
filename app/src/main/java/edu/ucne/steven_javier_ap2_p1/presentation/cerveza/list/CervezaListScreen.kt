package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza

@Composable
fun CervezaListScreen(
    viewModel: CervezaListViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDeleteDialog by remember { mutableStateOf(false) }
    var cervezaIdToDelete by remember { mutableIntStateOf(0) }

    if (showDeleteDialog) {
        viewModel.onEvent(CervezaListEvent.Delete(cervezaIdToDelete))
        showDeleteDialog = false
    }

    LaunchedEffect(state.navigateToCreate) {
        if (state.navigateToCreate) {
            onNavigateToCreate()
            viewModel.onNavigationHandled()
        }
    }

    LaunchedEffect(state.navigateToEditId) {
        state.navigateToEditId?.let { id ->
            onNavigateToEdit(id)
            viewModel.onNavigationHandled()
        }
    }

    CervezaListBody(
        state = state,
        onEvent = { event ->
            when (event) {
                is CervezaListEvent.Delete -> {
                    cervezaIdToDelete = event.id
                    showDeleteDialog = true
                }
                else -> viewModel.onEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CervezaListBody(
    state: CervezaListUiState,
    onEvent: (CervezaListEvent) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de cervezas") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(CervezaListEvent.CreateNew) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (state.cervezas.isEmpty() && !state.isLoading) {
                Text(
                    text = "No hay cervezas registradas",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(state.cervezas) { cerveza ->
                    CervezaCard(
                        cerveza = cerveza,
                        onClick = { onEvent(CervezaListEvent.Edit(cerveza.idcerveza ?: 0)) },
                        onEdit = { onEvent(CervezaListEvent.Edit(cerveza.idcerveza ?: 0)) },
                        onDelete = { onEvent(CervezaListEvent.Delete(cerveza.idcerveza ?: 0)) }
                    )
                }
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun CervezaCard(
    cerveza: Cerveza,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cerveza.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = cerveza.marca,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "★ ${cerveza.puntuacion ?: 0.0}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Spacer(Modifier.width(16.dp))

                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Lista con cervezas")
@Composable
private fun CervezaListaConDatos() {
    MaterialTheme {
        CervezaListBody(
            state = CervezaListUiState(
                isLoading = false,
                cervezas = listOf(
                    Cerveza(1, "Presidente", "Cervecería Nacional Dominicana", 5),
                    Cerveza(2, "Corona Extra", "Grupo Modelo", 4),
                    Cerveza(3, "Heineken", "Heineken International", 3)
                )
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Lista vacía")
@Composable
private fun CervezaListaVaciaPreview() {
    MaterialTheme {
        CervezaListBody(
            state = CervezaListUiState(
                isLoading = false,
                cervezas = emptyList(),
                message = null,
                navigateToCreate = false,
                navigateToEditId = null
            ),
            onEvent = {}
        )
    }
}