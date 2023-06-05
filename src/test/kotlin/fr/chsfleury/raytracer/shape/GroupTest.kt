package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.group
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.material.Material
import fr.chsfleury.raytracer.point
import fr.chsfleury.raytracer.ray
import fr.chsfleury.raytracer.scaling
import fr.chsfleury.raytracer.sphere
import fr.chsfleury.raytracer.translation
import fr.chsfleury.raytracer.vector
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test

class GroupTest {

    @Test
    fun `Creating a new group` () {
        val g = group()
        assertThat(g.isEmpty()).isTrue()
        assertThatNDArray(g.transform).isIdentity()
    }

    @Test
    fun `Adding a child to a group` () {
        val s = testShape()
        val g = group(listOf(s))
        assertThat(g.shapes).isNotEmpty
        assertThat(s.parent).isEqualTo(g)
    }

    @Test
    fun `Intersecting a ray with an empty group` () {
        val g = group()
        val r = ray(
            point(0, 0, 0),
            vector(0, 0, 1)
        )
        assertThat(g.localIntersect(r)).isEmpty()
    }

    @Test
    fun `Intersecting a ray with a nonempty group` () {
        val s1 = sphere()
        val s2 = sphere(
            transform = translation(0, 0, -3)
        )
        val s3 = sphere(
            transform = translation(5, 0, 0)
        )
        val g = group(
            shapes = listOf(s1, s2, s3)
        )
        val r = ray(
            point(0, 0, -5),
            vector(0, 0, 1)
        )
        assertThat(g.localIntersect(r))
            .hasSize(4)
            .extracting(Intersection::obj)
            .containsExactly(tuple(s2), tuple(s2), tuple(s1), tuple(s1))
    }

    @Test
    fun `Intersecting a transformed group` () {
        val s = sphere(
            transform = translation(5, 0, 0)
        )
        val g = group(
            shapes = listOf(s),
            transform = scaling(2, 2, 2)
        )
        val r = ray(
            point(10, 0, -10),
            vector(0, 0, 1)
        )
        assertThat(g.intersect(r)).hasSize(2)
    }

    fun testShape(
        material: Material = material(),
        transform: NDArray = NDArray.ID4,
        parent: Group? = null
    ): TestShape = TestShape(material, transform, parent)

}