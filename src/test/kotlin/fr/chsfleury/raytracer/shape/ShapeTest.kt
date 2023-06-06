package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.assertions.Vec4Assert.Companion.assertThatVec4
import fr.chsfleury.raytracer.group
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.material.Material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.rotationY
import fr.chsfleury.raytracer.rotationZ
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.math.sqrt

class ShapeTest {

    @Test
    fun `The default transformation`() {
        val s = testShape()
        assertThatNDArray(s.transform).isIdentity()
    }

    @Test
    fun `Assigning a transformation` () {
        val s = testShape(transform = translation(2, 3, 4))
        assertThatNDArray(s.transform).isEqualTo(translation(2, 3, 4))
    }

    @Test
    fun `The default material` () {
        val s = testShape()
        assertThat(s.material).isEqualTo(material())
    }

    @Test
    fun `Assigning a material` () {
        val m = material(ambient = 1.0)
        val s = testShape(material = m)
        assertThat(s.material).isEqualTo(m)
    }

    @Test
    fun `Intersecting a scaled shape with a ray`() {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = testShape(transform = scaling(2, 2, 2))
        s.intersect(r)
        assertThatVec4(s.savedRay.origin).isEqualTo(point(0, 0, -2.5))
        assertThatVec4(s.savedRay.direction).isEqualTo(vector(0, 0, 0.5))
    }

    @Test
    fun `Intersecting a translated shape with a ray`() {
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        val s = testShape(transform = translation(5, 0, 0))
        s.intersect(r)
        assertThatVec4(s.savedRay.origin).isEqualTo(point(-5, 0, -5))
        assertThatVec4(s.savedRay.direction).isEqualTo(vector(0, 0, 1))
    }

    @Test
    fun `Computing the normal on a translated shape` () {
        val s = testShape(transform = translation(0, 1, 0))
        assertThatVec4(s.normalAt(point(0, 1.70711, -0.70711)))
            .isEqualTo(vector(0, 0.70711, -0.70711))
    }

    @Test
    fun `Computing the normal on a transformed shape` () {
        val a = sqrt(2.0) / 2
        val s = testShape(transform = scaling(1, 0.5, 1) * rotationZ(PI / 5))
        assertThatVec4(s.normalAt(point(0, a, -a)))
            .isEqualTo(vector(0, 0.97014, -0.24254))
    }

    @Test
    fun `A shape has a parent attribute` () {
        val s = testShape()
        assertThat(s.parent).isNull()
    }

    @Test
    fun `Converting a point from world to object space` () {
        val s = sphere(
            transform = translation(5, 0, 0)
        )
        val g2 = group(
            shapes = listOf(s),
            transform = scaling(2, 2, 2)
        )
        val g1 = group(
            shapes = listOf(g2),
            transform = rotationY(PI / 2)
        )
        assertThatVec4(s.worldToObject(point(-2, 0, -10))).isEqualTo(point(0, 0, -1))
    }

    @Test
    fun `Converting a normal from object to world space` () {
        val s = sphere(
            transform = translation(5, 0, 0)
        )
        val g2 = group(
            shapes = listOf(s),
            transform = scaling(1, 2, 3)
        )
        val g1 = group(
            shapes = listOf(g2),
            transform = rotationY(PI / 2)
        )
        val a = sqrt(3.0) / 3
        assertThatVec4(s.normalToWorld(vector(a, a, a))).isEqualTo(vector(0.2857, 0.4286, -0.8571), 0.0001)
    }

    fun testShape(
        material: Material = material(),
        transform: NDArray = ID4,
        parent: Group? = null
    ): TestShape = TestShape(material, transform, parent)

}