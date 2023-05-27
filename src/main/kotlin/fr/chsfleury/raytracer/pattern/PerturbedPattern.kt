package fr.chsfleury.raytracer.pattern

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.linalg.NDArray
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.noise.Noise

data class PerturbedPattern(
    val pattern: Pattern,
    val noise: Noise,
    val scale: Double,
    override val transform: NDArray
): Pattern {

    override fun colorAt(point: Vec4): Color = pattern.colorAt(noise.jitter(point, scale))
}