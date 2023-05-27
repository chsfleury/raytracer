package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.floor
import kotlin.math.sqrt

data class RadialGradientPattern(
    val patternA: Pattern,
    val patternB: Pattern,
    override val transform: NDArray
): Pattern {
    override fun colorAt(point: Vec4): Color {
        val a = sqrt(point.x * point.x + point.z * point.z)
        val colorA = patternA.colorAt(point)
        val colorB = patternB.colorAt(point)
        return colorA + (colorB - colorA) * (a - floor(a))
    }
}
