package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.args
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.cases
import fr.chsfleury.raytracer.cube
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CubeTest {

    @ParameterizedTest
    @MethodSource("rayIntersectsCube")
    fun `A ray intersects a cube`(origin: Vec4, direction: Vec4, t1: Number, t2: Number) {
        val c = cube()
        val r = ray(origin, direction)
        val xs = c.localIntersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(t1)
        assertThatDouble(xs[1].t).isEqualTo(t2)
    }

    @ParameterizedTest
    @MethodSource("rayMissesCube")
    fun `A ray misses a cube`(origin: Vec4, direction: Vec4) {
        val c = cube()
        val r = ray(origin, direction)
        assertThat(c.localIntersect(r)).isEmpty()
    }

    @ParameterizedTest
    @MethodSource("normalAtSurface")
    fun `The normal on the surface of a cube`(p: Vec4, normal: Vec4) {
        assertThatVec4(cube().localNormalAt(p)).isEqualTo(normal)
    }

    companion object {
        @JvmStatic
        fun rayIntersectsCube() = cases(
            args(point(5, 0.5, 0), vector(-1, 0, 0), 4, 6),
            args(point(-5, 0.5, 0), vector(1, 0, 0), 4, 6),
            args(point(0.5, 5, 0), vector(0, -1, 0), 4, 6),
            args(point(0.5, -5, 0), vector(0, 1, 0), 4, 6),
            args(point(0.5, 0, 5), vector(0, 0, -1), 4, 6),
            args(point(0.5, 0, -5), vector(0, 0, 1), 4, 6),
            args(point(0, 0.5, 0), vector(0, 0, 1), -1, 1),
        )

        @JvmStatic
        fun rayMissesCube() = cases(
            args(point(-2, 0, 0), vector(0.2673, 0.5345, 0.8018)),
            args(point(0, -2, 0), vector(0.8018, 0.2673, 0.5345)),
            args(point(0, 0, -2), vector(0.5345, 0.8018, 0.2673)),
            args(point(2, 0, 2), vector(0, 0, -1)),
            args(point(0, 2, 2), vector(0, -1, 0)),
            args(point(2, 2, 0), vector(-1, 0, 0))
        )

        @JvmStatic
        fun normalAtSurface() = cases(
            args(point(1, 0.5, -0.8), vector(1, 0, 0)),
            args(point(-1, -0.2, 0.9), vector(-1, 0, 0)),
            args(point(-0.4, 1, -0.1), vector(0, 1, 0)),
            args(point(0.3, -1, -0.7), vector(0, -1, 0)),
            args(point(-0.6, 0.3, 1), vector(0, 0, 1)),
            args(point(0.4, 0.4, -1), vector(0, 0, -1)),
            args(point(1, 1, 1), vector(1, 0, 0)),
            args(point(-1, -1, -1), vector(-1, 0, 0))
        )
    }
}