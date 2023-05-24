package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.assertions.NDArrayAssert.Companion.assertThatNDArray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.material.Material
import org.junit.jupiter.api.Test

class ShapeTest {

    @Test
    fun `The default transformation` () {
        val s = testShape()
        assertThatNDArray(s.transform).isIdentity()
    }

    fun testShape(): Shape = DummyShape()

    class DummyShape(override val material: Material = material(), override val transform: NDArray = ID4) : Shape {
        override fun intersect(ray: Ray): List<Intersection> {
            TODO("Not yet implemented")
        }

        override fun normalAt(worldPoint: Vec4): Vec4 {
            TODO("Not yet implemented")
        }
    }
}