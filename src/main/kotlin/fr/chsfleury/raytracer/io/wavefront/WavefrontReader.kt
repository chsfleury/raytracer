package fr.chsfleury.raytracer.io.wavefront

import fr.chsfleury.raytracer.group
import fr.chsfleury.raytracer.io.Reader
import fr.chsfleury.raytracer.light.PointLight
import fr.chsfleury.raytracer.linalg.Vec4
import fr.chsfleury.raytracer.pointLight
import fr.chsfleury.raytracer.shape.Group
import fr.chsfleury.raytracer.shape.Shape
import fr.chsfleury.raytracer.triangle
import fr.chsfleury.raytracer.world.World
import java.lang.Exception
import java.nio.file.Path
import kotlin.io.path.useLines

object WavefrontReader : Reader<WaveFrontScene> {

    private val BLANK_SPACE_REGEX = Regex("\\s+")
    private const val VERTEX_COMMAND = "v"
    private const val FACE_COMMAND = "f"
    private const val GROUP_COMMAND = "g"

    override fun read(filePath: Path, pointLight: PointLight): WaveFrontScene = createScene(filePath.useLines(Charsets.UTF_8, this::readLines), pointLight)

    override fun read(lines: String, pointLight: PointLight): WaveFrontScene = createScene(parse(lines), pointLight)

    fun parse(lines: String): WavefrontParser = readLines(lines.lineSequence())

    private fun createScene(parser: WavefrontParser, pointLight: PointLight): WaveFrontScene {
        val world = World(pointLight, listOf(parser.toMainGroup()))
        return WaveFrontScene(world)
    }

    private fun readLines(lines: Sequence<String>): WavefrontParser {
        val lineContext = LineContext()


        var ignoredLines = 0

        lines.forEach {
            val line = it.trim()
            if (line.isNotEmpty()) {
                if (!parseLine(line, lineContext)) {
                    ignoredLines++
                }
            }
        }

        return WavefrontParser(
            lineContext.vertices,
            lineContext.groups,
            ignoredLines
        )
    }

    private fun parseLine(line: String, lineContext: LineContext): Boolean {
        val (vertices, objects, groups, currentGroup) = lineContext
        val tokenIterator = BLANK_SPACE_REGEX.splitToSequence(line).iterator()
        return when (tokenIterator.next()) {
            VERTEX_COMMAND -> parseVertex(tokenIterator, line)?.run(vertices::add) ?: false
            FACE_COMMAND -> parseFaces(tokenIterator, vertices, line)?.run {
                objects.addAll(this)
                val group = currentGroup ?: group(name = "default").also {
                    groups.add(it)
                    lineContext.currentGroup = it
                }
                group.addAll(this)
                true
            } ?: false
            GROUP_COMMAND -> parseGroup(tokenIterator, line)?.run {
                lineContext.currentGroup = findGroup(groups, name)
                    ?: this.also {
                        groups.add(it)
                    }
                true
            } ?: false
            else -> false
        }
    }

    private fun findGroup(groups: List<Shape>, name: String?): Group? = groups.asSequence()
            .mapNotNull { shape ->
                shape.takeIf { it is Group } as? Group
            }
            .firstOrNull { it.name == name }

    private fun parseVertex(iterator: Iterator<String>, line: String): Vec4? {
        return try {
            val x = iterator.next().toDouble()
            val y = iterator.next().toDouble()
            val z = iterator.next().toDouble()
            Vec4.point(x, y, z)
        } catch (ex: Exception) {
            println("invalid vertex line: ${ex.message} in '$line'")
            null
        }
    }

    private fun parseFaces(iterator: Iterator<String>, vertices: List<Vec4>, line: String): List<Shape>? {
        val vs = mutableListOf<Vec4>()
        try {
            while (iterator.hasNext()) {
                vs.add(vertices[iterator.next().toInt() - 1])
            }
        } catch (ex: Exception) {
            println("invalid face line: ${ex.message} in '$line'")
            return null
        }

        val n = vs.size
        return if (n < 3) {
            listOf()
        } else if (n == 3) {
            listOf(triangle(vs[0], vs[1], vs[2]))
        } else {
            val triangles = mutableListOf<Shape>()
            for (index in 1 until n - 1) {
                val triangle = triangle(vs[0], vs[index], vs[index + 1])
                triangles += triangle
            }
            triangles
        }
    }

    private fun parseGroup(iterator: Iterator<String>, line: String): Group? {
        val name = try {
            iterator.next()
        } catch (ex: Exception) {
            println("invalid group line: ${ex.message} in '$line'")
            return null
        }
        return group(name = name)
    }

}