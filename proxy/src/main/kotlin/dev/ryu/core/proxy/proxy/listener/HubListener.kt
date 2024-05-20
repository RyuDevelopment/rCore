package dev.ryu.core.proxy.proxy.listener

import dev.ryu.core.proxy.Core
import dev.ryu.core.shared.system.extra.server.Status
import dev.ryu.core.shared.system.module.NetworkModule
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.event.ServerKickEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class HubListener(private val instance: Core) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onServerConnect(event: ServerConnectEvent) {

        if (event.player.server != null) {
            return
        }

        val hubs = NetworkModule.servers.filter{it.status != Status.OFFLINE && it.isHub()}.mapNotNull{this.instance.proxy.getServerInfo(it.id)}.filter{it.canAccess(event.player)}

        val hub = hubs.firstOrNull{it.isRestricted} ?: if (hubs.isEmpty()) null else hubs.random()

        if (hub == null) {
            event.player.disconnect(TextComponent("${ChatColor.RED}Unavailable to find a suitable hub, please try again later!"))
            event.isCancelled = true
            return
        }

        event.target = hub
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onServerKick(event: ServerKickEvent) {

        val validKick = !event.kickReasonComponent.map{ChatColor.stripColor(it.toPlainText())}.any{ KICK_FILTERS.any{ filter -> filter.equals(it,true) || filter.startsWith(it,true)}}

        if (validKick) {
            return
        }

        val hubs = NetworkModule.servers.filter{it.status != Status.OFFLINE && it.isHub()}.mapNotNull{this.instance.proxy.getServerInfo(it.id)}.filter{it.canAccess(event.player)}

        val hub = hubs.firstOrNull{it.isRestricted} ?: if (hubs.isEmpty()) null else hubs.random()

        if (hub == null) {
            event.player.disconnect(TextComponent("${ChatColor.RED}Unavailable to find a suitable hub, please try again later!"))
            event.isCancelled = true
            return
        }

        event.isCancelled = true
        event.cancelServer = hub
    }

    companion object {

        val KICK_FILTERS = arrayListOf("Server closed","You have been deathbanned","You are still deathbanned")

    }

}