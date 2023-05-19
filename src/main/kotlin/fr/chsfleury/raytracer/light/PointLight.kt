package fr.chsfleury.raytracer.light

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.Vec4

data class PointLight(
    val position: Vec4,
    val intensity: Color
) {
    init {
        if (!position.isPoint()) {
            error("position is not a point")
        }
    }
}
