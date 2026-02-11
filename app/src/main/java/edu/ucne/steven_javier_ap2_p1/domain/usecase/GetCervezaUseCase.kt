package edu.ucne.steven_javier_ap2_p1.domain.usecase

import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza
import edu.ucne.steven_javier_ap2_p1.domain.repository.CervezaRepository
import javax.inject.Inject

class GetCervezaUseCase @Inject constructor(
    private val repository: CervezaRepository
) {
    suspend operator fun invoke(id: Int): Cerveza? {
        return repository.getCerveza(id)
    }
}