package edu.ucne.steven_javier_ap2_p1.domain.repository

import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza
import kotlinx.coroutines.flow.Flow

interface CervezaRepository {

    fun observeCervezas(): Flow<List<Cerveza>>
    suspend fun getCerveza(id: Int): Cerveza?
    suspend fun upsert(cerveza: Cerveza): Int
    suspend fun delete(id: Int)
    suspend fun getByNombre(nombre: String): Cerveza?
}