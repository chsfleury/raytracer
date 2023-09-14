package fr.chsfleury.raytracer.io

import fr.chsfleury.raytracer.light.PointLight
import java.nio.file.Path

interface Reader<S: Scene> {

    fun read(filePath: Path, pointLight: PointLight): S
    fun read(lines: String, pointLight: PointLight): S

}