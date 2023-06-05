package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.args
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.cases
import fr.chsfleury.raytracer.cone
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.sqrt

class ConeTest {

    @ParameterizedTest
    @MethodSource("intersectCone")
    fun `Intersecting a cone with a ray`(origin: Vec4, direction: Vec4, t0: Double, t1: Double) {
        val shape = cone()
        val r = ray(origin, direction.normalize())
        val xs = shape.localIntersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(t0)
        assertThatDouble(xs[1].t).isEqualTo(t1)
    }

    @Test
    fun `Intersecting a cone with a ray parallel to one of its halves`() {
        val shape = cone()
        val direction = vector(0, 1, 1).normalize()
        val r = ray(point(0, 0, -1), direction)
        val xs = shape.localIntersect(r)
        assertThat(xs).hasSize(1)
        assertThatDouble(xs[0].t).isEqualTo(0.35355)
    }

    @ParameterizedTest
    @MethodSource("intersectConeCaps")
    fun `Intersecting a cone's end caps`(origin: Vec4, direction: Vec4, count: Int) {
        val shape = cone(
            minimum = -0.5,
            maximum = 0.5,
            closed = true
        )
        val r = ray(origin, direction.normalize())
        val xs = shape.localIntersect(r)
        assertThat(xs).hasSize(count)
    }

    @ParameterizedTest
    @MethodSource("normalVector")
    fun `Computing the normal vector on a cone`(point: Vec4, normal: Vec4) {
        val shape = cone()
        assertThatVec4(shape.normalAt(point, false)).isEqualTo(normal)
    }

    companion object {

        @JvmStatic
        fun intersectCone() = cases(
            args(point(0, 0, -5), vector(0, 0, 1), 5, 5),
            args(point(0, 0, -5), vector(1, 1, 1), 8.66025, 8.66025),
            args(point(1, 1, -5), vector(-0.5, -1, 1), 4.55006, 49.44994),
        )

        @JvmStatic
        fun intersectConeCaps() = cases(
            args(point(0, 0, -5), vector(0, 1, 0), 0),
            args(point(0, 0, -0.25), vector(0, 1, 1), 2),
            args(point(0, 0, -0.25), vector(0, 1, 0), 4)
        )

        @JvmStatic
        fun normalVector() = cases(
            args(point(0, 0, 0), vector(0, 0, 0)),
            args(point(1, 1, 1), vector(1, -sqrt(2.0), 1)),
            args(point(-1, -1, 0), vector(-1, 1, 0))
        )
    }

}