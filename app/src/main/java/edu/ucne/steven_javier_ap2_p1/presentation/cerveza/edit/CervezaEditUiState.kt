package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.edit

data class CervezaEditUiState(
    val idCerveza: Int? = null,
    val nombre: String = "",
    val marca: String = "",
    val puntuacion: String = "",
    val idCervezaError: String? = null,
    val nombreError: String? = null,
    val marcaError: String? = null,
    val puntuacionError: String? = null,
    val isSaving: Boolean = false,
    val isDeleting: Boolean = false,
    val isNew: Boolean = true,
    val saved: Boolean = false,
    val deleted: Boolean = false
)