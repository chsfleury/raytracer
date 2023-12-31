package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import org.junit.jupiter.api.Test

class RayTest {

    @Test
    fun `Creating and querying a ray` () {
        val r = ray(
            point(1, 2, 3),
            vector(4, 5, 6)
        )
        assertThatVec4(r.origin)
            .isEqualTo(point(1, 2, 3))
        assertThatVec4(r.direction)
            .isEqualTo(vector(4, 5, 6))
    }

    @Test
    fun `Computing a point from a distance` () {
        val r = ray(
            point(2, 3, 4),
            vector(1, 0, 0)
        )
        assertThatVec4(r.position(0)).isEqualTo(point(2, 3, 4))
        assertThatVec4(r.position(1)).isEqualTo(point(3, 3, 4))
        assertThatVec4(r.position(-1)).isEqualTo(point(1, 3, 4))
        assertThatVec4(r.position(2.5)).isEqualTo(point(4.5, 3, 4))
    }

    @Test
    fun `Translating a ray` () {
        val r = ray(
            point(1, 2, 3),
            vector(0, 1, 0)
        )
        val m = translation(3, 4, 5)
        val r2 = m * r
        assertThatVec4(r2.origin).isEqualTo(point(4, 6, 8))
        assertThatVec4(r2.direction).isEqualTo(vector(0, 1, 0))
    }

    @Test
    fun `Scaling a ray` () {
        val r = ray(
            point(1, 2, 3),
            vector(0, 1, 0)
        )
        val m = scaling(2, 3, 4)
        val r2 = m * r
        assertThatVec4(r2.origin).isEqualTo(point(2, 6, 12))
        assertThatVec4(r2.direction).isEqualTo(vector(0, 3, 0))
    }
}