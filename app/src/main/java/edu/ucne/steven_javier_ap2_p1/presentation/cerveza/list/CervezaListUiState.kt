package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.list

import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza

data class CervezaListUiState(
    val isLoading: Boolean = false,
    val cervezas: List<Cerveza> = emptyList(),
    val cervezasFiltradas: List<Cerveza> = emptyList(),
    val filtroNombre: String = "",
    val filtroMarca: String = "",
    val conteoFiltrado: Int = 0,
    val promedioPuntuacion: Double = 0.0,
    val message: String? = null,
    val navigateToCreate: Boolean = false,
    val navigateToEditId: Int? = null
)