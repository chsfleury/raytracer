package fr.chsfleury.raytracer.light

import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import org.junit.jupiter.api.Test

class PointLightTest {

    @Test
    fun `A point light has a position and intensity` () {
        val intensity = color(1, 1, 1)
        val position = point(0, 0, 0)
        val light = pointLight(position, intensity)
        assertThatVec4(light.position).isEqualTo(position)
        assertThatColor(light.intensity).isEqualTo(intensity)
    }

}