package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material

interface Shape {
    val material: Material
    val transform: NDArray
    var parent: Group?

    fun localIntersect(localRay: Ray): List<Intersection>

    fun intersect(ray: Ray): List<Intersection> {
        val localRay = transform.inv * ray
        return localIntersect(localRay)
    }

    fun localNormalAt(localPoint: Vec4): Vec4

    fun normalAt(point: Vec4, normalize: Boolean = true): Vec4 {
        val localPoint = transform.inv * point
        val localNormal = localNormalAt(localPoint)
        val worldNormal = transform.trInv * localNormal
        return worldNormal.toVector().let {
            if(normalize) {
                it.normalize()
            } else {
                it
            }
        }
    }
}