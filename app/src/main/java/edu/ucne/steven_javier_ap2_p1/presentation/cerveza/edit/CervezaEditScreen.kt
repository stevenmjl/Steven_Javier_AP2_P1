package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CervezaEditScreen(
    cervezaId: Int?,
    viewModel: CervezaEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(cervezaId) {
        viewModel.onEvent(CervezaEditUiEvent.Load(cervezaId ?: 0))
    }

    LaunchedEffect(state.saved) {
        if (state.saved) {
            onNavigateBack()
        }
    }

    if (state.deleted) {
        onNavigateBack()
    }

    CervezaEditBody(
        state = state,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        onDeleteClick = {
            viewModel.onEvent(CervezaEditUiEvent.Delete)
        }
    )
}

@Composable
private fun CervezaEditBody(
    state: CervezaEditUiState,
    onEvent: (CervezaEditUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (state.isNew) "Nueva cerveza" else "Editar cerveza",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primaryContainer)

            OutlinedTextField(
                value = state.nombre,
                onValueChange = { onEvent(CervezaEditUiEvent.NombreChanged(it)) },
                label = { Text("Nombre de la cerveza") },
                isError = state.nombreError != null,
                supportingText = state.nombreError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.marca,
                onValueChange = { onEvent(CervezaEditUiEvent.MarcaChanged(it)) },
                label = { Text("Marca") },
                isError = state.marcaError != null,
                supportingText = state.marcaError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.puntuacion,
                onValueChange = { onEvent(CervezaEditUiEvent.PuntuacionChanged(it)) },
                label = { Text("Puntuación (1-5)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = state.puntuacionError != null,
                supportingText = state.puntuacionError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onEvent(CervezaEditUiEvent.Save) },
                    enabled = !state.isSaving,
                    modifier = Modifier.weight(1f)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar")
                    }
                }

                if (!state.isNew) {
                    OutlinedButton(
                        onClick = onDeleteClick,
                        enabled = !state.isDeleting,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                }
            }

            TextButton(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CervezaEditNuevaPreview() {
    MaterialTheme {
        CervezaEditBody(
            state = CervezaEditUiState(isNew = true),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CervezaEditEditarPreview() {
    MaterialTheme {
        CervezaEditBody(
            state = CervezaEditUiState(
                isNew = false,
                idCerveza = 1,
                nombre = "Presidente",
                marca = "Cervecería Nacional",
                puntuacion = "4"
            ),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CervezaEditConErroresPreview() {
    MaterialTheme {
        CervezaEditBody(
            state = CervezaEditUiState(
                isNew = false,
                nombre = "",
                nombreError = "El nombre de la cerveza es requerido",
                marca = "XX",
                marcaError = "La marca debe tener al menos 2 caracteres",
                puntuacion = "6",
                puntuacionError = "La puntuación debe estar entre 1 y 5"
            ),
            onEvent = {},
            onNavigateBack = {},
            onDeleteClick = {}
        )
    }
}