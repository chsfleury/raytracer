package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.abs

data class CheckersPattern(
    val colorA: Color,
    val colorB: Color,
    override val transform: NDArray
): Pattern {
    override fun patternAt(point: Vec4): Color = if ((abs(point.x) + abs(point.y) + abs(point.z)).toInt() % 2 == 0) {
        colorA
    } else {
        colorB
    }
}
