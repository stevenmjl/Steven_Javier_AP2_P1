package edu.ucne.steven_javier_ap2_p1.data.repository

import edu.ucne.steven_javier_ap2_p1.data.local.dao.CervezaDao
import edu.ucne.steven_javier_ap2_p1.data.mappers.toDomain
import edu.ucne.steven_javier_ap2_p1.data.mappers.toEntity
import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza
import edu.ucne.steven_javier_ap2_p1.domain.repository.CervezaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class cervezaRepositoryImpl @Inject constructor(
    private val dao: CervezaDao
) : CervezaRepository {
    override fun observeCervezas(): Flow<List<Cerveza>> = dao.observeAll().map { list ->
        list.map { it.toDomain() }
    }

    override suspend fun getCerveza(id: Int): Cerveza? = dao.getById(id)?.toDomain()

    override suspend fun upsert(cerveza: Cerveza): Int {
        dao.upsert(cerveza.toEntity())
        return cerveza.idcerveza ?: 0
    }

    override suspend fun getByNombre(nombre: String): Cerveza? {
        val entity = dao.getByNombre(nombre)
        return entity?.toDomain()
    }

    override suspend fun delete(id: Int) {
        dao.deleteById(id)
    }
}