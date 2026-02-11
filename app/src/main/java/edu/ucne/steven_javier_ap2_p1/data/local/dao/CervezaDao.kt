package edu.ucne.steven_javier_ap2_p1.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.steven_javier_ap2_p1.data.local.entities.CervezaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CervezaDao {

    @Query("SELECT * FROM cervezas ORDER BY Idcerveza DESC")
    fun observeAll(): Flow<List<CervezaEntity>>

    @Query("SELECT * FROM cervezas WHERE Idcerveza = :id")
    suspend fun getById(id: Int): CervezaEntity?

    @Upsert
    suspend fun upsert(entity: CervezaEntity)

    @Query("DELETE FROM cervezas WHERE Idcerveza = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM cervezas WHERE nombre = :nombre COLLATE NOCASE LIMIT 1")
    suspend fun getByNombre(nombre: String): CervezaEntity?
}