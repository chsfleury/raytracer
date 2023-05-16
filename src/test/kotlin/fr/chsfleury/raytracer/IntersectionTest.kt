package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class IntersectionTest {

    @Test
    fun `An intersection encapsulates t and object` () {
        val s = sphere()
        val i = intersection(3.5, s)
        assertThatDouble(i.t).isEqualTo(3.5)
        assertThat(i.obj).isEqualTo(s)
    }

    @Test
    fun `Aggregating intersections` () {
        val s = sphere()
        val i1 = intersection(1, s)
        val i2 = intersection(2, s)
        val xs = intersections(i1, i2)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(1)
        assertThatDouble(xs[1].t).isEqualTo(2)
    }

    @Test
    fun `The hit, when all intersections have positive t` () {
        val s = sphere()
        val i1 = intersection(1, s)
        val i2 = intersection(2, s)
        val xs = intersections(i1, i2)
        val i = Intersection.hit(xs)
        assertThat(i).isEqualTo(i1)
    }

    @Test
    fun `The hit, when some intersections have negative t` () {
        val s = sphere()
        val i1 = intersection(-1, s)
        val i2 = intersection(2, s)
        val xs = intersections(i1, i2)
        val i = Intersection.hit(xs)
        assertThat(i).isEqualTo(i2)
    }

    @Test
    fun `The hit, when all intersections have negative t` () {
        val s = sphere()
        val i1 = intersection(-1, s)
        val i2 = intersection(-2, s)
        val xs = intersections(i1, i2)
        val i = Intersection.hit(xs)
        assertThat(i).isNull()
    }

    @Test
    fun `The hit is always the lowest nonnegative intersection` () {
        val s = sphere()
        val i1 = intersection(5, s)
        val i2 = intersection(7, s)
        val i3 = intersection(-3, s)
        val i4 = intersection(2, s)
        val xs = intersections(i1, i2, i3, i4)
        val i = Intersection.hit(xs)
        assertThat(i).isEqualTo(i4)
    }

}