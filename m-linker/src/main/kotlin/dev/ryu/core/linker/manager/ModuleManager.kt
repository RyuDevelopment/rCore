package dev.ryu.core.linker.manager

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.linker.loader.ModuleJarLoader
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import java.util.logging.Logger

/**
 * Manages the modules within the system.
 */
object ModuleManager {

    lateinit var externalModulesPath: File

    /**
     * Map that stores modules based on their IDs.
     */
    var modules: ConcurrentMap<Int, MutableMap<IModule, Boolean>> = ConcurrentHashMap()

    /**
     * Map that stores default modules based on their IDs.
     */
    var defaultModules: ConcurrentMap<Int, MutableMap<IModule, Boolean>> = ConcurrentHashMap()

    /**
     * Logger
     */
    val logger: Logger = Logger.getLogger(ModuleManager::class.java.name)

    /**
     * Enables all registered modules.
     */
    fun onEnable() {
        if (!ModuleManager::externalModulesPath.isInitialized) {
            logger.warning("[Core] (Module System) External module directory path is not configured.");
        } else if (externalModulesPath.absolutePath.isEmpty()) {
            logger.info("[Core] (Module System) No external modules to register.");
        } else {
            ModuleJarLoader.loadExternalModulesFromJar();
        }

        defaultModules.values.forEach { toReturn ->
            toReturn.keys.sortedBy { it.id() }.filter { it.id() >= 1 }.forEach { module ->
                module.onEnable()
                logger.info("[Core] (Module System) Successfully enabled default module: ${module.moduleName()}")
            }
        }

        modules.values.forEach { toReturn ->
            toReturn.keys.sortedBy { it.id() }.filter { it.id() >= 1 }.forEach { module ->
                module.onEnable()
                logger.info("[Core] (Module System) Successfully enabled module: ${module.moduleName()}")
            }
        }

        logger.info("[Core] (Module System) Successfully enabled all registered modules!")
    }

    /**
     * Disables all registered modules.
     */
    fun onDisable() {
        defaultModules.values.forEach { toReturn ->
            toReturn.keys.sortedBy { it.id() }.filter { it.id() >= 1 }.forEach { module ->
                module.onDisable()
                logger.info("[Core] (Module System) Successfully disabled default module: ${module.moduleName()}")
            }
        }

        modules.values.forEach { toReturn ->
            toReturn.keys.sortedBy { it.id() }.filter { it.id() >= 1 }.forEach { module ->
                module.onDisable()
                logger.info("[Core] (Module System) Successfully disabled module: ${module.moduleName()}")
            }
        }

        logger.info("[Core] (Module System) Successfully disabled all registered modules!")
    }

    /**
     * Registers a new module with the given ID.
     *
     * @param module The module to register.
     * @param id The ID of the module.
     * @param default Is default module.
     */
    @JvmStatic
    fun registerModule(module: IModule, id: Int) {
        if (module.isDefault()) {
            defaultModules.computeIfAbsent(id) { ConcurrentHashMap() }[module] = true
        } else {
            modules.computeIfAbsent(id) { ConcurrentHashMap() }[module] = true

            logger.info("[Core] (Module System) Successfully registered module: ${module.moduleName()}")
        }
    }

    /**
     * Reloads the specified module.
     *
     * @param module The module to reload.
     */
    @JvmStatic
    fun reloadModule(module: IModule) {
        module.onReload()

        logger.info("[Core] (Module System) Successfully reloaded module: ${module.moduleName()}")
    }

    /**
     * Enables the specified module.
     *
     * @param module The module to enable.
     */
    @JvmStatic
    fun enableModule(module: IModule) {
        module.onEnable()

        if (module.isDefault()) {
            defaultModules.forEach { (_, moduleMap) ->
                if (moduleMap.containsKey(module)) {
                    moduleMap[module] = true
                }
            }

            logger.info("[Core] (Module System) Successfully enabled default module: ${module.moduleName()}")
            return
        }

        modules.forEach { (_, moduleMap) ->
            if (moduleMap.containsKey(module)) {
                moduleMap[module] = true
            }
        }

        logger.info("[Core] (Module System) Successfully enabled module: ${module.moduleName()}")
    }

    /**
     * Disables the specified module.
     *
     * @param module The module to disable.
     */
    @JvmStatic
    fun disableModule(module: IModule) {
        module.onDisable()

        if (module.isDefault()) {
            defaultModules.forEach { (_, moduleMap) ->
                if (moduleMap.containsKey(module)) {
                    moduleMap[module] = false
                }
            }

            logger.info("[Core] (Module System) Successfully disabled default module: ${module.moduleName()}")
            return
        }

        modules.forEach { (_, moduleMap) ->
            if (moduleMap.containsKey(module)) {
                moduleMap[module] = false
            }
        }

        logger.info("[Core] (Module System) Successfully disabled module: ${module.moduleName()}")
    }

    /**
     * Finds and returns the module associated with the specified ID.
     *
     * @param id The ID of the module to find.
     * @return The module associated with the specified ID.
     * @throws NoSuchElementException if no module is found with the given ID.
     */
    @JvmStatic
    fun findById(id: Int): IModule {
        val moduleInModules = modules.keys.find { it == id }?.let { moduleId ->
            modules[moduleId]?.keys?.firstOrNull()
        }

        return if (moduleInModules == null) {
            val moduleInDefaultModules = defaultModules.keys.find { it == id }?.let { moduleId ->
                defaultModules[moduleId]?.keys?.firstOrNull()
            }

            moduleInDefaultModules ?: throw NoSuchElementException("No module found with ID: $id")
        } else {
            moduleInModules
        }
    }

    /**
     * Checks if a module is enabled in the modules map.
     *
     * @param module The module to check if it is enabled.
     * @return true if the module is enabled in at least one entry of the map, false otherwise.
     */
    @JvmStatic
    fun isModuleEnabled(module: IModule): Boolean {
        val moduleEnabledInModules = modules.values.any { it[module] == true }

        return if (!moduleEnabledInModules) {
            defaultModules.values.any { it[module] == true }
        } else {
            true
        }
    }

}