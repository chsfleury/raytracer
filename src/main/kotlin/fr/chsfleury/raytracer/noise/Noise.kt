package fr.chsfleury.raytracer.noise

import fr.chsfleury.raytracer.linalg.Vec4

interface Noise {

    fun jitter(point: Vec4, scale: Double): Vec4

}