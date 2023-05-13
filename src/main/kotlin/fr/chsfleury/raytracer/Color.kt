package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.eq
import java.io.Writer
import kotlin.math.roundToInt

data class Color (
    val r: Double,
    val g: Double,
    val b: Double
) {
    constructor(r: Number, g: Number, b: Number) : this(r.toDouble(), g.toDouble(), b.toDouble())

    infix fun eq(other: Color): Boolean {
        return r eq other.r
                && g eq other.g
                && b eq other.b
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
}