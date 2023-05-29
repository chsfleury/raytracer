package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.eq

data class Color (
    val r: Double,
    val g: Double,
    val b: Double
) {
    companion object {
        val BLACK = Color(0.0, 0.0, 0.0)
        val WHITE = Color(1.0, 1.0, 1.0)
    }

    infix fun eq(other: Color): Boolean {
        return r eq other.r
                && g eq other.g
                && b eq other.b
    }

    fun eq(other: Color, epsilon: Double): Boolean {
        return r.eq(other.r, epsilon)
                && g.eq(other.g, epsilon)
                && b.eq(other.b, epsilon)
    }

    operator fun plus(other: Color) = Color(
        r + other.r,
        g + other.g,
        b + other.b
    )

    operator fun minus(other: Color) = Color(
        r - other.r,
        g - other.g,
        b - other.b
    )

    operator fun times(scalar: Double) = Color(
        r * scalar,
        g * scalar,
        b * scalar
    )

    operator fun times(scalar: Number) = times(scalar.toDouble())

    operator fun times(other: Color) = Color(
        r * other.r,
        g * other.g,
        b * other.b
    )

    fun averageWith(other: Color) = Color(
        (r + other.r) / 2,
        (g + other.g) / 2,
        (b + other.b) / 2
    )
}