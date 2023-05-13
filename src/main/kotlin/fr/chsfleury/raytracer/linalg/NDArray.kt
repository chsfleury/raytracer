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

        fun identity(n: Int, force: Boolean = false): NDArray {
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
