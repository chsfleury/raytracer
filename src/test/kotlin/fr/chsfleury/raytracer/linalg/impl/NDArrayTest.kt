package fr.chsfleury.raytracer.linalg.impl

import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NDArrayTest {

    @Test
    fun `Constructing and inspecting a 4x4 matrix` () {
        val m = NDArray.of4x4(
            1,      2,      3,      4,
            5.5,    6.5,    7.5,    8.5,
            9,      10,     11,     12,
            13.5,   14.5,   15.5,   16.5
        )

        assertThatDouble(m[0, 0]).isEqualTo(1)
        assertThatDouble(m[0, 3]).isEqualTo(4)
        assertThatDouble(m[1, 0]).isEqualTo(5.5)
        assertThatDouble(m[1, 2]).isEqualTo(7.5)
        assertThatDouble(m[2, 2]).isEqualTo(11)
        assertThatDouble(m[3, 0]).isEqualTo(13.5)
        assertThatDouble(m[3, 2]).isEqualTo(15.5)
    }

    @Test
    fun `A 2x2 matrix ought to be representable` () {
        val m = NDArray.of2x2(
            -3, 5,
            1,  -2
        )
        assertThatDouble(m[0, 0]).isEqualTo(-3)
        assertThatDouble(m[0, 1]).isEqualTo(5)
        assertThatDouble(m[1, 0]).isEqualTo(1)
        assertThatDouble(m[1, 1]).isEqualTo(-2)
    }

    @Test
    fun `A 3x3 matrix ought to be representable` () {
        val m = NDArray.of3x3(
            -3, 5,  0,
            1,  -2, -7,
            0,  1,  1
        )
        assertThatDouble(m[0, 0]).isEqualTo(-3)
        assertThatDouble(m[1, 1]).isEqualTo(-2)
        assertThatDouble(m[2, 2]).isEqualTo(1)
    }

    @Test
    fun `Matrix equality with identical matrices` () {
        val m1 = NDArray.of4x4(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

        val m2 = NDArray.of4x4(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

        assertThatNDArray(m1).isEqualTo(m2)
    }

    @Test
    fun `Matrix equality with different matrices` () {
        val m1 = NDArray.of4x4(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

        val m2 = NDArray.of4x4(
            2, 3, 4, 5,
            6, 7, 8, 9,
            8, 7, 6, 5,
            4, 3, 2, 1
        )

        assertThatNDArray(m1).isNotEqualTo(m2)
    }

    @Test
    fun `Multiplying two matrices` () {
        val m1 = NDArray.of4x4(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

        val m2 = NDArray.of4x4(
            -2, 1, 2, 3,
            3,  2, 1, -1,
            4,  3, 6, 5,
            1,  2, 7, 8
        )

        val result = NDArray.of4x4(
            20, 22, 50,  48,
            44, 54, 114, 108,
            40, 58, 110, 102,
            16, 26, 46,  42
        )

        assertThatNDArray(m1 * m2)
            .isEqualTo(result)
    }

    @Test
    fun `A matrix multiplied by a tuple` () {
        val m = NDArray.of4x4(
            1, 2, 3, 4,
            2, 4, 4, 2,
            8, 6, 4, 1,
            0, 0, 0, 1
        )

        val v = Vec4.tuple(1, 2, 3, 1)

        assertThatVec4(m * v)
            .isEqualTo(Vec4.tuple(18, 24, 33, 1))
    }

    @Test
    fun `Multiplying a matrix by the identity matrix` () {
        val m = NDArray.of4x4(
            0, 1, 2,  4,
            1, 2, 4,  8,
            2, 4, 8,  16,
            4, 8, 16, 32
        )

        assertThatNDArray(m * NDArray.identity(4))
            .isEqualTo(m)
    }

    @Test
    fun `Multiplying the identity matrix by a tuple` () {
        val a = Vec4.tuple(1, 2, 3, 4)
        assertThatVec4(NDArray.identity(4) * a)
            .isEqualTo(a)
    }

    @Test
    fun `Transposing a matrix` () {
        val m = NDArray.of4x4(
            0, 9, 3, 0,
            9, 8, 0, 8,
            1, 8, 5, 3,
            0, 0, 5, 8
        )

        val transposed = NDArray.of4x4(
            0, 9, 1, 0,
            9, 8, 8, 0,
            3, 0, 5, 5,
            0, 8, 3, 8
        )

        assertThatNDArray(m.transpose())
            .isEqualTo(transposed)
    }

    @Test
    fun `Transposing the identity matrix` () {
        assertThatNDArray(NDArray.identity(4).transpose())
            .isEqualTo(NDArray.identity(4))
    }

    @Test
    fun `Calculating the determinant of a 2x2 matrix` () {
        val m = NDArray.of2x2(
            1,  5,
            -3, 2
        )
        assertThatDouble(m.determinant())
            .isEqualTo(17)
    }

    @Test
    fun `A submatrix of a 3x3 matrix is a 2x2 matrix` () {
        val m = NDArray.of3x3(
            1,  5, 0,
            -3, 2, 7,
            0,  6, -3
        )

        val expectedSubMatrix = NDArray.of2x2(
            -3, 2,
            0,  6
        )

        assertThatNDArray(m.subMatrix(0, 2))
            .isEqualTo(expectedSubMatrix)
    }

    @Test
    fun `A submatrix of a 4x4 matrix is a 3x3 matrix` () {
        val m = NDArray.of4x4(
            -6, 1, 1,  6,
            -8, 5, 8,  6,
            -1, 0, 8,  2,
            -7, 1, -1, 1
        )

        val expectedSubMatrix = NDArray.of3x3(
            -6, 1,  6,
            -8, 8,  6,
            -7, -1, 1
        )

        assertThatNDArray(m.subMatrix(2, 1))
            .isEqualTo(expectedSubMatrix)
    }

    @Test
    fun `Calculating a minor of a 3x3 matrix` () {
        val m = NDArray.of3x3(
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        )

        assertThatDouble(m.minor(1, 0))
            .isEqualTo(25)
    }

    @Test
    fun `Calculating a cofactor of a 3x3 matrix` () {
        val m = NDArray.of3x3(
            3, 5,  0,
            2, -1, -7,
            6, -1, 5
        )
        assertThatDouble(m.minor(0, 0)).isEqualTo(-12)
        assertThatDouble(m.cofactor(0, 0)).isEqualTo(-12)
        assertThatDouble(m.minor(1, 0)).isEqualTo(25)
        assertThatDouble(m.cofactor(1, 0)).isEqualTo(-25)
    }

    @Test
    fun `Calculating the determinant of a 3x3 matrix` () {
        val m = NDArray.of3x3(
            1,  2, 6,
            -5, 8, -4,
            2,  6, 4
        )
        assertThatDouble(m.cofactor(0, 0)).isEqualTo(56)
        assertThatDouble(m.cofactor(0, 1)).isEqualTo(12)
        assertThatDouble(m.cofactor(0, 2)).isEqualTo(-46)
        assertThatDouble(m.determinant()).isEqualTo(-196)
    }

    @Test
    fun `Calculating the determinant of a 4x4 matrix` () {
        val m = NDArray.of4x4(
            -2, -8, 3, 5,
            -3, 1,  7, 3,
            1,  2,  -9, 6,
            -6, 7,  7, -9
        )
        assertThatDouble(m.cofactor(0, 0)).isEqualTo(690)
        assertThatDouble(m.cofactor(0, 1)).isEqualTo(447)
        assertThatDouble(m.cofactor(0, 2)).isEqualTo(210)
        assertThatDouble(m.cofactor(0, 3)).isEqualTo(51)
        assertThatDouble(m.determinant()).isEqualTo(-4071)

    }
}