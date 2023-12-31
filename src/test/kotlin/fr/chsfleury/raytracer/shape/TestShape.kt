package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material
import fr.chsfleury.raytracer.material.Material

data class TestShape(
    override val material: Material = material(),
    override val transform: NDArray = NDArray.ID4,
    override var parent: Group?
) : Shape {

    lateinit var savedRay: Ray
    override val bounds: Bounds = Bounds(
        Vec4.point(-1.0, -1.0, -1.0),
        Vec4.point(1.0, 1.0, 1.0)
    )

    override fun localIntersect(localRay: Ray): List<Intersection> {
        savedRay = localRay
        return emptyList()
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        return localPoint.toVector()
    }
}