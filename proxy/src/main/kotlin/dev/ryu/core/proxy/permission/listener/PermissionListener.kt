package dev.ryu.core.proxy.permission.listener

import dev.ryu.core.proxy.Core
import net.md_5.bungee.api.event.PermissionCheckEvent
import net.md_5.bungee.api.event.PostLoginEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class PermissionListener(private val instance: Core):Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPostLogin(event: PostLoginEvent) {
        this.instance.permissionHandler.update(event.player)
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPermissionCheck(event: PermissionCheckEvent) {
        event.setHasPermission(event.sender.permissions.contains(event.permission.toLowerCase()) && !event.sender.permissions.contains("-${event.permission.toLowerCase()}"))
    }

}