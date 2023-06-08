package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.bounds.Bounds
import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material

interface Shape {
    val material: Material
    val transform: NDArray
    var parent: Group?

    val bounds: Bounds

    fun localIntersect(localRay: Ray): List<Intersection>

    fun intersect(ray: Ray): List<Intersection> {
        val localRay = transform.inv * ray
        return localIntersect(localRay)
    }

    fun localNormalAt(localPoint: Vec4): Vec4

    fun normalAt(point: Vec4, normalize: Boolean = true): Vec4 {
        val localPoint = worldToObject(point)
        val localNormal = localNormalAt(localPoint)
        return normalToWorld(localNormal, normalize)
    }

    fun worldToObject(point: Vec4): Vec4 {
        val p = if (parent != null) {
            parent!!.worldToObject(point)
        } else {
            point
        }
        return transform.inv * p
    }

    fun normalToWorld(normal: Vec4, normalize: Boolean = true): Vec4 {
        var n = (transform.trInv * normal).toVector().let {
            if(normalize) {
                it.normalize()
            } else {
                it
            }
        }
        if (parent != null) {
            n = parent!!.normalToWorld(n)
        }
        return n
    }
}