package fr.chsfleury.raytracer.linalg

import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.viewTransform
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class TransformTest {

    @Test
    fun `Multiplying by a translation matrix`() {
        val t = Transform.translation(5, -3, 2)
        val p = point(-3, 4, 5)
        assertThatVec4(t * p).isEqualTo(point(2, 1, 7))
    }

    @Test
    fun `Multiplying by the inverse of a translation matrix`() {
        val t = Transform.translation(5, -3, 2).inverse()
        val p = point(-3, 4, 5)
        assertThatVec4(t * p).isEqualTo(point(-8, 7, 3))
    }

    @Test
    fun `Translation does not affect vectors`() {
        val t = Transform.translation(5, -3, 2)
        val v = vector(-3, 4, 5)
        assertThatVec4(t * v).isEqualTo(v)
    }

    @Test
    fun `A scaling matrix applied to a point`() {
        val t = Transform.scaling(2, 3, 4)
        val p = point(-4, 6, 8)
        assertThatVec4(t * p).isEqualTo(point(-8, 18, 32))
    }

    @Test
    fun `A scaling matrix applied to a vector`() {
        val t = Transform.scaling(2, 3, 4)
        val v = vector(-4, 6, 8)
        assertThatVec4(t * v).isEqualTo(vector(-8, 18, 32))
    }

    @Test
    fun `Multiplying by the inverse of a scaling matrix`() {
        val t = Transform.scaling(2, 3, 4).inverse()
        val v = vector(-4, 6, 8)
        assertThatVec4(t * v).isEqualTo(vector(-2, 2, 2))
    }

    @Test
    fun `Reflection is scaling by a negative value`() {
        val t = Transform.scaling(-1, 1, 1)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(-2, 3, 4))
    }

    @Test
    fun `Rotating a point around the x axis`() {
        val p = point(0, 1, 0)
        val halfQuarter = Transform.xRotation(PI / 4)
        val fullQuarter = Transform.xRotation(PI / 2)
        assertThatVec4(halfQuarter * p).isEqualTo(point(0, sqrt(2.0) / 2, sqrt(2.0) / 2))
        assertThatVec4(fullQuarter * p).isEqualTo(point(0, 0, 1))
    }

    @Test
    fun `The inverse of an x-rotation rotates in the opposite direction`() {
        val p = point(0, 1, 0)
        val halfQuarter = Transform.xRotation(PI / 4)
        val inv = halfQuarter.inverse()
        assertThatVec4(inv * p).isEqualTo(point(0, sqrt(2.0) / 2, -sqrt(2.0) / 2))
    }

    @Test
    fun `Rotating a point around the y axis`() {
        val p = point(0, 0, 1)
        val halfQuarter = Transform.yRotation(PI / 4)
        val fullQuarter = Transform.yRotation(PI / 2)
        assertThatVec4(halfQuarter * p).isEqualTo(point(sqrt(2.0) / 2, 0, sqrt(2.0) / 2))
        assertThatVec4(fullQuarter * p).isEqualTo(point(1, 0, 0))
    }

    @Test
    fun `Rotating a point around the z axis`() {
        val p = point(0, 1, 0)
        val halfQuarter = Transform.zRotation(PI / 4)
        val fullQuarter = Transform.zRotation(PI / 2)
        assertThatVec4(halfQuarter * p).isEqualTo(point(-sqrt(2.0) / 2, sqrt(2.0) / 2, 0))
        assertThatVec4(fullQuarter * p).isEqualTo(point(-1, 0, 0))
    }

    @Test
    fun `A shearing transformation moves x in proportion to y`() {
        val t = Transform.shearing(1, 0, 0, 0, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(5, 3, 4))
    }

    @Test
    fun `A shearing transformation moves x in proportion to z`() {
        val t = Transform.shearing(0, 1, 0, 0, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(6, 3, 4))
    }

    @Test
    fun `A shearing transformation moves y in proportion to x`() {
        val t = Transform.shearing(0, 0, 1, 0, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 5, 4))
    }

    @Test
    fun `A shearing transformation moves y in proportion to z`() {
        val t = Transform.shearing(0, 0, 0, 1, 0, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 7, 4))
    }

    @Test
    fun `A shearing transformation moves z in proportion to x`() {
        val t = Transform.shearing(0, 0, 0, 0, 1, 0)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 3, 6))
    }

    @Test
    fun `A shearing transformation moves z in proportion to y`() {
        val t = Transform.shearing(0, 0, 0, 0, 0, 1)
        val p = point(2, 3, 4)
        assertThatVec4(t * p).isEqualTo(point(2, 3, 7))
    }

    @Test
    fun `Individual transformations are applied in sequence`() {
        val p = point(1, 0, 1)
        val a = Transform.xRotation(PI / 2)
        val b = Transform.scaling(5, 5, 5)
        val c = Transform.translation(10, 5, 7)

        val p2 = a * p
        assertThatVec4(p2).isEqualTo(point(1, -1, 0))

        val p3 = b * p2
        assertThatVec4(p3).isEqualTo(point(5, -5, 0))

        val p4 = c * p3
        assertThatVec4(p4).isEqualTo(point(15, 0, 7))
    }

    @Test
    fun `Chained transformations must be applied in reverse order`() {
        val p = point(1, 0, 1)
        val a = Transform.xRotation(PI / 2)
        val b = Transform.scaling(5, 5, 5)
        val c = Transform.translation(10, 5, 7)
        val t = c * b * a
        assertThatVec4(t * p).isEqualTo(point(15, 0, 7))
    }

    @Test
    fun `The transformation matrix for the default orientation`() {
        val t = viewTransform(
            point(0, 0, 0),
            point(0, 0, -1),
            vector(0, 1, 0)
        )
        assertThatNDArray(t).isIdentity()
    }

    @Test
    fun `A view transformation matrix looking in positive z direction`() {
        val t = viewTransform(
            point(0, 0, 0),
            point(0, 0, 1),
            vector(0, 1, 0)
        )
        assertThatNDArray(t).isEqualTo(scaling(-1, 1, -1))
    }

    @Test
    fun `The view transformation moves the world`() {
        val t = viewTransform(
            point(0, 0, 8),
            point(0, 0, 0),
            vector(0, 1, 0)
        )
        assertThatNDArray(t).isEqualTo(translation(0, 0, -8))
    }

    @Test
    fun `An arbitrary view transformation`() {
        val t = viewTransform(
            point(1, 3, 2),
            point(4, -2, 8),
            vector(1, 1, 0)
        )
        val expected = NDArray.of4x4(
            -0.50709, 0.50709, 0.67612, -2.36643,
            0.76772, 0.60609, 0.12122, -2.82843,
            -0.35857, 0.59761, -0.71714, 0.00000,
            0.00000, 0.00000, 0.00000, 1.00000
        )
        assertThatNDArray(t).isEqualTo(expected)
    }
}