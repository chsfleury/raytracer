package fr.chsfleury.raytracer.material

import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.vector
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

class MaterialTest {

    val m = material()
    val position = point(0, 0, 0)

    @Test
    fun `The default material` () {
        val material = material()
        assertThatColor(material.color).isEqualTo(color(1, 1, 1))
        assertThatDouble(material.ambient).isEqualTo(0.1)
        assertThatDouble(material.diffuse).isEqualTo(0.9)
        assertThatDouble(material.specular).isEqualTo(0.9)
        assertThatDouble(material.shininess).isEqualTo(200.0)
    }

    @Test
    fun `Lighting with the eye between the light and the surface` () {
        val eyev = vector(0, 0, -1)
        val normalv = vector(0, 0, -1)
        val light = pointLight(
            point(0, 0, -10),
            color(1, 1, 1)
        )
        val result = m.lighting(sphere(), light, position, eyev, normalv, false)
        assertThatColor(result).isEqualTo(color(1.9, 1.9, 1.9))
    }

    @Test
    fun `Lighting with the eye between light and surface, eye offset 45 degrees` () {
        val a = sqrt(2.0) / 2
        val eyev = vector(0, a, -a)
        val normalv = vector(0, 0, -1)
        val light = pointLight(
            point(0, 0, -10),
            color(1, 1, 1)
        )
        val result = m.lighting(sphere(), light, position, eyev, normalv, false)
        assertThatColor(result).isEqualTo(color(1.0, 1.0, 1.0))
    }

    @Test
    fun `Lighting with eye opposite surface, light offset 45 degrees` () {
        val eyev = vector(0, 0, -1)
        val normalv = vector(0, 0, -1)
        val light = pointLight(
            point(0, 10, -10),
            color(1, 1, 1)
        )
        val result = m.lighting(sphere(), light, position, eyev, normalv, false)
        assertThatColor(result).isEqualTo(color(0.7364, 0.7364, 0.7364))
    }

    @Test
    fun `Lighting with eye in the path of the reflection vector` () {
        val a = sqrt(2.0) / 2
        val eyev = vector(0, -a, -a)
        val normalv = vector(0, 0, -1)
        val light = pointLight(
            point(0, 10, -10),
            color(1, 1, 1)
        )
        val result = m.lighting(sphere(), light, position, eyev, normalv, false)
        assertThatColor(result).isEqualTo(color(1.6364, 1.6364, 1.6364))
    }

    @Test
    fun `Lighting with the light behind the surface` () {
        val eyev = vector(0, 0, -1)
        val normalv = vector(0, 0, -1)
        val light = pointLight(
            point(0, 0, 10),
            color(1, 1, 1)
        )
        val result = m.lighting(sphere(), light, position, eyev, normalv, false)
        assertThatColor(result).isEqualTo(color(0.1, 0.1, 0.1))
    }

    @Test
    fun `Lighting with the surface in shadow` () {
        val eyeV = vector(0, 0, -1)
        val normalV = vector(0, 0, -1)
        val light = pointLight(
            point(0, 0, -10),
            color(1, 1, 1)
        )
        val inShadow = true
        assertThatColor(m.lighting(sphere(), light, position, eyeV, normalV, inShadow)).isEqualTo(color(0.1, 0.1, 0.1))
    }

    @Test
    fun `Reflectivity for the default material` () {
        assertThatDouble(material().reflective).isEqualTo(0.0)
    }
}