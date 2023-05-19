package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CanvasTest {

    @Test
    fun `Creating a canvas` () {
        val c = Canvas(10, 20)
        assertThat(c)
            .extracting(Canvas::width, Canvas::height)
            .containsExactly(10, 20)

        val black = color(0, 0, 0)
        val allPixelsBlack = c.asSequence().all { it eq black }
        assertThat(allPixelsBlack).isTrue()
    }

    @Test
    fun `Writing pixels to a canvas` () {
        val c = Canvas(10, 20)
        val red = color(1, 0, 0)
        c[2, 3] = red
        assertThatColor(c[2, 3]).isEqualTo(red)
    }

    @Test
    fun `Constructing the PPM pixel data` () {
        val c = Canvas(5, 3)
        c[0, 0] = color(1.5, 0, 0)
        c[2, 1] = color(0, 0.5, 0)
        c[4, 2] = color(-0.5, 0, 1)

        val ppm = c.writePPMToString()
        assertThat(ppm).isEqualTo("""
            P3
            5 3
            255
            255 0 0 0 0 0 0 0 0 0 0 0 0 0 0
            0 0 0 0 0 0 0 128 0 0 0 0 0 0 0
            0 0 0 0 0 0 0 0 0 0 0 0 0 0 255
            
        """.trimIndent())
    }

    @Test
    fun `Splitting long lines in PPM files` () {
        val c = Canvas(10, 2, color(1, 0.8, 0.6))

        val ppm = c.writePPMToString()
        assertThat(ppm).isEqualTo("""
            P3
            10 2
            255
            255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
            153 255 204 153 255 204 153 255 204 153 255 204 153
            255 204 153 255 204 153 255 204 153 255 204 153 255 204 153 255 204
            153 255 204 153 255 204 153 255 204 153 255 204 153
            
        """.trimIndent())
    }
}