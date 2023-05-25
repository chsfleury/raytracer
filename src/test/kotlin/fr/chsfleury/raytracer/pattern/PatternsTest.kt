package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.stripePattern
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

}