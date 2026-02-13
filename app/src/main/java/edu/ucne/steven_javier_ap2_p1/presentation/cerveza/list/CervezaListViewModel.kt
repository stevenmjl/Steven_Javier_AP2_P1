package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.steven_javier_ap2_p1.domain.usecase.DeleteCervezaUseCase
import edu.ucne.steven_javier_ap2_p1.domain.usecase.ObserveCervezasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CervezaListViewModel @Inject constructor(
    private val observeCervezasUseCase: ObserveCervezasUseCase,
    private val deleteCervezaUseCase: DeleteCervezaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CervezaListUiState(isLoading = true))
    val state: StateFlow<CervezaListUiState> = _state.asStateFlow()

    init {
        onEvent(CervezaListEvent.Load)
    }

    fun onEvent(event: CervezaListEvent) {
        when (event) {
            CervezaListEvent.Load -> observe()
            is CervezaListEvent.Delete -> onDelete(event.id)
            CervezaListEvent.CreateNew -> _state.update { it.copy(navigateToCreate = true) }
            is CervezaListEvent.Edit -> _state.update { it.copy(navigateToEditId = event.id) }
            is CervezaListEvent.ShowMessage -> _state.update { it.copy(message = event.message) }

            is CervezaListEvent.FiltroNombreChanged -> {
                _state.update { it.copy(filtroNombre = event.texto) }
                aplicarFiltros()
            }
            is CervezaListEvent.FiltroMarcaChanged -> {
                _state.update { it.copy(filtroMarca = event.texto) }
                aplicarFiltros()
            }
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeCervezasUseCase().collectLatest { listaCompleta ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        cervezas = listaCompleta,
                        message = null
                    )
                }
                aplicarFiltros()
            }
        }
    }

    private fun aplicarFiltros() {
        val listaCompleta = _state.value.cervezas
        val filtroNombre = _state.value.filtroNombre.trim().lowercase()
        val filtroMarca = _state.value.filtroMarca.trim().lowercase()

        val listaFiltrada = listaCompleta.filter { cerveza ->
            (filtroNombre.isEmpty() || cerveza.nombre.lowercase().contains(filtroNombre)) &&
                    (filtroMarca.isEmpty() || cerveza.marca.lowercase().contains(filtroMarca))
        }

        val conteo = listaFiltrada.size
        val promedio = if (listaFiltrada.isEmpty()) {
            0.0
        } else {
            listaFiltrada.map { it.puntuacion }.average()
        }

        _state.update {
            it.copy(
                cervezasFiltradas = listaFiltrada,
                conteoFiltrado = conteo,
                promedioPuntuacion = String.format("%.1f", promedio).toDouble()
            )
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            val result = runCatching { deleteCervezaUseCase(id) }

            if (result.isSuccess) {
                onEvent(CervezaListEvent.ShowMessage("Cerveza eliminada"))
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Error al eliminar la cerveza"
                onEvent(CervezaListEvent.ShowMessage(errorMsg))
            }
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}