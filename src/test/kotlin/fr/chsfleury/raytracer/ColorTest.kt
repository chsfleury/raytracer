package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ColorTest {

    @Test
    fun `Colors are (red, green, blue) tuples` () {
        val c = Color(-0.5, 0.4, 1.7)
        assertThat(c)
            .extracting(Color::r, Color::g, Color::b)
            .containsExactly(-0.5, 0.4, 1.7)
    }

    @Test
    fun `Adding colors` () {
        val c1 = Color(0.9, 0.6, 0.75)
        val c2 = Color(0.7, 0.1, 0.25)
        assertThatColor(c1 + c2)
            .isEqualTo(Color(1.6, 0.7, 1.0))
    }

    @Test
    fun `Subtracting colors` () {
        val c1 = Color(0.9, 0.6, 0.75)
        val c2 = Color(0.7, 0.1, 0.25)
        assertThatColor(c1 - c2)
            .isEqualTo(Color(0.2, 0.5, 0.5))
    }

    @Test
    fun `Multiplying a color by a scalar` () {
        val c = Color(0.2, 0.3, 0.4)
        assertThatColor(c * 2)
            .isEqualTo(Color(0.4, 0.6, 0.8))
    }

    @Test
    fun `Multiplying colors` () {
        val c1 = Color(1, 0.2, 0.4)
        val c2 = Color(0.9, 1, 0.1)
        assertThatColor(c1 * c2)
            .isEqualTo(Color(0.9, 0.2, 0.04))
    }

}