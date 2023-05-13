package fr.chsfleury.raytracer.swing

import fr.chsfleury.raytracer.Canvas
import javax.swing.JFrame

class Window(private val canvas: Canvas) {

    fun show() {
        val panel = Panel(canvas)
        JFrame("Raytracer").apply {
            setSize(canvas.width, canvas.height)
            defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            setLocationRelativeTo(null)
            contentPane = panel
            isResizable = false
            isVisible = true
            setSize(canvas.width, canvas.height + insets.top)
        }
    }

}