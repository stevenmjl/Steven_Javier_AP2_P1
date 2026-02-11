package edu.ucne.steven_javier_ap2_p1.data.local.dao

import androidx.room.Dao
import androidx.room.Upsert

@Dao
interface BorrameDao {
    @Upsert
    suspend fun upsert(borrame: BorrameEntity)

}