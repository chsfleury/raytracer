package fr.chsfleury.raytracer.linalg.impl

import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.tuple
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class Vec4Test {

    @Test
    fun `A tuple with w=1,0 is a point` () {
        // Given
        val a = Vec4.point(4.3, -4.2, 3.1)
        assertThat(a)
            .extracting("x", "y", "z", "w")
            .containsExactly(4.3, -4.2, 3.1, 1.0)

        assertThatVec4(a).isAPoint()
        assertThatVec4(a).isNotAVector()
    }

    @Test
    fun `A tuple with w=0,0 is a vector` () {
        // Given
        val a = vector(4.3, -4.2, 3.1)
        assertThat(a)
            .extracting("x", "y", "z", "w")
            .containsExactly(4.3, -4.2, 3.1, 0.0)

        assertThatVec4(a).isNotAPoint()
        assertThatVec4(a).isAVector()
    }

    @Test
    fun `Adding two tuples` () {
        val a1 = point(3, -2, 5)
        val a2 = vector(-2, 3, 1)
        assertThatVec4(a1 + a2)
            .isEqualTo(point(1, 1, 6))
    }

    @Test
    fun `Substracting two points` () {
        val p1 = point(3, 2, 1)
        val p2 = point(5, 6, 7)
        assertThatVec4(p1 - p2)
            .isEqualTo(vector(-2, -4, -6))
    }

    @Test
    fun `Substracting a vector from a point` () {
        val p = point(3, 2, 1)
        val v = vector(5, 6, 7)
        assertThatVec4(p - v)
            .isEqualTo(point(-2, -4, -6))
    }

    @Test
    fun `Substracting two vectors` () {
        val v1 = vector(3, 2, 1)
        val v2 = vector(5, 6, 7)
        assertThatVec4(v1 - v2)
            .isEqualTo(vector(-2, -4, -6))
    }

    @Test
    fun `Subtracting a vector from the zero vector` () {
        val zero = vector(0, 0, 0)
        val v = vector(1, -2, 3)
        assertThatVec4(zero - v)
            .isEqualTo(vector(-1, 2, -3))
    }

    @Test
    fun `Negating a tuple` () {
        val a = tuple(1, -2, 3, -4)
        assertThatVec4(-a)
            .isEqualTo(tuple(-1, 2, -3, 4))
    }

    @Test
    fun `Multiplying a tuple by a scalar` () {
        val a = tuple(1, -2, 3, -4)
        assertThatVec4(a * 3.5)
            .isEqualTo(tuple(3.5, -7, 10.5, -14))
    }

    @Test
    fun ` Multiplying a tuple by a fraction` () {
        val a = tuple(1, -2, 3, -4)
        assertThatVec4(a * 0.5)
            .isEqualTo(tuple(0.5, -1, 1.5, -2))
    }

    @Test
    fun `Dividing a tuple by a scalar` () {
        val a = tuple(1, -2, 3, -4)
        assertThatVec4(a / 2)
            .isEqualTo(tuple(0.5, -1, 1.5, -2))
    }

    @Test
    fun `Computing the magnitude of vector(1, 0, 0)` () {
        val v = vector(1, 0, 0)
        assertThatVec4(v)
            .hasMagnitude(1)
    }

    @Test
    fun `Computing the magnitude of vector(0, 1, 0)` () {
        val v = vector(0, 1, 0)
        assertThatVec4(v)
            .hasMagnitude(1)
    }

    @Test
    fun `Computing the magnitude of vector(0, 0, 1)` () {
        val v = vector(0, 0, 1)
        assertThatVec4(v)
            .hasMagnitude(1)
    }

    @Test
    fun `Computing the magnitude of vector(1, 2, 3)` () {
        val v = vector(1, 2, 3)
        assertThatVec4(v)
            .hasMagnitude(sqrt(14.0))
    }

    @Test
    fun `Computing the magnitude of vector(-1, -2, -3)` () {
        val v = vector(-1, -2, -3)
        assertThatVec4(v)
            .hasMagnitude(sqrt(14.0))
    }

    @Test
    fun `Normalizing vector(4, 0, 0) gives (1, 0, 0)` () {
        val v = vector(4, 0, 0)
        assertThatVec4(v.normalize())
            .isEqualTo(vector(1, 0, 0))
    }

    @Test
    fun `Normalizing vector(1, 2, 3)` () {
        val v = vector(1, 2, 3)
        assertThatVec4(v.normalize())
            .isEqualTo(vector(0.26726, 0.53452, 0.80178))
    }

    @Test
    fun `The magnitude of a normalized vector` () {
        val v = vector(1, 2, 3)
        assertThatVec4(v.normalize())
            .hasMagnitude(1)
    }

    @Test
    fun `The dot product of two tuples` () {
        val a = vector(1, 2, 3)
        val b = vector(2, 3, 4)
        val dot = a dot b
        assertThat(dot eq 20).isTrue()
    }

    @Test
    fun ` The cross product of two vectors` () {
        val a = vector(1, 2, 3)
        val b = vector(2, 3, 4)
        assertThatVec4(a cross b)
            .isEqualTo(vector(-1, 2, -1))
        assertThatVec4(b cross a)
            .isEqualTo(vector(1, -2, 1))
    }

}