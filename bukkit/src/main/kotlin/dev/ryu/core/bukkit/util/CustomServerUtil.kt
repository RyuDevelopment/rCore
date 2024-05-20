package dev.ryu.core.bukkit.util

import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import org.bukkit.entity.Player
import java.io.*
import java.net.URL

object CustomServerUtil {

    @JvmStatic
    fun createCustomServer(player: Player) {
        val serverName = CustomServerInfo.serverName ?: return

        val serverFolder = File("/home/deploys/$serverName")
        try {
            serverFolder.mkdirs()

            val spigot = CustomServerInfo.spigot ?: return
            val spigotFile = File("/home/deploys/settings/spigots/$spigot.jar")
            if (spigotFile.exists()) {
                val destination = File(serverFolder, "$spigot.jar")
                spigotFile.copyTo(destination)
            }

            createStartScript(serverFolder, spigot)
            createServerPropertiesFile(serverFolder)
            createCoreFolderAndConfigFile(serverFolder)

            val pluginsFolder = File(serverFolder, "plugins")
            pluginsFolder.mkdirs()

            CustomServerInfo.pluginsAvailables.forEach { (plugin, isEnabled) ->
                if (isEnabled) {
                    val pluginFile = File("/home/deploys/settings/plugins/$plugin.jar")
                    if (pluginFile.exists()) {
                        val destination = File(pluginsFolder, "$plugin.jar")
                        pluginFile.copyTo(destination)
                    }
                }
            }

            executeCommand(serverFolder)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun executeCommand(serverFolder: File) {
        val command = "screen -S ${CustomServerInfo.serverName!!.toLowerCase()} -D -m ./start.sh"

        try {
            val processBuilder = ProcessBuilder("/bin/bash", "-c", "$command &")
            processBuilder.directory(serverFolder)
            val process = processBuilder.start()

            val exitCode = process.waitFor()

            if (exitCode != 0) {
                val errorReader = BufferedReader(InputStreamReader(process.errorStream))
                val errorMessage = errorReader.readText()
                throw RuntimeException("Failed to execute command '$command' with exit code $exitCode. Error message: $errorMessage")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun createStartScript(serverFolder: File, spigot: String) {
        val startScriptFile = File(serverFolder, "start.sh")
        FileWriter(startScriptFile).use { writer ->
            writer.write("java -Xmx6G -Xms256M -jar $spigot.jar")
        }

        val command = "chmod 777 ${startScriptFile.absolutePath}"
        try {
            val process = Runtime.getRuntime().exec(arrayOf("/bin/bash", "-c", command))
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                val errorReader = BufferedReader(InputStreamReader(process.errorStream))
                val errorMessage = errorReader.readText()
                throw RuntimeException("Failed to execute command '$command' with exit code $exitCode. Error message: $errorMessage")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @JvmStatic
    fun createServerPropertiesFile(serverFolder: File) {
        val serverPropertiesFile = File(serverFolder, "server.properties")
        FileWriter(serverPropertiesFile).use { writer ->
            writer.write("server-port=${CustomServerInfo.port}\n")
            writer.write("max-players=${CustomServerInfo.maxPlayers}\n")
        }
    }
    @JvmStatic
    fun createCoreFolderAndConfigFile(serverFolder: File) {
        val path = "$serverFolder/plugins/Core/"

        val pathFolder = File(path)
        if (!pathFolder.exists()) pathFolder.mkdirs()

        val configFile = File(pathFolder, "config.yml")
        FileWriter(configFile).use { writer ->
            writer.write("server-info:\n")
            writer.write("  name: ${CustomServerInfo.serverName}\n")
            writer.write("  group: \"Custom Server\"\n")
            writer.write("chatFormat:\n")
            writer.write("  text: \"{rank} {name}{message}\"\n")
            writer.write("  enabled: true\n")
        }
    }

    @JvmStatic
    fun downloadPlugin(url: String, pluginName: String) {
        val destinationFolder = "/home/deploys/settings/plugins"
        try {
            val urlObject = URL(url)
            val connection = urlObject.openConnection()
            val inputStream = connection.getInputStream()

            val outputStream = FileOutputStream("$destinationFolder/$pluginName.jar")
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            println("Plugin downloaded successfully to: $destinationFolder/$pluginName.jar")

            CustomServerInfo.pluginsAvailables[pluginName] = false
        } catch (e: IOException) {
            println("Error downloading plugin: ${e.message}")
            e.printStackTrace()
        }
    }

    
}