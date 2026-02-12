package edu.ucne.steven_javier_ap2_p1.presentation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object CervezaList : Screen()

    @Serializable
    data class CervezaEdit(val id: Int) : Screen()
}