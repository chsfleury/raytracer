package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4

data class TestPattern(
    override val transform: NDArray
): Pattern {
    override fun colorAt(point: Vec4): Color {
        return color(point.x, point.y, point.z)
    }

}
