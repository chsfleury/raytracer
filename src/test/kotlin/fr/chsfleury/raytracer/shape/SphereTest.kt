package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SphereTest {

    @Test
    fun `A ray intersects a sphere at two points` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0]).isEqualTo(4)
        assertThatDouble(xs[1]).isEqualTo(6)
    }

    @Test
    fun `A ray intersects a sphere at a tangent` () {
        val r = ray(
            point(0, 1, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0]).isEqualTo(5)
        assertThatDouble(xs[1]).isEqualTo(5)
    }

    @Test
    fun `A ray misses a sphere` () {
        val r = ray(
            point(0, 2, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).isEmpty()
    }

    @Test
    fun `A ray originates inside a sphere` () {
        val r = ray(
            point(),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0]).isEqualTo(-1)
        assertThatDouble(xs[1]).isEqualTo(1)
    }

    @Test
    fun `A sphere is behind a ray` () {
        val r = ray(
            point(0, 0, 5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0]).isEqualTo(-6)
        assertThatDouble(xs[1]).isEqualTo(-4)
    }
}