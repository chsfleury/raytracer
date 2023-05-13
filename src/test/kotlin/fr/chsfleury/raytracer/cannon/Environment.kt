package fr.chsfleury.raytracer.cannon

import fr.chsfleury.raytracer.linalg.Vec4

data class Environment(
    val gravity: Vec4,
    val wind: Vec4
)