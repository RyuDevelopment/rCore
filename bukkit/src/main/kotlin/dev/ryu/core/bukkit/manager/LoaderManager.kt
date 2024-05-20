package dev.ryu.core.bukkit.manager

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.listener.StaffModeHotbarListener
import dev.ryu.core.bukkit.listener.orbit.CodeOrbitListener
import dev.ryu.core.bukkit.listener.orbit.PunishmentOrbitListener
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.extra.IManager
import com.starlight.nexus.manager.ClassManager
import org.bukkit.event.HandlerList

object LoaderManager : IManager {

    override fun onEnable() {
        ClassManager.registerPackage("dev.ryu.core.bukkit.listener", "listener", false)
        dev.ryu.core.bukkit.Core.get().server.pluginManager.registerEvents(StaffModeHotbarListener, dev.ryu.core.bukkit.Core.get())

        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().addListener(CodeOrbitListener())
        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().addListener(PunishmentOrbitListener())
    }

    override fun onDisable() {

        HandlerList.unregisterAll()

    }

}