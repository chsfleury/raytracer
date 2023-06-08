package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.AABB
import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.linalg.Vec4.Companion.point
import fr.chsfleury.raytracer.material.Material
import kotlin.math.abs

data class Cube(
    override val material: Material,
    override val transform: NDArray,
    override var parent: Group?
) : Shape {
    override val bounds: Bounds = Bounds(
        point(-1.0, -1.0, -1.0),
        point(1.0, 1.0, 1.0)
    )

    override fun localIntersect(localRay: Ray): List<Intersection> = AABB.intersect(bounds, localRay).map { Intersection(it, this) }

    override fun localNormalAt(localPoint: Vec4): Vec4 {
        val absX = abs(localPoint.x)
        val absY = abs(localPoint.y)
        val absZ = abs(localPoint.z)
        if (absX >= absY) {
            if (absX >= absZ) {
                return Vec4.vector(localPoint.x, 0.0, 0.0)
            }
        } else if (absY >= absZ) {
            return Vec4.vector(0.0, localPoint.y, 0.0)
        }
        return Vec4.vector(0.0, 0.0, localPoint.z)
    }

}
