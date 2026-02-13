package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
        },
        bottomBar = {
            if (!state.isLoading && state.cervezasFiltradas.isNotEmpty()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total cervezas: ${state.conteoFiltrado}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Promedio puntuación: ${String.format("%.1f", state.promedioPuntuacion)} ★",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Filtros",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.filtroNombre,
                        onValueChange = { onEvent(CervezaListEvent.FiltroNombreChanged(it)) },
                        label = { Text("Nombre") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.filtroMarca,
                        onValueChange = { onEvent(CervezaListEvent.FiltroMarcaChanged(it)) },
                        label = { Text("Marca") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.cervezasFiltradas.isEmpty()) {
                Text(
                    text = "No hay cervezas que coincidan con los filtros",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(state.cervezasFiltradas) { cerveza ->
                        CervezaCard(
                            cerveza = cerveza,
                            onClick = { onEvent(CervezaListEvent.Edit(cerveza.idcerveza ?: 0)) },
                            onEdit = { onEvent(CervezaListEvent.Edit(cerveza.idcerveza ?: 0)) },
                            onDelete = { onEvent(CervezaListEvent.Delete(cerveza.idcerveza ?: 0)) }
                        )
                    }
                    item { Spacer(Modifier.height(16.dp)) }
                }
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
                ),
                cervezasFiltradas = listOf(
                    Cerveza(1, "Presidente", "Cervecería Nacional Dominicana", 5),
                    Cerveza(2, "Corona Extra", "Grupo Modelo", 4)
                ),
                conteoFiltrado = 2,
                promedioPuntuacion = 4.5
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