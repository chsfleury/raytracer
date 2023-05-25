package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.Vec4

interface Pattern {

    fun colorAt(point: Vec4): Color

}