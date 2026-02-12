package edu.ucne.steven_javier_ap2_p1.domain.usecase

data class ValidationResult(
    val isValid: Boolean,
    val error: String? = null
)

fun validateNombre(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "El nombre de la cerveza es requerido")
    if (value.trim().length < 3) return ValidationResult(false, "El nombre debe tener al menos 3 caracteres")
    return ValidationResult(true)
}

fun validateMarca(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "La marca es requerida")
    if (value.trim().length < 2) return ValidationResult(false, "La marca debe tener al menos 2 caracteres")
    return ValidationResult(true)
}

fun validatePuntuacion(value: String): ValidationResult {
    if (value.isBlank()) return ValidationResult(false, "La puntuación es requerida")

    val number = value.toIntOrNull()
        ?: return ValidationResult(false, "Debe ser un número entero")

    if (number < 1 || number > 5) {
        return ValidationResult(false, "La puntuación debe estar entre 1 y 5")
    }

    return ValidationResult(true)
}