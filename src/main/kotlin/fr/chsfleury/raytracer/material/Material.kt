package fr.chsfleury.raytracer.material

import fr.chsfleury.raytracer.Color
import fr.chsfleury.raytracer.Doubles.NORMALIZED
import fr.chsfleury.raytracer.light.PointLight
import fr.chsfleury.raytracer.linalg.Vec4
import kotlin.math.pow

data class Material(
    val color: Color,
    val ambient: Double,
    val diffuse: Double,
    val specular: Double,
    val shininess: Double
) {
    init {
        if (ambient !in NORMALIZED) {
            error("ambient must be between 0 and 1")
        }

        if (diffuse !in NORMALIZED) {
            error("diffuse must be between 0 and 1")
        }

        if (specular !in NORMALIZED) {
            error("specular must be between 0 and 1")
        }

        if (shininess !in 10.0..200.0) {
            error("shininess must be between 10 and 200")
        }
    }

    fun lighting(light: PointLight, point: Vec4, eyeVector: Vec4, normalVector: Vec4, inShadow: Boolean): Color {
        val effectiveColor = color * light.intensity
        val lightv = (light.position - point).normalize()
        val currentAmbient = effectiveColor * ambient

        val lightDotNormal = lightv dot normalVector
        val (currentDiffuse, currentSpecular) = if (lightDotNormal < 0 || inShadow) {
            Pair(Color.BLACK, Color.BLACK)
        } else {
            val currentDiffuse = effectiveColor * diffuse * lightDotNormal
            val reflectv = (-lightv).reflect(normalVector)
            val reflectDotEye = reflectv dot eyeVector

            val currentSpecular = if (reflectDotEye <= 0) {
                Color.BLACK
            } else {
                val factor = reflectDotEye.pow(shininess)
                light.intensity * specular * factor
            }

            Pair(currentDiffuse, currentSpecular)
        }
        return currentAmbient + currentDiffuse + currentSpecular
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Material

        if (color != other.color) return false
        if (ambient != other.ambient) return false
        if (diffuse != other.diffuse) return false
        if (specular != other.specular) return false
        return shininess == other.shininess
    }

    override fun hashCode(): Int {
        var result = color.hashCode()
        result = 31 * result + ambient.hashCode()
        result = 31 * result + diffuse.hashCode()
        result = 31 * result + specular.hashCode()
        result = 31 * result + shininess.hashCode()
        return result
    }
}
