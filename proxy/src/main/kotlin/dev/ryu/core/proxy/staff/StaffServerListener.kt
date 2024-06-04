package dev.ryu.core.proxy.staff

import com.google.gson.JsonObject
import dev.ryu.core.proxy.Core
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.NetworkModule
import dev.t4yrn.jupiter.Jupiter
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

class StaffServerListener(private val instance: Core) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPostLogin(event: ServerConnectedEvent) {

        if (!GrantModule.findBestRank(event.player.uniqueId).isStaff()) {
            return
        }

        this.instance.proxy.scheduler.runAsync(this.instance) {

            val jsonObject = JsonObject()

            jsonObject.addProperty("to",event.server.info.name)

            if (event.player.server != null) {
                jsonObject.addProperty("from",event.player.server.info.name)
            }

            jsonObject.addProperty("name",event.player.name)
            jsonObject.addProperty("uuid",event.player.uniqueId.toString())

            val packet = if (event.player.server == null) NetworkModule.NETWORK_JOIN_PACKET else NetworkModule.NETWORK_SWITCH_PACKET

            val server = NetworkModule.findServerById((if (event.player.server != null) event.player.server.info else event.server.info).name)

            if (server != null) {
                jsonObject.addProperty("displayName",event.player.name)
                jsonObject.addProperty("rankColor", GrantModule.findBestRank(event.player.uniqueId).color)
            } else {
                jsonObject.addProperty("displayName",event.player.name)
                jsonObject.addProperty("rankColor", GrantModule.findBestRank(event.player.uniqueId).color)
            }

            dev.ryu.core.shared.Shared.backendManager.getJupiter().sendPacket(Jupiter(packet,jsonObject))
        }

    }

    @EventHandler(priority = EventPriority.NORMAL)
    fun onPlayerDisconnect(event: PlayerDisconnectEvent) {

        if (event.player.server == null) {
            return
        }

        if (!GrantModule.findBestRank(event.player.uniqueId).isStaff()) {
            return
        }

        this.instance.proxy.scheduler.runAsync(this.instance) {

            val jsonObject = JsonObject()

            jsonObject.addProperty("name",event.player.name)
            jsonObject.addProperty("uuid",event.player.uniqueId.toString())
            jsonObject.addProperty("from",event.player.server.info.name)

            val server = NetworkModule.findServerById(event.player.server.info.name)

            if (server != null) {
                jsonObject.addProperty("displayName", GrantModule.findDisplayName(event.player.uniqueId))
            } else {
                jsonObject.addProperty("displayName",event.player.name)
            }

            dev.ryu.core.shared.Shared.backendManager.getJupiter().sendPacket(Jupiter(NetworkModule.NETWORK_LEAVE_PACKET,jsonObject))
        }

    }
}