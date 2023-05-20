package fr.chsfleury.raytracer.shape

import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.material.Material

interface Shape {
    val material: Material
    fun normalAt(worldPoint: Vec4): Vec4
}