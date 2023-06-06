package fr.chsfleury.raytracer.linalg

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.Doubles.neq
import fr.chsfleury.raytracer.Ray

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

    val inv by lazy(::inverse)
    val trInv by lazy { inv.transpose() }

    constructor(width: Int, height: Int, init: (Int) -> Double) : this(
        width, height, DoubleArray(width * height, init)
    )

    constructor(width: Int, height: Int, vararg values: Number) : this(
        width,
        height,
        values.map(Number::toDouble).toDoubleArray()
    )

    companion object {
        fun fromXY(width: Int, height: Int, init: (Int, Int) -> Double): NDArray = NDArray(width, height, DoubleArray(width * height) {
            val (x, y) = toXY(it, width)
            init(x, y)
        })

        fun of2x2(vararg values: Number) = NDArray(2, 2, *values)
        fun of3x3(vararg values: Number) = NDArray(3, 3, *values)
        fun of4x4(vararg values: Number) = NDArray(4, 4, *values)

        val ID2 = identity(2, true)
        val ID3 = identity(3, true)
        val ID4 = identity(4, true)

        fun identity(n: Int = 4, force: Boolean = false): NDArray {
            if (!force) {
                when (n) {
                    4 -> return ID4
                    3 -> return ID3
                    2 -> return ID2
                }
            }
            return fromXY(n, n) { x, y ->
                if (x == y) {
                    1.0
                } else {
                    0.0
                }
            }
        }

        private fun toXY(i: Int, n: Int) = Pair(i % n, i / n)
    }

    operator fun get(y: Int, x: Int): Double = data[y * width + x]
    operator fun set(y: Int, x: Int, v: Double) {
        data[y * width + x] = v
    }

    infix fun eq(other: NDArray): Boolean = eq(other, EPSILON)

    fun eq(other: NDArray, epsilon: Double): Boolean {
        if (width != other.width || height != other.height) {
            return false
        }

        for (i in 0 until (width * height)) {
            if (data[i].neq((other.data[i]), epsilon)) {
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

    operator fun times(other: Ray): Ray = Ray(
        this * other.origin,
        this * other.direction
    )

    operator fun div(scalar: Double): NDArray = NDArray(width, height) { i ->
        data[i] / scalar
    }

    fun div(scalar: Number): NDArray = div(scalar.toDouble())

    fun transpose(): NDArray = fromXY(height, width) { x, y -> this[x, y] }

    /**
     * | a b |
     * | c d |
     * det = ad - bc
     */
    fun determinant(): Double {
        return if (width == 2 && height == 2) {
            determinant2x2()
        } else {
            determinantLargerMatrix()
        }
    }

    private fun determinant2x2(): Double = data[0] * data[3] - data[1] * data[2]
    private fun determinantLargerMatrix(): Double = (0 until width).asSequence()
        .map { data[it] * cofactor(0, it) }
        .sum()

    fun subMatrix(removedRow: Int, removedCol: Int): NDArray = fromXY(width - 1, height - 1) { x, y ->
        val sourceX = if (x >= removedCol) {
            x + 1
        } else {
            x
        }
        val sourceY = if (y >= removedRow) {
            y + 1
        } else {
            y
        }
        this[sourceY, sourceX]
    }

    fun minor(row: Int, col: Int): Double = subMatrix(row, col).determinant()

    fun cofactor(row: Int, col: Int): Double = if ((row + col) % 2 == 0) {
        minor(row, col)
    } else {
        -minor(row, col)
    }

    fun isInvertible() = determinant() neq 0

    fun inverse(): NDArray {
        val det = determinant()
        return fromXY(width, height) { x, y ->
            cofactor(x, y) / det
        }
    }

    fun rotateX(r: Double) = Transform.xRotation(r) * this
    fun rotateX(r: Number) = rotateX(r.toDouble())

    fun rotateY(r: Double) = Transform.yRotation(r) * this
    fun rotateY(r: Number) = rotateY(r.toDouble())

    fun rotateZ(r: Double) = Transform.zRotation(r) * this
    fun rotateZ(r: Number) = rotateZ(r.toDouble())

    fun scale(x: Double, y: Double, z: Double) = Transform.scaling(x, y, z) * this
    fun scale(x: Number, y: Number, z: Number) = scale(x.toDouble(), y.toDouble(), z.toDouble())

    fun translate(x: Double, y: Double, z: Double) = Transform.translation(x, y, z) * this
    fun translate(x: Number, y: Number, z: Number) = translate(x.toDouble(), y.toDouble(), z.toDouble())

    fun shear(xByY: Double, xByZ: Double, yByX: Double, yByZ: Double, zByX: Double, zByY: Double) = Transform.shearing(xByY, xByZ, yByX, yByZ, zByX, zByY)
    fun shear(xByY: Number, xByZ: Number, yByX: Number, yByZ: Number, zByX: Number, zByY: Number) = shear(
        xByY.toDouble(), xByZ.toDouble(), yByX.toDouble(), yByZ.toDouble(), zByX.toDouble(), zByY.toDouble()
    )

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

    override fun toString(): String {
        val buffer = StringBuilder(width * height * 3)
        repeat(height) { y ->
            repeat(width) { x ->
                buffer.append("${this[y, x]}, ")
            }
            buffer.append("\n")
        }
        return buffer.toString()
    }
}
