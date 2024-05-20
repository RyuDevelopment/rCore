package dev.ryu.core.bukkit.listener.orbit

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.manager.ServerManager
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.NetworkModule
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import mkremins.fanciful.FancyMessage
import org.bukkit.ChatColor

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class ServerOrbitListener : OrbitListener {

    @Orbit(NetworkModule.NETWORK_JOIN_PACKET)
    fun onNetworkJoin(data: JsonObject) {

        val server = NetworkModule.findServerById(data["to"].asString) ?: return

        this.broadcast(data["displayName"].asString,null,server.id)
    }

    @Orbit(NetworkModule.NETWORK_LEAVE_PACKET)
    fun onNetworkLeave(data: JsonObject) {

        val server = NetworkModule.findServerById(data["from"].asString) ?: return

        this.broadcast(data["displayName"].asString,server.id,null)
    }

    @Orbit(NetworkModule.NETWORK_SWITCH_PACKET)
    fun onServerSwitch(data: JsonObject) {

        val to = NetworkModule.findServerById(data["to"].asString) ?: return
        val from = NetworkModule.findServerById(data["from"].asString) ?: return

        this.broadcast(data["displayName"].asString,from.id,to.id)
    }

    @Orbit(ServerManager.MESSAGE_ID)
    fun onReceiveMessage(data: JsonObject) {

        val message = FancyMessage.deserialize(data["message"].asString)

        if (data.has("permission")) {
            dev.ryu.core.bukkit.Core.get().server.onlinePlayers.filter{it.hasPermission(data["permission"].asString)}.forEach{message.send(it)}
            return
        }

        dev.ryu.core.bukkit.Core.get().server.onlinePlayers.forEach{message.send(it)}
    }

    private fun broadcast(displayName: String,from: String?,to: String?) {

        val prefix = "${ChatColor.BLUE}[Staff] $displayName${ChatColor.GRAY} has "
        val suffix = if (from == null) "${ChatColor.GREEN}joined ${ChatColor.GRAY}the network on ${ChatColor.WHITE}$to${ChatColor.GRAY}." else if (to == null) "${ChatColor.RED}left ${ChatColor.GRAY}the network from ${ChatColor.WHITE}$from${ChatColor.GRAY}." else "joined ${ChatColor.WHITE}$to ${ChatColor.GRAY}from ${ChatColor.WHITE}$from${ChatColor.GRAY}."

        dev.ryu.core.bukkit.Core.get().server.onlinePlayers.filter{ GrantModule.findBestRank(it.uniqueId).isStaff()}.forEach{it.sendMessage("$prefix$suffix")}
    }

}