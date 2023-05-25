package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.intersection
import fr.chsfleury.raytracer.plane
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PlaneTest {

    @Test
    fun `The normal of a plane is constant everywhere` () {
        val p = plane()
        assertThatVec4(p.localNormalAt(point(0, 0, 0))).isEqualTo(vector(0, 1, 0))
        assertThatVec4(p.localNormalAt(point(10, 0, -10))).isEqualTo(vector(0, 1, 0))
        assertThatVec4(p.localNormalAt(point(-5, 0, 150))).isEqualTo(vector(0, 1, 0))
    }

    @Test
    fun `Intersect with a ray parallel to the plane` () {
        val p = plane()
        val r = ray(
            point(0, 10, 0),
            vector(0, 0, 1)
        )
        assertThat(p.localIntersect(r)).isEmpty()
    }

    @Test
    fun `Intersect with a coplanar ray` () {
        val p = plane()
        val r = ray(
            point(0, 0, 0),
            vector(0, 0, 1)
        )
        assertThat(p.localIntersect(r)).isEmpty()
    }

    @Test
    fun `A ray intersecting a plane from above` () {
        val p = plane()
        val r = ray(
            point(0, 1, 0),
            vector(0, -1, 0)
        )
        val xs = p.localIntersect(r)
        assertThat(xs)
            .hasSize(1)
            .element(0)
            .isEqualTo(intersection(1, p))
    }

    @Test
    fun `A ray intersecting a plane from below` () {
        val p = plane()
        val r = ray(
            point(0, -1, 0),
            vector(0, 1, 0)
        )
        val xs = p.localIntersect(r)
        assertThat(xs)
            .hasSize(1)
            .element(0)
            .isEqualTo(intersection(1, p))
    }

}