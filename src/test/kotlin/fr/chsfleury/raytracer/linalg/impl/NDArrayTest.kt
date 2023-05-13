package fr.chsfleury.raytracer.linalg.impl

import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.exp

class NDArrayTest {

    @Test
    fun `Constructing and inspecting a 4x4 matrix`() {
        val m = NDArray.of4x4(
            1, 2, 3, 4,
            5.5, 6.5, 7.5, 8.5,
            9, 10, 11, 12,
            13.5, 14.5, 15.5, 16.5
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
    fun `A 2x2 matrix ought to be representable`() {
        val m = NDArray.of2x2(
            -3, 5,
            1, -2
        )
        assertThatDouble(m[0, 0]).isEqualTo(-3)
        assertThatDouble(m[0, 1]).isEqualTo(5)
        assertThatDouble(m[1, 0]).isEqualTo(1)
        assertThatDouble(m[1, 1]).isEqualTo(-2)
    }

    @Test
    fun `A 3x3 matrix ought to be representable`() {
        val m = NDArray.of3x3(
            -3, 5, 0,
            1, -2, -7,
            0, 1, 1
        )
        assertThatDouble(m[0, 0]).isEqualTo(-3)
        assertThatDouble(m[1, 1]).isEqualTo(-2)
        assertThatDouble(m[2, 2]).isEqualTo(1)
    }

    @Test
    fun `Matrix equality with identical matrices`() {
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
    fun `Matrix equality with different matrices`() {
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
    fun `Multiplying two matrices`() {
        val m1 = NDArray.of4x4(
            1, 2, 3, 4,
            5, 6, 7, 8,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

        val m2 = NDArray.of4x4(
            -2, 1, 2, 3,
            3, 2, 1, -1,
            4, 3, 6, 5,
            1, 2, 7, 8
        )

        val result = NDArray.of4x4(
            20, 22, 50, 48,
            44, 54, 114, 108,
            40, 58, 110, 102,
            16, 26, 46, 42
        )

        assertThatNDArray(m1 * m2)
            .isEqualTo(result)
    }

    @Test
    fun `A matrix multiplied by a tuple`() {
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
    fun `Multiplying a matrix by the identity matrix`() {
        val m = NDArray.of4x4(
            0, 1, 2, 4,
            1, 2, 4, 8,
            2, 4, 8, 16,
            4, 8, 16, 32
        )

        assertThatNDArray(m * NDArray.identity(4))
            .isEqualTo(m)
    }

    @Test
    fun `Multiplying the identity matrix by a tuple`() {
        val a = Vec4.tuple(1, 2, 3, 4)
        assertThatVec4(NDArray.identity(4) * a)
            .isEqualTo(a)
    }

    @Test
    fun `Transposing a matrix`() {
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
    fun `Transposing the identity matrix`() {
        assertThatNDArray(NDArray.identity(4).transpose())
            .isEqualTo(NDArray.identity(4))
    }

    @Test
    fun `Calculating the determinant of a 2x2 matrix`() {
        val m = NDArray.of2x2(
            1, 5,
            -3, 2
        )
        assertThatDouble(m.determinant())
            .isEqualTo(17)
    }

    @Test
    fun `A submatrix of a 3x3 matrix is a 2x2 matrix`() {
        val m = NDArray.of3x3(
            1, 5, 0,
            -3, 2, 7,
            0, 6, -3
        )

        val expectedSubMatrix = NDArray.of2x2(
            -3, 2,
            0, 6
        )

        assertThatNDArray(m.subMatrix(0, 2))
            .isEqualTo(expectedSubMatrix)
    }

    @Test
    fun `A submatrix of a 4x4 matrix is a 3x3 matrix`() {
        val m = NDArray.of4x4(
            -6, 1, 1, 6,
            -8, 5, 8, 6,
            -1, 0, 8, 2,
            -7, 1, -1, 1
        )

        val expectedSubMatrix = NDArray.of3x3(
            -6, 1, 6,
            -8, 8, 6,
            -7, -1, 1
        )

        assertThatNDArray(m.subMatrix(2, 1))
            .isEqualTo(expectedSubMatrix)
    }

    @Test
    fun `Calculating a minor of a 3x3 matrix`() {
        val m = NDArray.of3x3(
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        )

        assertThatDouble(m.minor(1, 0))
            .isEqualTo(25)
    }

    @Test
    fun `Calculating a cofactor of a 3x3 matrix`() {
        val m = NDArray.of3x3(
            3, 5, 0,
            2, -1, -7,
            6, -1, 5
        )
        assertThatDouble(m.minor(0, 0)).isEqualTo(-12)
        assertThatDouble(m.cofactor(0, 0)).isEqualTo(-12)
        assertThatDouble(m.minor(1, 0)).isEqualTo(25)
        assertThatDouble(m.cofactor(1, 0)).isEqualTo(-25)
    }

    @Test
    fun `Calculating the determinant of a 3x3 matrix`() {
        val m = NDArray.of3x3(
            1, 2, 6,
            -5, 8, -4,
            2, 6, 4
        )
        assertThatDouble(m.cofactor(0, 0)).isEqualTo(56)
        assertThatDouble(m.cofactor(0, 1)).isEqualTo(12)
        assertThatDouble(m.cofactor(0, 2)).isEqualTo(-46)
        assertThatDouble(m.determinant()).isEqualTo(-196)
    }

    @Test
    fun `Calculating the determinant of a 4x4 matrix`() {
        val m = NDArray.of4x4(
            -2, -8, 3, 5,
            -3, 1, 7, 3,
            1, 2, -9, 6,
            -6, 7, 7, -9
        )
        assertThatDouble(m.cofactor(0, 0)).isEqualTo(690)
        assertThatDouble(m.cofactor(0, 1)).isEqualTo(447)
        assertThatDouble(m.cofactor(0, 2)).isEqualTo(210)
        assertThatDouble(m.cofactor(0, 3)).isEqualTo(51)
        assertThatDouble(m.determinant()).isEqualTo(-4071)
    }

    @Test
    fun `Testing an invertible matrix for invertibility`() {
        val m = NDArray.of4x4(
            6, 4, 4, 4,
            5, 5, 7, 6,
            4, -9, 3, -7,
            9, 1, 7, -6
        )

        assertThatDouble(m.determinant()).isEqualTo(-2120)
        assertThat(m.isInvertible()).isTrue()
    }

    @Test
    fun `Testing a noninvertible matrix for invertibility`() {
        val m = NDArray.of4x4(
            -4, 2, -2, -3,
            9, 6, 2, 6,
            0, -5, 1, -5,
            0, 0, 0, 0
        )
        assertThatDouble(m.determinant()).isEqualTo(0)
        assertThat(m.isInvertible()).isFalse()
    }

    @Test
    fun `Calculating the inverse of a matrix`() {
        val a = NDArray.of4x4(
            -5, 2, 6, -8,
            1, -5, 1, 8,
            7, 7, -6, -7,
            1, -3, 7, 4
        )
        assertThatDouble(a.determinant()).isEqualTo(532)
        assertThatDouble(a.cofactor(2, 3)).isEqualTo(-160)
        assertThatDouble(a.cofactor(3, 2)).isEqualTo(105)
        val b = a.inverse()
        assertThatDouble(b[3, 2]).isEqualTo(-160.0 / 532)
        assertThatDouble(b[2, 3]).isEqualTo(105.0 / 532)

        val expected = NDArray.of4x4(
            0.21805, 0.45113, 0.24060, -0.04511,
            -0.80827, -1.45677, -0.44361, 0.52068,
            -0.07895, -0.22368, -0.05263, 0.19737,
            -0.52256, -0.81391, -0.30075, 0.30639
        )

        assertThatNDArray(b).isEqualTo(expected)
    }

    @Test
    fun `Calculating the inverse of another matrix`() {
        val a = NDArray.of4x4(
            8, -5, 9, 2,
            7, 5, 6, 1,
            -6, 0, 9, 6,
            -3, 0, -9, -4
        )
        val expected = NDArray.of4x4(
            -0.15385, -0.15385, -0.28205, -0.53846,
            -0.07692, 0.12308, 0.02564, 0.03077,
            0.35897, 0.35897, 0.43590, 0.92308,
            -0.69231, -0.69231, -0.76923, -1.92308
        )
        assertThatNDArray(a.inverse()).isEqualTo(expected)
    }

    @Test
    fun `Calculating the inverse of a third matrix` () {
        val a = NDArray.of4x4(
            9, 3, 0, 9,
            -5, -2, -6, -3,
            -4, 9, 6, 4,
            -7, 6, 6, 2
        )
        val expected = NDArray.of4x4(
            -0.04074, -0.07778, 0.14444, -0.22222,
            -0.07778, 0.03333, 0.36667, -0.33333,
            -0.02901, -0.14630, -0.10926, 0.12963,
            0.17778, 0.06667, -0.26667, 0.33333
        )
        assertThatNDArray(a.inverse()).isEqualTo(expected)
    }

    @Test
    fun `Multiplying a product by its inverse` () {
        val a = NDArray.of4x4(
            3, -9, 7, 3,
            3, -8, 2, -9,
            -4, 4, 4, 1,
            -6, 5, -1, 1
        )

        val b = NDArray.of4x4(
            8, 2, 2, 2,
            3, -1, 7, 0,
            7, 0, 5, 4,
            6, -2, 0, 5
        )

        val c = a * b
        assertThatNDArray(c * b.inverse()).isEqualTo(a)
    }
}