package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Doubles.EPSILON
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material
import kotlin.math.abs

data class Plane(
    override val material: Material,
    override val transform: NDArray
): Shape {

    override fun localIntersect(localRay: Ray): List<Intersection> {
        if (abs(localRay.direction.y) < EPSILON) {
            return emptyList()
        }
        val t = -localRay.origin.y / localRay.direction.y
        return listOf(Intersection(t, this))
    }

    override fun localNormalAt(localPoint: Vec4): Vec4 = Vec4.vector(0.0, 1.0, 0.0)
}
