package fr.chsfleury.raytracer.linalg.impl

import fr.chsfleury.raytracer.Doubles.eq
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

        assertThat(m[0, 0] eq 1).isTrue()
        assertThat(m[0, 3] eq 4).isTrue()
        assertThat(m[1, 0] eq 5.5).isTrue()
        assertThat(m[1, 2] eq 7.5).isTrue()
        assertThat(m[2, 2] eq 11).isTrue()
        assertThat(m[3, 0] eq 13.5).isTrue()
        assertThat(m[3, 2] eq 15.5).isTrue()
    }

    @Test
    fun `A 2x2 matrix ought to be representable` () {
        val m = NDArray.of2x2(
            -3, 5,
            1,  -2
        )
        assertThat(m[0, 0] eq -3).isTrue()
        assertThat(m[0, 1] eq 5).isTrue()
        assertThat(m[1, 0] eq 1).isTrue()
        assertThat(m[1, 1] eq -2).isTrue()
    }

    @Test
    fun `A 3x3 matrix ought to be representable` () {
        val m = NDArray.of3x3(
            -3, 5,  0,
            1,  -2, -7,
            0,  1,  1
        )
        assertThat(m[0, 0] eq -3).isTrue()
        assertThat(m[1, 1] eq -2).isTrue()
        assertThat(m[2, 2] eq 1).isTrue()
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
}