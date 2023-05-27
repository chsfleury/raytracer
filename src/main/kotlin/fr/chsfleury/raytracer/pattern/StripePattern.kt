package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.floor

data class StripePattern(
    val patternA: Pattern,
    val patternB: Pattern,
    override val transform: NDArray
): Pattern {
    override fun patternAt(point: Vec4): Pattern = if (floor(point.x).toInt() % 2 == 0) {
        patternA
    } else {
        patternB
    }
}
