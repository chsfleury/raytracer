package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.floor
import kotlin.math.sqrt

data class RadialGradientPattern(
    val colorA: Color,
    val colorB: Color,
    override val transform: NDArray
): Pattern {
    override fun patternAt(point: Vec4): Color {
        val a = sqrt(point.x * point.x + point.z * point.z)
        return colorA + (colorB - colorA) * (a - floor(a))
    }
}
