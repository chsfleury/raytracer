package fr.chsfleury.raytracer.swing

import fr.chsfleury.raytracer.Canvas
import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Doubles.scaleTo255Int
import java.awt.Graphics
import java.awt.image.BufferedImage
import javax.swing.JPanel

class Panel(
    private val canvas: Canvas
): JPanel() {
    private val image = BufferedImage(canvas.width + 1, canvas.height + 1, BufferedImage.TYPE_INT_ARGB).apply {
        canvas.forEach { (x, y, c) ->
            setRGB(x, y, toAwtColor(c).rgb)
        }
    }

    override fun paintComponent(g: Graphics) {
        g.drawImage(image, 0, 0, null)
    }

    private fun toAwtColor(color: Color): java.awt.Color {
        return java.awt.Color(scaleTo255Int(color.r), scaleTo255Int(color.g), scaleTo255Int(color.b))
    }
}