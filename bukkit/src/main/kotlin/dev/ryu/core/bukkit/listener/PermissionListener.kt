package dev.ryu.core.bukkit.listener

import dev.ryu.core.bukkit.manager.PermissionManager
import dev.ryu.core.bukkit.system.permission.VPermissible
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.permissions.PermissibleBase

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

object PermissionListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    private fun onPlayerLogin(event: PlayerLoginEvent) {
        PermissionManager.setPermissible(event.player, VPermissible(event.player))
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private fun onPlayerQuit(event: PlayerQuitEvent) {
        PermissionManager.setPermissible(event.player,PermissibleBase(event.player))
    }

}