package fr.chsfleury.raytracer.io.wavefront

import fr.chsfleury.raytracer.io.Scene
import fr.chsfleury.raytracer.shape.Group
import fr.chsfleury.raytracer.world.World

data class WaveFrontScene(
    override val world: World
): Scene
