package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.stripePattern
import fr.chsfleury.raytracer.translation
import org.junit.jupiter.api.Test

class PatternsTest {

    @Test
    fun `Creating a stripe pattern` () {
        val pattern = stripePattern()
        assertThatColor(pattern.colorA).isEqualTo(Color.WHITE)
        assertThatColor(pattern.colorB).isEqualTo(Color.BLACK)
    }

    @Test
    fun `A stripe pattern is constant in y` () {
        val pattern = stripePattern()
        assertThatColor(pattern.stripeAt(point(0, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.stripeAt(point(0, 1, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.stripeAt(point(0, 2, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `A stripe pattern is constant in z` () {
        val pattern = stripePattern()
        assertThatColor(pattern.stripeAt(point(0, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.stripeAt(point(0, 0, 1)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.stripeAt(point(0, 0, 2)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `A stripe pattern alternates in x` () {
        val pattern = stripePattern()
        assertThatColor(pattern.stripeAt(point(0, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.stripeAt(point(0.9, 0, 0)))
            .isEqualTo(Color.WHITE)
        assertThatColor(pattern.stripeAt(point(1, 0, 0)))
            .isEqualTo(Color.BLACK)
        assertThatColor(pattern.stripeAt(point(-0.1, 0, 0)))
            .isEqualTo(Color.BLACK)
        assertThatColor(pattern.stripeAt(point(-1, 0, 0)))
            .isEqualTo(Color.BLACK)
        assertThatColor(pattern.stripeAt(point(-1.1, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `Stripes with an object transformation` () {
        val obj = sphere(transform = scaling(2, 2, 2))
        val pattern = stripePattern()
        assertThatColor(pattern.stripeAtObject(obj, point(1.5, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `Stripes with a pattern transformation` () {
        val obj = sphere()
        val pattern = stripePattern(transform = scaling(2, 2, 2))
        assertThatColor(pattern.stripeAtObject(obj, point(1.5, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

    @Test
    fun `Stripes with both an object and a pattern transformation` () {
        val obj = sphere(transform = scaling(2, 2, 2))
        val pattern = stripePattern(transform = translation(0.5, 0, 0))
        assertThatColor(pattern.stripeAtObject(obj, point(2.5, 0, 0)))
            .isEqualTo(Color.WHITE)
    }

}