package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.NDArray.Companion.ID4
import fr.chsfleury.raytracer.linalg.Vec4

data class SolidPattern(
    val color: Color,
): Pattern {
    override val transform: NDArray = ID4

    override fun colorAt(point: Vec4): Color = color

    companion object {
        val BLACK_PATTERN = SolidPattern(Color.BLACK)
        val WHITE_PATTERN = SolidPattern(Color.WHITE)
    }
}
