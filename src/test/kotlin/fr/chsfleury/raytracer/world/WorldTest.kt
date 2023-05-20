package fr.chsfleury.raytracer.world

import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.world
import org.assertj.core.api.Assertions
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

}