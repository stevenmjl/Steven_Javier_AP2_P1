package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.edit

sealed interface CervezaEditUiEvent {
    data class Load(val id: Int?) : CervezaEditUiEvent
    data class NombreChanged(val nombre: String) : CervezaEditUiEvent
    data class MarcaChanged(val marca: String) : CervezaEditUiEvent
    data class PuntuacionChanged(val puntuacion: String) : CervezaEditUiEvent
    data object Save : CervezaEditUiEvent
    data object Delete : CervezaEditUiEvent
}