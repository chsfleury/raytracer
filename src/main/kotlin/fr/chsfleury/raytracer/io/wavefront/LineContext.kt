package fr.chsfleury.raytracer.io.wavefront

import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Group
import fr.chsfleury.raytracer.shape.Shape

data class LineContext(
    val vertices: MutableList<Vec4> = mutableListOf(),
    val objects: MutableList<Shape> = mutableListOf(),
    val groups: MutableList<Shape> = mutableListOf(),
    var currentGroup: Group? = null
)
