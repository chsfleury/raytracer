package fr.chsfleury.raytracer.io

import java.nio.file.Path

interface Reader<S: Scene> {

    fun read(filePath: Path): S
    fun read(lines: String): S

}