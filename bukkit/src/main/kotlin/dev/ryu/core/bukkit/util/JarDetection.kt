package dev.ryu.core.bukkit.util

import java.io.File

object JarDetection {

    fun detectSpigotsJar(directoryPath: String): List<String> {
        val jarFileNames = mutableListOf<String>()
        val directory = File(directoryPath)
        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles()
            files?.forEach { file ->
                if (file.isFile && file.name.endsWith(".jar")) {
                    val nameWithoutExtension = file.nameWithoutExtension
                    jarFileNames.add(nameWithoutExtension)
                }
            }
        } else {
            println("The specified directory does not exist or is not a valid directory.")
        }
        return jarFileNames
    }

    fun detectPluginsJars(directoryPath: String): HashMap<String, Boolean> {
        val jarFileNames = hashMapOf<String, Boolean>()
        val directory = File(directoryPath)
        if (directory.exists() && directory.isDirectory) {
            val files = directory.listFiles()
            files?.forEach { file ->
                if (file.isFile && file.name.endsWith(".jar")) {
                    val nameWithoutExtension = file.nameWithoutExtension
                    jarFileNames[nameWithoutExtension] = false
                }
            }
        } else {
            println("The specified directory does not exist or is not a valid directory.")
        }
        return jarFileNames
    }

}