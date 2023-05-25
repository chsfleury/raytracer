package fr.chsfleury.raytracer

import fr.chsfleury.raytracer.Doubles.scaleTo255Int
import java.io.ByteArrayOutputStream
import java.io.OutputStream

data class Canvas(
    val width: Int,
    val height: Int,
    val bgColor: Color = Color(0.0, 0.0, 0.0)
) {
    private val xRange = 0..width
    private val yRange = 0..height

    private val pixels: Array<Array<Color>> = Array(height) {
        Array(width) {
            bgColor
        }
    }

    operator fun get(x: Int, y: Int): Color {
        if (x !in xRange || y !in yRange) {
            error("invalid pixel")
        }
        return pixels[y][x]
    }

    operator fun get(p: Pixel): Color = get(p.x, p.y)

    operator fun set(x: Int, y: Int, c: Color) {
        if (x !in xRange || y !in yRange) {
            error("invalid pixel ($x,$y)")
        }
        pixels[y][x] = c
    }

    operator fun set(p: Pixel, c: Color) = set(p.x, p.y, c)

    operator fun contains(p: Pixel): Boolean = p.x in xRange && p.y in yRange

    fun asSequence(): Sequence<Color> = pixels.asSequence().flatMap { it.asSequence() }

    fun forEach(consumer: (Triple<Int, Int, Color>) -> Unit) {
        pixels.forEachIndexed { y, row ->
            row.forEachIndexed { x, color ->
                consumer(Triple(x, y, color))
            }
        }
    }

    fun writePPMToString(): String {
        val outputStream = ByteArrayOutputStream()
        writePPMTo(outputStream)
        return outputStream.toString(Charsets.UTF_8)
    }

    fun writePPMTo(outputStream: OutputStream) {
        outputStream.bufferedWriter().use { writer ->
            writer.write("P3\n$width $height\n255\n")

            val rowContent = StringBuilder(PPM_LIMIT)

            pixels.forEach { row ->

                row.asSequence().flatMap(::getPPMColor).forEach {
                    if (rowContent.length + it.length + 1 > PPM_LIMIT) {
                        writer.write(rowContent.toString())
                        writer.write("\n")
                        rowContent.clear()
                    }

                    if (rowContent.isNotEmpty()) {
                        rowContent.append(" ")
                    }
                    rowContent.append(it)
                }

                if (rowContent.isNotEmpty()) {
                    writer.write(rowContent.append("\n").toString())
                    rowContent.clear()
                }
            }
        }
    }

    private fun getPPMColor(color: Color): Sequence<String> {
        return arrayOf(
            scaleTo255Int(color.r).toString(),
            scaleTo255Int(color.g).toString(),
            scaleTo255Int(color.b).toString()
        ).asSequence()
    }



    companion object {
        const val PPM_LIMIT = 70
    }
}
