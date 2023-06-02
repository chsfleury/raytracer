package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.args
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.cases
import fr.chsfleury.raytracer.cylinder
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.POSITIVE_INFINITY

class CylinderTest {

    @ParameterizedTest
    @MethodSource("rayMissesCylinder")
    fun `A ray misses a cylinder`(origin: Vec4, direction: Vec4) {
        val cyl = cylinder()
        val r = ray(origin, direction.normalize())
        assertThat(cyl.localIntersect(r)).isEmpty()
    }

    @ParameterizedTest
    @MethodSource("rayStrikesCylinder")
    fun `A ray strikes a cylinder`(origin: Vec4, direction: Vec4, t0: Double, t1: Double) {
        val cyl = cylinder()
        val r = ray(origin, direction.normalize())
        val xs = cyl.localIntersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(t0)
        assertThatDouble(xs[1].t).isEqualTo(t1)
    }

    @ParameterizedTest
    @MethodSource("normalVector")
    fun `Normal vector on a cylinder`(point: Vec4, normal: Vec4) {
        assertThatVec4(cylinder().localNormalAt(point)).isEqualTo(normal)
    }

    @Test
    fun `The default minimum and maximum for a cylinder`() {
        val cyl = cylinder()
        assertThat(cyl.minimum).isEqualTo(NEGATIVE_INFINITY)
        assertThat(cyl.maximum).isEqualTo(POSITIVE_INFINITY)
    }

    @ParameterizedTest
    @MethodSource("intersectsConstrainedCylinder")
    fun `Intersecting a constrained cylinder`(point: Vec4, direction: Vec4, count: Int) {
        val cyl = cylinder(
            minimum = 1.0,
            maximum = 2.0
        )
        val r = ray(point, direction.normalize())
        val xs = cyl.localIntersect(r)
        assertThat(xs).hasSize(count)
    }

    @Test
    fun `The default closed value for a cylinder`() {
        assertThat(cylinder().closed).isFalse()
    }

    @ParameterizedTest
    @MethodSource("cylinderCaps")
    fun `Intersecting the caps of a closed cylinder`(point: Vec4, direction: Vec4, count: Int) {
        val cyl = cylinder(
            minimum = 1.0,
            maximum = 2.0,
            closed = true
        )
        val r = ray(point, direction.normalize())
        assertThat(cyl.localIntersect(r)).hasSize(count)
    }

    @ParameterizedTest
    @MethodSource("normalVectorCaps")
    fun `The normal vector on a cylinder's end caps` (point: Vec4, normal: Vec4) {
        val cyl = cylinder(
            minimum = 1.0,
            maximum = 2.0,
            closed = true
        )
        assertThatVec4(cyl.localNormalAt(point)).isEqualTo(normal)
    }

    companion object {

        @JvmStatic
        fun rayMissesCylinder() = cases(
            args(point(1, 0, 0), vector(0, 1, 0)),
            args(point(0, 0, 0), vector(0, 1, 0)),
            args(point(0, 0, -5), vector(1, 1, 1))
        )

        @JvmStatic
        fun rayStrikesCylinder() = cases(
            args(point(1, 0, -5), vector(0, 0, 1), 5, 5),
            args(point(0, 0, -5), vector(0, 0, 1), 4, 6),
            args(point(0.5, 0, -5), vector(0.1, 1, 1), 6.80798, 7.08872)
        )

        @JvmStatic
        fun normalVector() = cases(
            args(point(1, 0, 0), vector(1, 0, 0)),
            args(point(0, 5, -1), vector(0, 0, -1)),
            args(point(0, -2, 1), vector(0, 0, 1)),
            args(point(-1, 1, 0), vector(-1, 0, 0))
        )

        @JvmStatic
        fun intersectsConstrainedCylinder() = cases(
            args(point(0, 1.5, 0), vector(0.1, 1, 0), 0),
            args(point(0, 3, -5), vector(0, 0, 1), 0),
            args(point(0, 0, -5), vector(0, 0, 1), 0),
            args(point(0, 2, -5), vector(0, 0, 1), 0),
            args(point(0, 1, -5), vector(0, 0, 1), 0),
            args(point(0, 1.5, -2), vector(0, 0, 1), 2)
        )

        @JvmStatic
        fun cylinderCaps() = cases(
            args(point(0, 3, 0), vector(0, -1, 0), 2),
            args(point(0, 3, -2), vector(0, -1, 2), 2),
            args(point(0, 4, -2), vector(0, -1, 1), 2), // corner case
            args(point(0, 0, -2), vector(0, 1, 2), 2),
            args(point(0, -1, -2), vector(0, 1, 1), 2), // corner case
        )

        @JvmStatic
        fun normalVectorCaps() = cases(
            args(point(0, 1, 0), vector(0, -1, 0)),
            args(point(0.5, 1, 0), vector(0, -1, 0)),
            args(point(0, 1, 0.5), vector(0, -1, 0)),
            args(point(0, 2, 0), vector(0, 1, 0)),
            args(point(0.5, 2, 0), vector(0, 1, 0)),
            args(point(0, 2, 0.5), vector(0, 1, 0))
        )
    }

}