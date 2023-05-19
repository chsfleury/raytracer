package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.assertions.DoubleAssert.Companion.assertThatDouble
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.rotationZ
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class SphereTest {

    @Test
    fun `A ray intersects a sphere at two points` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(4)
        assertThatDouble(xs[1].t).isEqualTo(6)
    }

    @Test
    fun `A ray intersects a sphere at a tangent` () {
        val r = ray(
            point(0, 1, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(5)
        assertThatDouble(xs[1].t).isEqualTo(5)
    }

    @Test
    fun `A ray misses a sphere` () {
        val r = ray(
            point(0, 2, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).isEmpty()
    }

    @Test
    fun `A ray originates inside a sphere` () {
        val r = ray(
            point(),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(-1)
        assertThatDouble(xs[1].t).isEqualTo(1)
    }

    @Test
    fun `A sphere is behind a ray` () {
        val r = ray(
            point(0, 0, 5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(-6)
        assertThatDouble(xs[1].t).isEqualTo(-4)
    }

    @Test
    fun `Intersect sets the object on the intersection` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = sphere()
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThat(xs[0].obj).isEqualTo(s)
        assertThat(xs[1].obj).isEqualTo(s)
    }

    @Test
    fun `A sphere's default transformation` () {
        val s = sphere()
        assertThatNDArray(s.transform).isEqualTo(NDArray.ID4)
    }

    @Test
    fun `Changing a sphere's transformation` () {
        val t = translation(2, 3, 4)
        val s = sphere(transform = t)
        assertThatNDArray(s.transform).isEqualTo(t)
    }

    @Test
    fun `Intersecting a scaled sphere with a ray` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = sphere(transform = scaling(2, 2, 2))
        val xs = s.intersect(r)
        assertThat(xs).hasSize(2)
        assertThatDouble(xs[0].t).isEqualTo(3)
        assertThatDouble(xs[1].t).isEqualTo(7)
    }

    @Test
    fun `Intersecting a translated sphere with a ray` () {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = sphere(transform = translation(5, 0, 0))
        val xs = s.intersect(r)
        assertThat(xs).isEmpty()
    }

    @Test
    fun `The normal on a sphere at a point on the x axis` () {
        val s = sphere()
        val n = s.normalAt(point(1, 0, 0))
        assertThatVec4(n).isEqualTo(vector(1, 0, 0))
    }

    @Test
    fun `The normal on a sphere at a point on the y axis` () {
        val s = sphere()
        val n = s.normalAt(point(0, 1, 0))
        assertThatVec4(n).isEqualTo(vector(0, 1, 0))
    }

    @Test
    fun `The normal on a sphere at a point on the z axis` () {
        val s = sphere()
        val n = s.normalAt(point(0, 0, 1))
        assertThatVec4(n).isEqualTo(vector(0, 0, 1))
    }

    @Test
    fun `The normal on a sphere at a nonaxial point` () {
        val a = sqrt(3.0) / 3
        val s = sphere()
        val n = s.normalAt(point(a, a, a))
        assertThatVec4(n).isEqualTo(vector(a, a, a))
    }

    @Test
    fun `The normal is a normalized vector` () {
        val a = sqrt(3.0) / 3
        val s = sphere()
        val n = s.normalAt(point(a, a, a))
        assertThatVec4(n.normalize()).isEqualTo(n)
    }

    @Test
    fun `Computing the normal on a translated sphere` () {
        val s = sphere(transform = translation(0, 1, 0))
        val n = s.normalAt(point(0, 1.70711, -0.70711))
        assertThatVec4(n).isEqualTo(vector(0, 0.70711, -0.70711))
    }

    @Test
    fun `Computing the normal on a transformed sphere` () {
        val a = sqrt(2.0) / 2
        val s = sphere(transform = scaling(1, 0.5, 1) * rotationZ(PI / 5))
        val n = s.normalAt(point(0, a, -a))
        assertThatVec4(n).isEqualTo(vector(0, 0.97014, -0.24254))
    }

    @Test
    fun `A sphere has a default material` () {
        val s = sphere()
        assertThat(s.material).isEqualTo(material())
    }

    @Test
    fun `A sphere may be assigned a material` () {
        val m = material(1.0)
        val s = sphere(material = m)
        assertThat(s.material).isEqualTo(m)
    }
}