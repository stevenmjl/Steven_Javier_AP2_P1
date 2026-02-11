package edu.ucne.steven_javier_ap2_p1.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "borrame")
class BorrameEntity {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}