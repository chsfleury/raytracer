package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
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

    @Test
    fun `Precomputing the state of an intersection` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val shape = sphere()
        val i = intersection(4, shape)
        val comps = prepareComputations(i, r)
        assertThatDouble(comps.t).isEqualTo(i.t)
        assertThatVec4(comps.point).isEqualTo(point(0, 0, -1))
        assertThatVec4(comps.eyeVector).isEqualTo(vector(0, 0, -1))
        assertThatVec4(comps.normalVector).isEqualTo(vector(0, 0, -1))
    }

    @Test
    fun `The hit, when an intersection occurs on the outside` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val shape = sphere()
        val i = intersection(4, shape)
        val comps = prepareComputations(i, r)
        assertThat(comps.inside).isFalse()
    }

    @Test
    fun `The hit, when an intersection occurs on the inside` () {
        val r = ray(
            point(0, 0, 0),
            vector(0, 0, 1)
        )
        val shape = sphere()
        val i = intersection(1, shape)
        val comps = prepareComputations(i, r)
        assertThatVec4(comps.point).isEqualTo(point(0, 0, 1))
        assertThatVec4(comps.eyeVector).isEqualTo(vector(0, 0, -1))
        assertThatVec4(comps.normalVector).isEqualTo(vector(0, 0, -1))
        assertThat(comps.inside).isTrue()
    }

    @Test
    fun `The hit should offset the point` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val shape = sphere(
            transform = translation(0, 0, 1)
        )
        val i = intersection(5, shape)
        val comps = prepareComputations(i, r)
        assertThat(comps.overPoint.z).isLessThan(-EPSILON / 2)
        assertThat(comps.point.z).isGreaterThan(comps.overPoint.z)
    }
}