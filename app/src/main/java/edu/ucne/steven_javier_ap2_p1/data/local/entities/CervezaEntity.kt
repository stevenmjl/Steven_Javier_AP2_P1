package edu.ucne.steven_javier_ap2_p1.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cervezas")
class CervezaEntity (
    @PrimaryKey(autoGenerate = true)
    var IdCerveza: Int = 0,
    var Nombre: String = "",
    var Marca: String = "",
    var Puntuacion: Int = 0
)