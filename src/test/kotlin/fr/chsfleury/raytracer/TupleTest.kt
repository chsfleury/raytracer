package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.eq
import fr.chsfleury.raytracer.Tuple.Companion.point
import fr.chsfleury.raytracer.Tuple.Companion.vector
import fr.chsfleury.raytracer.assertions.TupleAssert.Companion.assertThatTuple
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class TupleTest {

    @Test
    fun `A tuple with w=1,0 is a point` () {
        // Given
        val a = point(4.3, -4.2, 3.1)
        assertThat(a)
            .extracting("x", "y", "z", "w")
            .containsExactly(4.3, -4.2, 3.1, 1.0)

        assertThatTuple(a).isAPoint()
        assertThatTuple(a).isNotAVector()
    }

    @Test
    fun `A tuple with w=0,0 is a vector` () {
        // Given
        val a = vector(4.3, -4.2, 3.1)
        assertThat(a)
            .extracting("x", "y", "z", "w")
            .containsExactly(4.3, -4.2, 3.1, 0.0)

        assertThatTuple(a).isNotAPoint()
        assertThatTuple(a).isAVector()
    }

    @Test
    fun `Adding two tuples` () {
        val a1 = point(3, -2, 5)
        val a2 = vector(-2, 3, 1)
        assertThatTuple(a1 + a2)
            .isEqualTo(point(1, 1, 6))
    }

    @Test
    fun `Substracting two points` () {
        val p1 = point(3, 2, 1)
        val p2 = point(5, 6, 7)
        assertThatTuple(p1 - p2)
            .isEqualTo(vector(-2, -4, -6))
    }

    @Test
    fun `Substracting a vector from a point` () {
        val p = point(3, 2, 1)
        val v = vector(5, 6, 7)
        assertThatTuple(p - v)
            .isEqualTo(point(-2, -4, -6))
    }

    @Test
    fun `Substracting two vectors` () {
        val v1 = vector(3, 2, 1)
        val v2 = vector(5, 6, 7)
        assertThatTuple(v1 - v2)
            .isEqualTo(vector(-2, -4, -6))
    }

    @Test
    fun `Subtracting a vector from the zero vector` () {
        val zero = vector(0, 0, 0)
        val v = vector(1, -2, 3)
        assertThatTuple(zero - v)
            .isEqualTo(vector(-1, 2, -3))
    }

    @Test
    fun `Negating a tuple` () {
        val a = Tuple(1, -2, 3, -4)
        assertThatTuple(-a)
            .isEqualTo(Tuple(-1, 2, -3, 4))
    }

    @Test
    fun `Multiplying a tuple by a scalar` () {
        val a = Tuple(1, -2, 3, -4)
        assertThatTuple(a * 3.5)
            .isEqualTo(Tuple(3.5, -7, 10.5, -14))
    }

    @Test
    fun ` Multiplying a tuple by a fraction` () {
        val a = Tuple(1, -2, 3, -4)
        assertThatTuple(a * 0.5)
            .isEqualTo(Tuple(0.5, -1, 1.5, -2))
    }

    @Test
    fun `Dividing a tuple by a scalar` () {
        val a = Tuple(1, -2, 3, -4)
        assertThatTuple(a / 2)
            .isEqualTo(Tuple(0.5, -1, 1.5, -2))
    }

    @Test
    fun `Computing the magnitude of vector(1, 0, 0)` () {
        val v = vector(1, 0, 0)
        assertThatTuple(v)
            .hasMagnitude(1)
    }

    @Test
    fun `Computing the magnitude of vector(0, 1, 0)` () {
        val v = vector(0, 1, 0)
        assertThatTuple(v)
            .hasMagnitude(1)
    }

    @Test
    fun `Computing the magnitude of vector(0, 0, 1)` () {
        val v = vector(0, 0, 1)
        assertThatTuple(v)
            .hasMagnitude(1)
    }

    @Test
    fun `Computing the magnitude of vector(1, 2, 3)` () {
        val v = vector(1, 2, 3)
        assertThatTuple(v)
            .hasMagnitude(sqrt(14.0))
    }

    @Test
    fun `Computing the magnitude of vector(-1, -2, -3)` () {
        val v = vector(-1, -2, -3)
        assertThatTuple(v)
            .hasMagnitude(sqrt(14.0))
    }

    @Test
    fun `Normalizing vector(4, 0, 0) gives (1, 0, 0)` () {
        val v = vector(4, 0, 0)
        assertThatTuple(v.normalize())
            .isEqualTo(vector(1, 0, 0))
    }

    @Test
    fun `Normalizing vector(1, 2, 3)` () {
        val v = vector(1, 2, 3)
        assertThatTuple(v.normalize())
            .isEqualTo(vector(0.26726, 0.53452, 0.80178))
    }

    @Test
    fun `The magnitude of a normalized vector` () {
        val v = vector(1, 2, 3)
        assertThatTuple(v.normalize())
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
        assertThatTuple(a cross b)
            .isEqualTo(vector(-1, 2, -1))
        assertThatTuple(b cross a)
            .isEqualTo(vector(1, -2, 1))
    }
}