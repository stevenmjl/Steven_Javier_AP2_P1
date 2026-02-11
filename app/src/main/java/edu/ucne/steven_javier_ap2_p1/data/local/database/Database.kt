package edu.ucne.steven_javier_ap2_p1.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.steven_javier_ap2_p1.data.local.entities.CervezaEntity
import edu.ucne.steven_javier_ap2_p1.data.local.dao.CervezaDao

@Database(entities = [CervezaEntity::class], version = 1, exportSchema = false)

abstract class Database : RoomDatabase() {
    abstract val cervezaDao: CervezaDao

}