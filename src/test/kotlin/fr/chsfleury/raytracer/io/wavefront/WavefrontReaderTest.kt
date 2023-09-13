package fr.chsfleury.raytracer.io.wavefront

import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.shape.Group
import fr.chsfleury.raytracer.shape.Triangle
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WavefrontReaderTest {

    @Test
    fun `Ignoring unrecognized lines`() {
        val input = """
        There was a young lady named Bright
        who traveled much faster than light.
        She set out one day
        in a relative way,
        and came back the previous night.
        """.trimIndent()

        val parser = WavefrontReader.parse(input)
        assertThat(parser.ignoredLines).isEqualTo(5)
        assertThat(parser.vertices()).isZero()
    }

    @Test
    fun `Vertex records`() {
        val input = """
        v -1 1 0
        v -1.0000 0.5000 0.0000
        v 1 0 0
        v 1 1 0
        v ignored 
        """.trimIndent()

        val parser = WavefrontReader.parse(input)
        assertThat(parser.ignoredLines).isEqualTo(1)
        assertThat(parser.vertices()).isEqualTo(4)
        assertThatVec4(parser.vertices(1)).isEqualTo(point(-1, 1, 0))
        assertThatVec4(parser.vertices(2)).isEqualTo(point(-1, 0.5, 0))
        assertThatVec4(parser.vertices(3)).isEqualTo(point(1, 0, 0))
        assertThatVec4(parser.vertices(4)).isEqualTo(point(1, 1, 0))
    }

    @Test
    fun `Parsing triangle faces`() {
        val input = """
        v -1 1 0
        v -1 0 0
        v 1 0 0
        v 1 1 0
        v ignored 
        f 1 2 3
        f 1 3 4
        """.trimIndent()

        val parser = WavefrontReader.parse(input)
        assertThat(parser.ignoredLines).isEqualTo(1)

        val g = parser.shapes[0] as Group
        val t1 = g[0] as Triangle
        val t2 = g[1] as Triangle
        assertThatVec4(parser.vertices(1)).isEqualTo(t1.p1)
        assertThatVec4(parser.vertices(2)).isEqualTo(t1.p2)
        assertThatVec4(parser.vertices(3)).isEqualTo(t1.p3)
        assertThatVec4(parser.vertices(1)).isEqualTo(t2.p1)
        assertThatVec4(parser.vertices(3)).isEqualTo(t2.p2)
        assertThatVec4(parser.vertices(4)).isEqualTo(t2.p3)
    }

    @Test
    fun `Triangulating polygons`() {
        val input = """
        v -1 1 0
        v -1 0 0
        v 1 0 0
        v 1 1 0
        v 0 2 0
        f 1 2 3 4 5
        """.trimIndent()

        val parser = WavefrontReader.parse(input)

        val g = parser.shapes[0] as Group
        val t1 = g[0] as Triangle
        val t2 = g[1] as Triangle
        val t3 = g[2] as Triangle

        assertThatVec4(parser.vertices(1)).isEqualTo(t1.p1)
        assertThatVec4(parser.vertices(2)).isEqualTo(t1.p2)
        assertThatVec4(parser.vertices(3)).isEqualTo(t1.p3)
        assertThatVec4(parser.vertices(1)).isEqualTo(t2.p1)
        assertThatVec4(parser.vertices(3)).isEqualTo(t2.p2)
        assertThatVec4(parser.vertices(4)).isEqualTo(t2.p3)
        assertThatVec4(parser.vertices(1)).isEqualTo(t3.p1)
        assertThatVec4(parser.vertices(4)).isEqualTo(t3.p2)
        assertThatVec4(parser.vertices(5)).isEqualTo(t3.p3)
    }

    @Test
    fun `Triangle in groups` () {
        val input = """
        v -1 1 0
        v -1 0 0
        v 1 0 0
        v 1 1 0
        g FirstGroup
        f 1 2 3
        g SecondGroup
        f 1 3 4
        """.trimIndent()

        val parser = WavefrontReader.parse(input)

        val g1 = parser["FirstGroup"]!!
        val g2 = parser["SecondGroup"]!!
        val t1 = g1[0] as Triangle
        val t2 = g2[0] as Triangle
        assertThatVec4(parser.vertices(1)).isEqualTo(t1.p1)
        assertThatVec4(parser.vertices(2)).isEqualTo(t1.p2)
        assertThatVec4(parser.vertices(3)).isEqualTo(t1.p3)
        assertThatVec4(parser.vertices(1)).isEqualTo(t2.p1)
        assertThatVec4(parser.vertices(3)).isEqualTo(t2.p2)
        assertThatVec4(parser.vertices(4)).isEqualTo(t2.p3)
    }

}