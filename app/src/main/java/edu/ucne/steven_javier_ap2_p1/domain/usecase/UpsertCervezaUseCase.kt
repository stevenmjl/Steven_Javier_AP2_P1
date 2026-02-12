package edu.ucne.steven_javier_ap2_p1.domain.usecase

import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza
import edu.ucne.steven_javier_ap2_p1.domain.repository.CervezaRepository
import javax.inject.Inject

class UpsertCervezaUseCase @Inject constructor(
    private val repository: CervezaRepository
) {
    suspend operator fun invoke(cerveza: Cerveza): Result<Int> {
        val vNombre = validateNombre(cerveza.nombre)
        if (!vNombre.isValid) return Result.failure(IllegalArgumentException(vNombre.error))

        val vMarca = validateMarca(cerveza.marca)
        if (!vMarca.isValid) return Result.failure(IllegalArgumentException(vMarca.error))

        val vPuntuacion = validatePuntuacion(cerveza.puntuacion.toString())
        if (!vPuntuacion.isValid) return Result.failure(IllegalArgumentException(vPuntuacion.error))

        return runCatching { repository.upsert(cerveza) }
    }
}