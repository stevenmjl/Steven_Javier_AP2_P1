package edu.ucne.steven_javier_ap2_p1.presentation.cerveza.list

sealed interface CervezaListEvent {
    data object Load : CervezaListEvent
    data class Delete(val id: Int) : CervezaListEvent
    data object CreateNew : CervezaListEvent
    data class Edit(val id: Int) : CervezaListEvent
    data class ShowMessage(val message: String) : CervezaListEvent
}