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
        }
    }

    private fun observe() {
        viewModelScope.launch {
            observeCervezasUseCase().collectLatest { list ->
                _state.update { it.copy(isLoading = false, cervezas = list, message = null) }
            }
        }
    }

    private fun onDelete(id: Int) {
        viewModelScope.launch {
            deleteCervezaUseCase(id)
            onEvent(CervezaListEvent.ShowMessage("Cerveza eliminada"))
        }
    }

    fun onNavigationHandled() {
        _state.update { it.copy(navigateToCreate = false, navigateToEditId = null) }
    }
}