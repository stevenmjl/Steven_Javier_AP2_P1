package edu.ucne.steven_javier_ap2_p1.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [BorrameEntity::class], version = 1, exportSchema = false)

abstract class Database : RoomDatabase() {

}