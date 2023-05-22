package fr.chsfleury.raytracer.world

import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.intersection
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.prepareComputations
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.world
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WorldTest {

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
    fun `The default world` () {
        assertThat(defaultWorld.light).isEqualTo(light)
        assertThat(defaultWorld.objects).containsExactly(s1, s2)
    }

    @Test
    fun `Intersect a world with a ray` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val xs = defaultWorld.intersect(r)
        assertThat(xs).hasSize(4)
        assertThatDouble(xs[0].t).isEqualTo(4)
        assertThatDouble(xs[1].t).isEqualTo(4.5)
        assertThatDouble(xs[2].t).isEqualTo(5.5)
        assertThatDouble(xs[3].t).isEqualTo(6)
    }

    @Test
    fun `Shading an intersection` () {
        val w = defaultWorld
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val shape = w.objects[0]
        val i = intersection(4, shape)
        val comps = prepareComputations(i, r)
        assertThatColor(w.shadeHit(comps)).isEqualTo(color(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `Shading an intersection from the inside` () {
        val pointLight = pointLight(point(0, 0.25, 0), color(1, 1, 1))
        val w = world(pointLight, listOf(s1, s2))
        val r = ray(point(0, 0, 0), vector(0, 0, 1))
        val shape = w.objects[1]
        val i = intersection(0.5, shape)
        val comps = prepareComputations(i, r)
        assertThatColor(w.shadeHit(comps)).isEqualTo(color(0.90498, 0.90498, 0.90498))
    }

    @Test
    fun `The color when a ray misses` () {
        val w = defaultWorld
        val r = ray(
            point(0, 0, -5),
            vector(0, 1, 0)
        )
        assertThatColor(w.colorAt(r)).isEqualTo(color(0, 0, 0))
    }

    @Test
    fun `The color when a ray hits` () {
        val w = defaultWorld
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        assertThatColor(w.colorAt(r)).isEqualTo(color(0.38066, 0.47583, 0.2855))
    }

    @Test
    fun `The color with an intersection behind the ray` () {
        val w = defaultWorld.copy(
            objects = listOf(
                s1.copy(material = s1.material.copy(ambient = 1.0)),
                s2.copy(material = s2.material.copy(ambient = 1.0))
            )
        )
        val r = ray(
            point(0, 0, 0.75),
            vector(0, 0, -1)
        )
        assertThatColor(w.colorAt(r)).isEqualTo(s2.material.color)
    }

}