package fr.chsfleury.raytracer.io.wavefront

import fr.chsfleury.raytracer.group
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.shape.Group
import fr.chsfleury.raytracer.shape.Shape

data class WavefrontParser(
    private val vertices: List<Vec4>,
    val shapes: MutableList<Shape>,
    val ignoredLines: Int
) {
    fun vertices(): Int = vertices.size
    fun vertices(index: Int): Vec4 = vertices[index - 1]

    fun toMainGroup() = group(shapes = shapes)

    operator fun get(groupName: String): Group? = shapes.firstOrNull {
        it is Group && it.name == groupName
    } as? Group
}