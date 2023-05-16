package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.linalg.Vec4

data class Ray(
    val origin: Vec4,
    val direction: Vec4
) {
    init {
        if (!origin.isPoint()) {
            error("origin must be a point")
        }

        if (!direction.isVector()) {
            error("direction must be a vector")
        }
    }

    fun position(t: Double): Vec4 = origin + direction * t
    fun position(t: Number): Vec4 = position(t.toDouble())
}
