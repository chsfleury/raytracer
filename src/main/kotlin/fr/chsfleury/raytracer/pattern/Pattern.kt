package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Shape

interface Pattern {
    val transform: NDArray

    fun colorAt(point: Vec4): Color = patternAt(point).let { pattern ->
        pattern.colorAt(pattern.transform.inv * point)
    }

    fun patternAt(point: Vec4): Pattern = this

    fun patternAtShape(shape: Shape, worldPoint: Vec4): Color {
        val objectPoint = shape.transform.inv * worldPoint
        val patternPoint = transform.inv * objectPoint
        return colorAt(patternPoint)
    }
}