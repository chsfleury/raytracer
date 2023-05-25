package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.floor

data class StripePattern(
    val colorA: Color,
    val colorB: Color
): Pattern {
    override fun colorAt(point: Vec4): Color = if (floor(point.x).toInt() % 2 == 0) {
        colorA
    } else {
        colorB
    }

    fun stripeAt(point: Vec4) = colorAt(point)
}
