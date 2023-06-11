package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.triangle
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TriangleTest {

    @Test
    fun `Constructing a triangle` () {
        val p1 = point(0, 1, 0)
        val p2 = point(-1, 0, 0)
        val p3 = point(1, 0, 0)
        val t = triangle(p1, p2, p3)
        assertThatVec4(t.p1).isEqualTo(p1)
        assertThatVec4(t.p2).isEqualTo(p2)
        assertThatVec4(t.p3).isEqualTo(p3)
        assertThatVec4(t.e1).isEqualTo(vector(-1, -1, 0))
        assertThatVec4(t.e2).isEqualTo(vector(1, -1, 0))
        assertThatVec4(t.normal).isEqualTo(vector(0, 0, -1))
    }

    @Test
    fun `Finding the normal on a triangle` () {
        val t = triangle(
            point(0, 1, 0),
            point(-1, 0, 0),
            point(1, 0, 0)
        )
        val n1 = t.localNormalAt(point(0, 0.5, 0))
        val n2 = t.localNormalAt(point(-0.5, 0.75, 0))
        val n3 = t.localNormalAt(point(0.5, 0.25, 0))
        assertThatVec4(n1).isEqualTo(t.normal)
        assertThatVec4(n2).isEqualTo(t.normal)
        assertThatVec4(n3).isEqualTo(t.normal)
    }

    @Test
    fun `Intersecting a ray parallel to the triangle` () {
        val t = triangle(
            point(0, 1, 0),
            point(-1, 0, 0),
            point(1, 0, 0)
        )
        val r = ray(
            point(0, -1, -2),
            vector(0, 1, 0)
        )
        assertThat(t.localIntersect(r)).isEmpty()
    }

    @Test
    fun `A ray misses the p1-p3 edge` () {
        val t = triangle(
            point(0, 1, 0),
            point(-1, 0, 0),
            point(1, 0, 0)
        )
        val r = ray(
            point(1, 1, -2),
            vector(0, 0, 1)
        )
        assertThat(t.localIntersect(r)).isEmpty()
    }

    @Test
    fun `A ray misses the p1-p2 edge` () {
        val t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = ray(point(-1, 1, -2), vector(0, 0, 1))
        assertThat(t.localIntersect(r)).isEmpty()
    }

    @Test
    fun `A ray misses the p2-p3 edge` () {
        val t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = ray(point(0, -1, -2), vector(0, 0, 1))
        assertThat(t.localIntersect(r)).isEmpty()
    }

    @Test
    fun `A ray strikes a triangle` () {
        val t = triangle(point(0, 1, 0), point(-1, 0, 0), point(1, 0, 0))
        val r = ray(point(0, 0.5, -2), vector(0, 0, 1))
        val xs = t.localIntersect(r)
        assertThat(xs).hasSize(1)
        assertThatDouble(xs[0].t).isEqualTo(2)
    }
}