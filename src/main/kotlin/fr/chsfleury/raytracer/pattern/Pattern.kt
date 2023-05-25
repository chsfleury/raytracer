package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape

interface Pattern {
    val transform: NDArray

    fun patternAt(point: Vec4): Color

    fun patternAtShape(shape: Shape, point: Vec4): Color {
        val objectPoint = shape.transform.inv * point
        val patternPoint = transform.inv * objectPoint
        return patternAt(patternPoint)
    }
}