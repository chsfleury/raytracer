package fr.chsfleury.raytracer.linalg

import fr.chsfleury.raytracer.Doubles.eq
import kotlin.math.sqrt

class Vec4(internal val ndArray: NDArray) {
    constructor(data: DoubleArray) : this(NDArray(1, 4, data))
    constructor(x: Double, y: Double, z: Double, w: Double) : this(doubleArrayOf(x, y, z, w))

    companion object {
        const val POINT_W = 1.0
        const val VECTOR_W = 0.0

        fun tuple(x: Double, y: Double, z: Double, w: Double) = Vec4(x, y, z, w)
        fun vector(x: Double, y: Double, z: Double) = Vec4(x, y, z, VECTOR_W)
        fun point(x: Double, y: Double, z: Double) = Vec4(x, y, z, POINT_W)
    }

    val x: Double
        get() = ndArray.data[0]
    val y: Double
        get() = ndArray.data[1]
    val z: Double
        get() = ndArray.data[2]
    val w: Double
        get() = ndArray.data[3]

    fun isPoint() = w eq POINT_W
    fun isVector() = w eq VECTOR_W

    fun toVector() = vector(x, y, z)

    fun magnitude(): Double = sqrt(x * x + y * y + z * z + w * w)
    fun normalize() = div(magnitude())

    infix fun cross(other: Vec4): Vec4 = vector(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    infix fun dot(other: Vec4): Double = x * other.x + y * other.y + z * other.z + w * other.w

    operator fun get(y: Int, x: Int): Double = ndArray[x, y]

    operator fun set(y: Int, x: Int, v: Double) {
        ndArray[x, y] = v
    }

    operator fun unaryMinus() = Vec4(-ndArray)

    operator fun times(scalar: Double): Vec4 = Vec4(ndArray * scalar)
    operator fun times(scalar: Number): Vec4 = times(scalar.toDouble())
    operator fun times(other: Vec4): Vec4 = Vec4(ndArray.times(other.ndArray))

    operator fun div(scalar: Double): Vec4 = Vec4(ndArray / scalar)
    operator fun div(scalar: Number): Vec4 = div(scalar.toDouble())
    operator fun plus(other: Vec4): Vec4 = Vec4(ndArray + other.ndArray)
    operator fun minus(other: Vec4): Vec4 = Vec4(ndArray - other.ndArray)

    infix fun eq(other: Vec4): Boolean = ndArray eq other.ndArray

    override fun toString() = "($x, $y, $z, $w)"

}