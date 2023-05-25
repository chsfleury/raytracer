package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.floor

data class GradientPattern(
    val colorA: Color,
    val colorB: Color,
    override val transform: NDArray
): Pattern {

    override fun patternAt(point: Vec4): Color {
        return colorA + (colorB - colorA) * (point.x - floor(point.x))
    }
}
