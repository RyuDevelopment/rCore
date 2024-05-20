package dev.ryu.core.proxy.api

import dev.ryu.core.proxy.Core
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.NetworkModule
import net.md_5.bungee.api.event.LoginEvent
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectedEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class APIListener(private val instance: Core) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLoginAPI(event: LoginEvent) {

        event.registerIntent(this.instance)

        this.instance.proxy.scheduler.runAsync(this.instance) {

            val grants = GrantModule.repository.findAllByPlayer(event.connection.uniqueId).toHashSet()

            event.completeIntent(this.instance)

            GrantModule.active[event.connection.uniqueId] = grants.filter{it.isActive()}.toCollection(ArrayList())

            GrantModule.setGrant(event.connection.uniqueId,grants)
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onServerSwitch(event: ServerConnectedEvent) {

        val server = NetworkModule.findServerById(event.server.info.name) ?: return

        GrantModule.setGrant(event.player.uniqueId, GrantModule.active[event.player.uniqueId] ?: HashSet())
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDisconnect(event: PlayerDisconnectEvent) {
        GrantModule.active.remove(event.player.uniqueId)
    }

}