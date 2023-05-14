package fr.chsfleury.raytracer.linalg

import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.linalg.Vec4.Companion.point
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class TransformTest {

    @Test
    fun `Multiplying by a translation matrix` () {
        val t = Transform.translation(5, -3, 2)
        val p = point(-3, 4, 5)
        assertThatVec4(t * p).isEqualTo(point(2, 1, 7))
    }

    @Test
    fun `Multiplying by the inverse of a translation matrix` () {
        val t = Transform.translation(5, -3, 2).inverse()
        val p = point(-3, 4, 5)
        assertThatVec4(t * p).isEqualTo(point(-8, 7, 3))
    }

    @Test
    fun `Translation does not affect vectors` () {
        val t = Transform.translation(5, -3, 2)
        val v = Vec4.vector(-3, 4, 5)
        assertThatVec4(t * v).isEqualTo(v)
    }

    @Test
    fun `A scaling matrix applied to a point` () {
        val t = Transform.scaling(2, 3, 4)
        val p = point(-4, 6, 8)
        assertThatVec4(t * p).isEqualTo(point(-8, 18, 32))
    }

    @Test
    fun `A scaling matrix applied to a vector` () {
        val t = Transform.scaling(2, 3, 4)
        val v = Vec4.vector(-4, 6, 8)
        assertThatVec4(t * v).isEqualTo(Vec4.vector(-8, 18, 32))
    }

    @Test
    fun `Multiplying by the inverse of a scaling matrix` () {
        val t = Transform.scaling(2, 3, 4).inverse()
        val v = Vec4.vector(-4, 6, 8)
        assertThatVec4(t * v).isEqualTo(Vec4.vector(-2, 2, 2))
    }

    @Test
    fun `Reflection is scaling by a negative value` () {
        val t = Transform.scaling(-1, 1, 1)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(-2, 3, 4))
    }

    @Test
    fun `Rotating a point around the x axis` () {
        val p = point(0, 1, 0)
        val halfQuarter = Transform.xRotation(PI / 4)
        val fullQuarter = Transform.xRotation(PI / 2)
        assertThatVec4(halfQuarter * p).isEqualTo(point(0, sqrt(2.0) / 2, sqrt(2.0) / 2))
        assertThatVec4(fullQuarter * p).isEqualTo(point(0, 0, 1))
    }

    @Test
    fun `The inverse of an x-rotation rotates in the opposite direction` () {
        val p = point(0, 1, 0)
        val halfQuarter = Transform.xRotation(PI / 4)
        val inv = halfQuarter.inverse()
        assertThatVec4(inv * p).isEqualTo(point(0, sqrt(2.0) / 2, -sqrt(2.0) / 2))
    }

    @Test
    fun `Rotating a point around the y axis` () {
        val p = point(0, 0, 1)
        val halfQuarter = Transform.yRotation(PI / 4)
        val fullQuarter = Transform.yRotation(PI / 2)
        assertThatVec4(halfQuarter * p).isEqualTo(point(sqrt(2.0) / 2, 0, sqrt(2.0) / 2))
        assertThatVec4(fullQuarter * p).isEqualTo(point(1, 0, 0))
    }

    @Test
    fun `Rotating a point around the z axis` () {
        val p = point(0, 1, 0)
        val halfQuarter = Transform.zRotation(PI / 4)
        val fullQuarter = Transform.zRotation(PI / 2)
        assertThatVec4(halfQuarter * p).isEqualTo(point(-sqrt(2.0) / 2, sqrt(2.0) / 2, 0))
        assertThatVec4(fullQuarter * p).isEqualTo(point(-1, 0, 0))
    }

    @Test
    fun `A shearing transformation moves x in proportion to y` () {
        val t = Transform.shearing(1, 0, 0, 0, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(5, 3, 4))
    }

    @Test
    fun `A shearing transformation moves x in proportion to z` () {
        val t = Transform.shearing(0, 1, 0, 0, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(6, 3, 4))
    }

    @Test
    fun `A shearing transformation moves y in proportion to x` () {
        val t = Transform.shearing(0, 0, 1, 0, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 5, 4))
    }

    @Test
    fun `A shearing transformation moves y in proportion to z` () {
        val t = Transform.shearing(0, 0, 0, 1, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 7, 4))
    }

    @Test
    fun `A shearing transformation moves z in proportion to x` () {
        val t = Transform.shearing(0, 0, 0, 0, 1, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 3, 6))
    }

    @Test
    fun `A shearing transformation moves z in proportion to y` () {
        val t = Transform.shearing(0, 0, 0, 0, 0, 1)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 3, 7))
    }
}