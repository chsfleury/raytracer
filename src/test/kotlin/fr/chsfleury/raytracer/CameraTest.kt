package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class CameraTest {

    private val light = pointLight(
        point(-10, 10, -10),
        color(1, 1, 1)
    )

    private val s1 = sphere(
        material = material(
            color = color(0.8, 1.0, 0.6),
            diffuse = 0.7,
            specular = 0.2
        )
    )

    private val s2 = sphere(
        transform = scaling(0.5, 0.5, 0.5)
    )

    private val defaultWorld = world(light, listOf(s1, s2))

    @Test
    fun `Constructing a camera` () {
        val c = camera(160, 120, PI / 2)
        assertThat(c.hSize).isEqualTo(160)
        assertThat(c.vSize).isEqualTo(120)
        assertThatDouble(c.fieldOfView).isEqualTo(PI / 2)
        assertThatNDArray(c.transform).isIdentity()
    }

    @Test
    fun `The pixel size for a horizontal canvas` () {
        val c = camera(200, 125)
        assertThatDouble(c.pixelSize).isEqualTo(0.01)
    }

    @Test
    fun `The pixel size for a vertical canvas` () {
        val c = camera(125, 200)
        assertThatDouble(c.pixelSize).isEqualTo(0.01)
    }

    @Test
    fun `Constructing a ray through the center of the canvas` () {
        val c = camera(201, 101, PI / 2)
        val r = c.rayForPixel(100, 50)
        assertThatVec4(r.origin).isEqualTo(point(0, 0, 0))
        assertThatVec4(r.direction).isEqualTo(vector(0, 0, -1))
    }

    @Test
    fun `Constructing a ray through a corner of the canvas` () {
        val c = camera(201, 101)
        val r = c.rayForPixel(0, 0)
        assertThatVec4(r.origin).isEqualTo(point(0, 0, 0))
        assertThatVec4(r.direction).isEqualTo(vector(0.66519, 0.33259, -0.66851))
    }

    @Test
    fun `Constructing a ray when the camera is transformed` () {
        val a = sqrt(2.0) / 2
        val c = camera(
            201,
            101,
            PI / 2,
            rotationY(PI / 4) * translation(0, -2, 5)
        )
        val r = c.rayForPixel(100, 50)
        assertThatVec4(r.origin).isEqualTo(point(0, 2, -5))
        assertThatVec4(r.direction).isEqualTo(vector(a, 0, -a))
    }

    @Test
    fun `Rendering a world with a camera` () {
        val w = defaultWorld
        val from = point(0, 0, -5)
        val to = point(0, 0, 0)
        val up = vector(0, 1, 0)
        val c = camera(
            11,
            11,
            PI / 2,
            transform = viewTransform(from, to, up)
        )
        val image = c.render(w)
        assertThatColor(image[5, 5]).isEqualTo(color(0.38066, 0.47583, 0.2855))
    }
}