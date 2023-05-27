package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4

data class BlendedPattern(
    val patternA: Pattern,
    val patternB: Pattern,
    override val transform: NDArray
): Pattern {

    override fun colorAt(point: Vec4): Color {
        val colorA = patternA.colorAt(point)
        val colorB = patternB.colorAt(point)
        return colorA.averageWith(colorB)
    }
}
