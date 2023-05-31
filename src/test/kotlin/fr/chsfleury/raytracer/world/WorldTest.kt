package fr.chsfleury.raytracer.world

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.assertions.ColorAssert.Companion.assertThatColor
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.color
import fr.chsfleury.raytracer.intersection
import fr.chsfleury.raytracer.intersections
import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.pattern.TestPattern
import fr.chsfleury.raytracer.plane
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.prepareComputations
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import fr.chsfleury.raytracer.world
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test
import kotlin.math.sqrt

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

    @Test
    fun `There is no shadow when nothing is collinear with point and light` () {
        val w = defaultWorld
        val p = point(0, 10, 0)
        assertThat(w.isShadowed(p)).isFalse()
    }

    @Test
    fun `The shadow when an object is between the point and the light` () {
        val w = defaultWorld
        val p = point(10, -10, 10)
        assertThat(w.isShadowed(p)).isTrue()
    }

    @Test
    fun `There is no shadow when an object is behind the light` () {
        val w = defaultWorld
        val p = point(-20, 20, -20)
        assertThat(w.isShadowed(p)).isFalse()
    }

    @Test
    fun `There is no shadow when an object is behind the point` () {
        val w = defaultWorld
        val p = point(-2, 2, -2)
        assertThat(w.isShadowed(p)).isFalse()
    }

    @Test
    fun `shade_hit() is given an intersection in shadow` () {
        val s2 = sphere(
            transform = translation(0, 0, 10)
        )
        val w = world(
            light = pointLight(
                point(0, 0, -10),
                color(1, 1, 1)
            ),
            objects = listOf(
                sphere(),
                s2
            )
        )
        val r = ray(
            point(0, 0, 5),
            vector(0, 0, 1)
        )
        val i = intersection(4, s2)
        val comps = prepareComputations(i, r)
        assertThatColor(w.shadeHit(comps)).isEqualTo(color(0.1, 0.1, 0.1))
    }

    @Test
    fun `The reflected color for a nonreflective material` () {
        val w = defaultWorld.copy(
            objects = listOf(
                s1,
                s2.copy(material = s2.material.copy(ambient = 1.0))
            )
        )
        val r = ray(
            point(0, 0, 0),
            vector(0, 0, 1)
        )
        val shape = w.objects[1]
        val i = intersection(1, shape)
        val comps = prepareComputations(i, r)
        val color = w.reflectedColor(comps)
        assertThatColor(color).isEqualTo(Color.BLACK)
    }

    @Test
    fun `The reflected color for a reflective material` () {
        val shape = plane(
            material(
                reflective = 0.5
            ),
            transform = translation(0, -1, 0)
        )
        val w = defaultWorld.copy(
            objects = listOf(
                s1,
                s2,
                shape
            )
        )
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, -3),
            vector(0, -a, a)
        )
        val i = intersection(sqrt(2.0), shape)
        val comps = prepareComputations(i, r)
        val color = w.reflectedColor(comps)
        assertThatColor(color).isEqualTo(color(0.19032, 0.2379, 0.14274), 0.000017)
    }

    @Test
    fun `shade_hit() with a reflective material` () {
        val shape = plane(
            material(
                reflective = 0.5
            ),
            transform = translation(0, -1, 0)
        )
        val w = defaultWorld.copy(
            objects = listOf(
                s1,
                s2,
                shape
            )
        )
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, -3),
            vector(0, -a, a)
        )
        val i = intersection(sqrt(2.0), shape)
        val comps = prepareComputations(i, r)
        val color = w.shadeHit(comps)
        assertThatColor(color).isEqualTo(color(0.87677, 0.92436, 0.82918), 0.00002)
    }

    @Test
    fun `color_at() with mutually reflective surfaces` () {
        val light = pointLight(
            point(0, 0, 0),
            color(1, 1, 1)
        )
        val lower = plane(
            material = material(
                reflective = 1.0,
            ),
            transform = translation(0, -1, 0)
        )
        val upper = plane(
            material = material(
                reflective = 1.0
            ),
            transform = translation(0, 1, 0)
        )
        val w = world(
            light = light,
            objects = listOf(
                lower,
                upper
            )
        )
        val r = ray(
            point(0, 0, 0),
            vector(0, 1, 0)
        )
        assertDoesNotThrow {
            w.colorAt(r)
        }
    }

    @Test
    fun `The reflected color at the maximum recursive depth` () {
        val shape = plane(
            material = material(
                reflective = 0.5
            ),
            transform = translation(0, -1, 0)
        )
        val w = defaultWorld.copy(
            objects = listOf(
                s1, s2, shape
            )
        )
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, -3),
            vector(0, -a, a)
        )
        val i = intersection(sqrt(2.0), shape)
        val comps = prepareComputations(i, r)
        assertThatColor(w.reflectedColor(comps, 0)).isEqualTo(Color.BLACK)
    }

    @Test
    fun `The refracted color with an opaque surface` () {
        val w = defaultWorld
        val shape = w.objects[0]
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val xs = intersections(
            intersection(4, shape),
            intersection(6, shape)
        )
        val comps = prepareComputations(xs[0], r, xs)
        assertThatColor(w.refractedColor(comps, 5)).isEqualTo(Color.BLACK)
    }

    @Test
    fun `The refracted color at the maximum recursive depth` () {
        val shape = s1.copy(
            material = s1.material.copy(
                transparency = 1.0,
                refractiveIndex = 1.5
            )
        )
        val w = defaultWorld.copy(
            objects = listOf(
                shape, s2
            )
        )
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val xs = intersections(
            intersection(4, shape),
            intersection(6, shape)
        )
        val comps = prepareComputations(xs[0], r, xs)
        assertThatColor(w.refractedColor(comps, 0)).isEqualTo(Color.BLACK)
    }

    @Test
    fun `The refracted color under total internal reflection` () {
        val shape = s1.copy(
            material = s1.material.copy(
                transparency = 1.0,
                refractiveIndex = 1.5
            )
        )
        val w = defaultWorld.copy(
            objects = listOf(
                shape, s2
            )
        )
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, a),
            vector(0, 1, 0)
        )
        val xs = intersections(
            intersection(-a, shape),
            intersection(a, shape)
        )
        // NOTE: this time you're inside the sphere, so you need
        // to look at the second intersection, xs[1], not xs[0]
        val comps = prepareComputations(xs[1], r, xs)
        assertThatColor(w.refractedColor(comps, 5)).isEqualTo(Color.BLACK)
    }

    @Test
    fun `The refracted color with a refracted ray` () {
        val a = s1.copy(
            material = material(
                ambient = 1.0,
                pattern = TestPattern(ID4)
            )
        )
        val b = s2.copy(
            material = material(
                transparency = 1.0,
                refractiveIndex = 1.5
            )
        )
        val w = defaultWorld.copy(
            objects = listOf(a, b)
        )
        val r = ray(
            point(0, 0, 0.1),
            vector(0, 1, 0)
        )
        val xs = intersections(
            intersection(-0.9899, a),
            intersection(-0.4899, b),
            intersection(0.4899, b),
            intersection(0.9899, a)
        )
        val comps = prepareComputations(xs[2], r, xs)
        assertThatColor(w.refractedColor(comps, 5)).isEqualTo(color(0, 0.99888, 0.04725), 0.0001)
    }

    @Test
    fun `shade_hit() with a transparent material` () {
        val floor = plane(
            transform = translation(0, -1, 0),
            material = material(
                transparency = 0.5,
                refractiveIndex = 1.5
            )
        )
        val ball = sphere(
            transform = translation(0, -3.5, -0.5),
            material = material(
                color = color(1, 0, 0),
                ambient = 0.5
            )
        )
        val w = defaultWorld.copy(
            objects = listOf(s1, s2, floor, ball)
        )
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, -3),
            vector(0, -a, a)
        )
        val xs = intersections(
            intersection(sqrt(2.0), floor)
        )
        val comps = prepareComputations(xs[0], r, xs)
        assertThatColor(w.shadeHit(comps, 5)).isEqualTo(color(0.93642, 0.68642, 0.68642))
    }

    @Test
    fun `shade_hit() with a reflective, transparent material` () {
        val floor = plane(
            transform = translation(0, -1, 0),
            material = material(
                reflective = 0.5,
                transparency = 0.5,
                refractiveIndex = 1.5
            )
        )
        val ball = sphere(
            transform = translation(0, -3.5, -0.5),
            material = material(
                color = color(1, 0, 0),
                ambient = 0.5
            )
        )
        val w = defaultWorld.copy(
            objects = listOf(s1, s2, floor, ball)
        )
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, -3),
            vector(0, -a, a)
        )
        val xs = intersections(
            intersection(sqrt(2.0), floor)
        )
        val comps = prepareComputations(xs[0], r, xs)
        assertThatColor(w.shadeHit(comps, 5)).isEqualTo(color(0.93391, 0.69643, 0.69243))
    }
}