package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.Intersection
import fr.chsfleury.raytracer.Ray
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material

interface Shape {
    val material: Material
    val transform: NDArray
    val invT: NDArray
    val trInvT: NDArray

    fun localIntersect(localRay: Ray): List<Intersection>

    fun intersect(ray: Ray): List<Intersection> {
        val localRay = invT * ray
        return localIntersect(localRay)
    }

    fun localNormalAt(localPoint: Vec4): Vec4

    fun normalAt(point: Vec4): Vec4 {
        val localPoint = invT * point
        val localNormal = localNormalAt(localPoint)
        val worldNormal = trInvT * localNormal
        return worldNormal.toVector().normalize()
    }
}