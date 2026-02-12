package edu.ucne.steven_javier_ap2_p1.domain.usecase

import  edu.ucne.steven_javier_ap2_p1.domain.repository.CervezaRepository
import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveCervezasUseCase @Inject constructor(
    private val repository: CervezaRepository
) {
    operator fun invoke(): Flow<List<Cerveza>> = repository.observeCervezas()

}