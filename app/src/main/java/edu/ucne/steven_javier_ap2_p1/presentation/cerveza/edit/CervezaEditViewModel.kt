package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza
import edu.ucne.steven_javier_ap2_p1.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CervezaEditViewModel @Inject constructor(
    private val getCervezaUseCase: GetCervezaUseCase,
    private val upsertCervezaUseCase: UpsertCervezaUseCase,
    private val deleteCervezaUseCase: DeleteCervezaUseCase,
    private val getCervezaByNombreUseCase: GetCervezaByNombreUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CervezaEditUiState())
    val state: StateFlow<CervezaEditUiState> = _state.asStateFlow()

    fun onEvent(event: CervezaEditUiEvent) {
        when (event) {
            is CervezaEditUiEvent.Load -> onLoad(event.id)
            is CervezaEditUiEvent.NombreChanged -> _state.update {
                it.copy(nombre = event.nombre, nombreError = null)
            }
            is CervezaEditUiEvent.MarcaChanged -> _state.update {
                it.copy(marca = event.marca, marcaError = null)
            }
            is CervezaEditUiEvent.PuntuacionChanged -> _state.update {
                it.copy(puntuacion = event.puntuacion, puntuacionError = null)
            }
            CervezaEditUiEvent.Save -> onSave()
            CervezaEditUiEvent.Delete -> onDelete()
        }
    }

    private fun onLoad(id: Int?) {
        if (id == null || id == 0) {
            _state.update { it.copy(isNew = true, idCerveza = null) }
            return
        }
        viewModelScope.launch {
            val cerveza = getCervezaUseCase(id)
            if (cerveza != null) {
                _state.update {
                    it.copy(
                        isNew = false,
                        idCerveza = cerveza.idcerveza,
                        nombre = cerveza.nombre,
                        marca = cerveza.marca,
                        puntuacion = cerveza.puntuacion.toString()
                    )
                }
            }
        }
    }

    private fun onSave() {
        val nombreLimpio = state.value.nombre.trim()
        val marcaLimpia = state.value.marca.trim()
        val puntuacionStrLimpia = state.value.puntuacion.trim()

        val vNombre = validateNombre(nombreLimpio)
        val vMarca = validateMarca(marcaLimpia)
        val vPuntuacion = validatePuntuacion(puntuacionStrLimpia)

        if (!vNombre.isValid || !vMarca.isValid || !vPuntuacion.isValid) {
            _state.update {
                it.copy(
                    nombreError = vNombre.error,
                    marcaError = vMarca.error,
                    puntuacionError = vPuntuacion.error
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }

            val cervezaExistente = getCervezaByNombreUseCase(nombreLimpio)

            if (cervezaExistente != null && cervezaExistente.idcerveza != state.value.idCerveza) {
                _state.update {
                    it.copy(
                        isSaving = false,
                        nombreError = "Ya existe una cerveza con este nombre."
                    )
                }
                return@launch
            }

            val cerveza = Cerveza(
                idcerveza = state.value.idCerveza,
                nombre = nombreLimpio,
                marca = marcaLimpia,
                puntuacion = puntuacionStrLimpia.toIntOrNull() ?: 0
            )

            val result = upsertCervezaUseCase(cerveza)
            result.onSuccess { newId ->
                _state.update { it.copy(isSaving = false, saved = true, idCerveza = newId) }
            }.onFailure {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun onDelete() {
        val id = state.value.idCerveza ?: return
        viewModelScope.launch {
            _state.update { it.copy(isDeleting = true) }
            deleteCervezaUseCase(id)
            _state.update { it.copy(isDeleting = false, deleted = true) }
        }
    }
}