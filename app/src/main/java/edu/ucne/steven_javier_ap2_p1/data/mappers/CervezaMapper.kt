package edu.ucne.steven_javier_ap2_p1.data.mappers

import edu.ucne.steven_javier_ap2_p1.data.local.entities.CervezaEntity
import edu.ucne.steven_javier_ap2_p1.domain.model.Cerveza

fun CervezaEntity.toDomain(): Cerveza {
    return Cerveza(
        idcerveza = this.IdCerveza,
        nombre = this.Nombre,
        marca = this.Marca,
        puntuacion = this.Puntuacion,
    )
}

fun Cerveza.toEntity(): CervezaEntity{
    return CervezaEntity(
        IdCerveza = this.idcerveza ?: 0,
        Nombre = this.nombre,
        Marca = this.marca,
        Puntuacion = this.puntuacion ?: 0,
    )
}