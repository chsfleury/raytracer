package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.eq
import kotlin.math.sqrt

data class Tuple(
    val x: Double,
    val y: Double,
    val z: Double,
    val w: Double
) {
    constructor(x: Number, y: Number, z: Number, w: Number) : this(
        x.toDouble(),
        y.toDouble(),
        z.toDouble(),
        w.toDouble()
    )

    fun isPoint() = w == POINT_W
    fun isVector() = w == VECTOR_W

    infix fun eq(other: Tuple): Boolean {
        return x eq other.x
                && y eq other.y
                && z eq other.z
                && w eq other.w
    }

    operator fun plus(other: Tuple) = Tuple(
        x + other.x,
        y + other.y,
        z + other.z,
        w + other.w
    )

    operator fun minus(other: Tuple) = Tuple(
        x - other.x,
        y - other.y,
        z - other.z,
        w - other.w
    )

    operator fun unaryMinus() = Tuple(-x, -y, -z, -w)

    operator fun times(scalar: Double) = Tuple(
        x * scalar,
        y * scalar,
        z * scalar,
        w * scalar
    )

    operator fun times(scalar: Number) = times(scalar.toDouble())

    operator fun div(scalar: Double) = Tuple(
        x / scalar,
        y / scalar,
        z / scalar,
        w / scalar
    )

    operator fun div(scalar: Number) = div(scalar.toDouble())

    fun magnitude() = sqrt(x * x + y * y + z * z + w * w)

    fun isUnit() = magnitude() eq 1.0

    fun normalize() = div(magnitude())

    infix fun dot(other: Tuple) = x * other.x + y * other.y + z * other.z + w * other.w

    infix fun cross(other: Tuple) = vector(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    override fun toString() = "($x, $y, $z, $w)"

    companion object {
        const val POINT_W = 1.0
        const val VECTOR_W = 0.0

        fun point(x: Double, y: Double, z: Double) = Tuple(x, y, z, POINT_W)
        fun point(x: Number, y: Number, z: Number) = Tuple(x.toDouble(), y.toDouble(), z.toDouble(), POINT_W)

        fun vector(x: Double, y: Double, z: Double) = Tuple(x, y, z, VECTOR_W)
        fun vector(x: Number, y: Number, z: Number) = Tuple(x.toDouble(), y.toDouble(), z.toDouble(), VECTOR_W)
    }
}