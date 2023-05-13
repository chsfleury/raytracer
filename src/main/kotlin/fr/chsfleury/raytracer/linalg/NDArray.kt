package fr.chsfleury.raytracer.linalg

import fr.chsfleury.raytracer.Doubles.neq

open class NDArray(
    val width: Int,
    val height: Int,
    internal val data: DoubleArray = DoubleArray(width * height) { 0.0 }
) {
    init {
        if (data.size != width * height) {
            error("inconsistent data")
        }
    }

    constructor(width: Int, height: Int, init: (Int) -> Double) : this(
        width, height, DoubleArray(width * height, init)
    )

    constructor(width: Int, height: Int, vararg values: Number) : this(
        width,
        height,
        values.map(Number::toDouble).toDoubleArray()
    )

    companion object {
        fun of2x2(vararg values: Number) = NDArray(2, 2, *values)
        fun of3x3(vararg values: Number) = NDArray(3, 3, *values)
        fun of4x4(vararg values: Number) = NDArray(4, 4, *values)
    }

    operator fun get(y: Int, x: Int): Double = data[y * width + x]
    operator fun set(y: Int, x: Int, v: Double) {
        data[y * width + x] = v
    }

    infix fun eq(other: NDArray): Boolean {
        if (width != other.width || height != other.height) {
            return false
        }

        for (i in 0 until (width * height)) {
            if (data[i] neq other.data[i]) {
                return false
            }
        }

        return true
    }

    operator fun plus(other: NDArray): NDArray {
        checkWidthAndHeightAreEquals(other)

        return NDArray(width, height) { i ->
            data[i] + other.data[i]
        }
    }

    operator fun minus(other: NDArray): NDArray {
        checkWidthAndHeightAreEquals(other)

        return NDArray(width, height) { i ->
            data[i] - other.data[i]
        }
    }

    operator fun unaryMinus(): NDArray = NDArray(width, height) { i ->
        -data[i]
    }

    operator fun times(scalar: Double): NDArray = NDArray(width, height) { i ->
        data[i] * scalar
    }

    fun times(scalar: Number): NDArray = times(scalar.toDouble())

    operator fun times(other: NDArray): NDArray {
        if (width != other.height) {
            error("invalid operation")
        }
        val result = NDArray(other.width, height)
        for (y in 0 until height) {
            for (x in 0 until other.width) {
                result[y, x] = (0 until width).asSequence()
                    .map { this[y, it] * other[it, x] }
                    .sum()
            }
        }
        return result
    }

    operator fun times(other: Vec4): Vec4 = Vec4(times(other.ndArray))

    operator fun div(scalar: Double): NDArray = NDArray(width, height) { i ->
        data[i] / scalar
    }

    fun div(scalar: Number): NDArray = div(scalar.toDouble())

    private fun checkWidthAndHeightAreEquals(other: NDArray) {
        if (width != other.width || height != other.height) {
            error("invalid width or height: ${width}x${height} != ${other.width}x${other.height}")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NDArray

        if (width != other.width) return false
        if (height != other.height) return false
        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + data.contentHashCode()
        return result
    }
}
