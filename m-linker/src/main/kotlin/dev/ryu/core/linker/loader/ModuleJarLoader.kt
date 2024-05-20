package dev.ryu.core.linker.loader

import dev.ryu.core.linker.manager.ModuleManager
import dev.ryu.core.linker.data.IModule
import java.net.URLClassLoader
import java.util.jar.JarFile

object ModuleJarLoader {

    @JvmStatic
    fun loadExternalModulesFromJar() {
        val modulesDirectory = ModuleManager.externalModulesPath
        if (!modulesDirectory.exists() || !modulesDirectory.isDirectory) {
            ModuleManager.logger.warning("[Core] (Module System) External modules directory does not exist or is not a directory.")
            return
        }

        val jarFiles = modulesDirectory.listFiles { file -> file.extension == "jar" }
        jarFiles?.forEach { jarFile ->
            try {
                val jarClassLoader = URLClassLoader.newInstance(arrayOf(jarFile.toURI().toURL()), javaClass.classLoader)
                val jar = JarFile(jarFile)
                val entries = jar.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    if (!entry.isDirectory && entry.name.endsWith(".class")) {
                        val className = entry.name.replace('/', '.').removeSuffix(".class")
                        try {
                            val loadedClass = jarClassLoader.loadClass(className)
                            if (IModule::class.java.isAssignableFrom(loadedClass)) {
                                val module = loadedClass.getDeclaredConstructor().newInstance() as IModule
                                if (!ModuleManager.modules.keys.any { it == module.id() }) {
                                    ModuleManager.registerModule(module, module.id())
                                    ModuleManager.logger.info("[Core] (Module System) Successfully loaded external module: ${module.moduleName()} from ${jarFile.name}")
                                } else {
                                    ModuleManager.logger.severe("[Core] (Module System) Failed to load external module ${module.moduleName()} from ${jarFile.name}: A module with the same ID (${module.id()}) already exists.")
                                }
                            }
                        } catch (e: Exception) {
                            ModuleManager.logger.warning("[Core] (Module System) Failed to load class $className from ${jarFile.name}: ${e.message}")
                        }
                    }
                }
                jar.close()
            } catch (e: Exception) {
                ModuleManager.logger.warning("[Core] (Module System) Failed to load modules from ${jarFile.name}: ${e.message}")
            }
        }
    }

}
