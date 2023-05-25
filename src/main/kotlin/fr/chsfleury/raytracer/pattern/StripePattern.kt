package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape
import kotlin.math.floor

data class StripePattern(
    val colorA: Color,
    val colorB: Color,
    override val transform: NDArray
): Pattern {
    override fun colorAt(point: Vec4): Color = if (floor(point.x).toInt() % 2 == 0) {
        colorA
    } else {
        colorB
    }

    override fun colorAtObject(obj: Shape, point: Vec4): Color {
        return colorAt(point)
    }

    fun stripeAt(point: Vec4) = colorAt(point)

    fun stripeAtObject(obj: Shape, point: Vec4): Color {
        val objectPoint = obj.transform.inv * point
        val patternPoint = transform.inv * objectPoint
        return stripeAt(patternPoint)
    }
}
