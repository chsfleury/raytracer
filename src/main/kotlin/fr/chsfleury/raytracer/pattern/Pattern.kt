package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape

interface Pattern {
    val transform: NDArray

    fun colorAt(point: Vec4): Color
    fun colorAtObject(obj: Shape, point: Vec4): Color

}