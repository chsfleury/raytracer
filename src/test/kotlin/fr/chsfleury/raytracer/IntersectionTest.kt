package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.math.sqrt

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

    @Test
    fun `Precomputing the reflection vector` () {
        val a = sqrt(2.0) / 2
        val shape = plane()
        val r = ray(
            point(0, 1, -1),
            vector(0, -a, a)
        )
        val i = intersection(sqrt(2.0), shape)
        val comps = prepareComputations(i, r)
        assertThatVec4(comps.reflectVector).isEqualTo(vector(0, a, a))
    }

    @ParameterizedTest
    @CsvSource(
        "0, 1.0, 1.5",
        "1, 1.5, 2.0",
        "2, 2.0, 2.5",
        "3, 2.5, 2.5",
        "4, 2.5, 1.5",
        "5, 1.5, 1.0"
    )
    fun `Finding n1 and n2 at various intersections` (index: Int, n1: Double, n2: Double) {
        val a = glassSphere(
            material = material(
                refractiveIndex = 1.5
            ),
            transform = scaling(2, 2, 2)
        )
        val b = glassSphere(
            material = material(
                refractiveIndex = 2.0
            ),
            transform = translation(0, 0, -0.25)
        )
        val c = glassSphere(
            material = material(
                refractiveIndex = 2.5
            ),
            transform = translation(0, 0, 0.25)
        )
        val r = ray(
            point(0, 0, -4),
            vector(0, 0, 1)
        )
        val xs = intersections(
            intersection(2, a),
            intersection(2.75, b),
            intersection(3.25, c),
            intersection(4.75, b),
            intersection(5.25, c),
            intersection(6, a)
        )
        val comps = prepareComputations(xs[index], r, xs)
        assertThatDouble(comps.n1).isEqualTo(n1)
        assertThatDouble(comps.n2).isEqualTo(n2)
    }

    @Test
    fun `The under point is offset below the surface` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val shape = glassSphere(transform = translation(0, 0, 1))
        val i = intersection(5, shape)
        val xs = intersections(i)
        val comps = prepareComputations(i, r, xs)
        assertThat(comps.underPoint.z).isGreaterThan(EPSILON / 2)
        assertThat(comps.point.z).isLessThan(comps.underPoint.z)
    }

    @Test
    fun `The Schlick approximation under total internal reflection` () {
        val shape = glassSphere()
        val a = sqrt(2.0) / 2
        val r = ray(
            point(0, 0, a),
            vector(0, 1, 0)
        )
        val xs = intersections(
            intersection(-a, shape),
            intersection(a, shape)
        )
        val comps = prepareComputations(xs[1], r, xs)
        assertThatDouble(comps.schlick()).isEqualTo(1.0)
    }

    @Test
    fun `The Schlick approximation with a perpendicular viewing angle` () {
        val shape = glassSphere()
        val r = ray (
            point(0, 0, 0),
            vector(0, 1, 0)
        )
        val xs = intersections(
            intersection(-1, shape),
            intersection(1, shape)
        )
        val comps = prepareComputations(xs[1], r, xs)
        assertThatDouble(comps.schlick()).isEqualTo(0.04)
    }

    @Test
    fun `The Schlick approximation with small angle and n2 gt n1` () {
        val shape = glassSphere()
        val r = ray(
            point(0, 0.99, -2),
            vector(0, 0, 1)
        )
        val xs = intersections(
            intersection(1.8589, shape)
        )
        val comps = prepareComputations(xs[0], r, xs)
        assertThatDouble(comps.schlick()).isEqualTo(0.48873)
    }
}