package dev.ryu.core.bukkit.manager

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.listener.orbit.ServerOrbitListener
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Group
import dev.ryu.core.shared.system.Server
import dev.ryu.core.shared.system.extra.IManager
import dev.ryu.core.shared.system.extra.server.Status
import dev.ryu.core.shared.system.module.NetworkModule
import dev.t4yrn.jupiter.Jupiter
import mkremins.fanciful.FancyMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

object ServerManager : IManager {

    lateinit var server: Server

    val serverID = dev.ryu.core.bukkit.Core.get().config.getString("server-info.name","unnamed")
    val serverGroup = dev.ryu.core.bukkit.Core.get().config.getString("server-info.group","unknown")

    private var announcement = 0

    private var postTaskId = dev.ryu.core.bukkit.Core.get().server.scheduler.runTaskTimer(dev.ryu.core.bukkit.Core.get(),{ post() },90L*20L,90L*20L).taskId
    private var updateTaskId = dev.ryu.core.bukkit.Core.get().server.scheduler.runTaskTimerAsynchronously(dev.ryu.core.bukkit.Core.get(),{ update() },60L,40L).taskId

    private var initialized = false

    override fun onEnable() {
        server = Server(serverID, dev.ryu.core.bukkit.Core.get().server.port, serverGroup)
        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().addListener(ServerOrbitListener())

        if (NetworkModule.findGroupById(serverGroup) == null) {
            NetworkModule.update(Group(serverGroup))
        }

        initialized = true
    }

    override fun onDisable() {
        dispose()
    }

    private fun dispose() {

        if (!initialized) {
            return
        }

        dev.ryu.core.bukkit.Core.get().server.scheduler.cancelTask(postTaskId)
        dev.ryu.core.bukkit.Core.get().server.scheduler.cancelTask(updateTaskId)

        server.group = serverGroup
        server.update = System.currentTimeMillis()
        server.status = Status.OFFLINE
        server.onlinePlayers.clear()

        NetworkModule.update(server)
    }

    private fun post() {

        val announcements = findAnnouncements()

        if (announcements.isEmpty()) {
            return
        }

        if (announcement >= announcements.size) {
            announcement = 0
        }

        announcements[announcement++].forEach{ dev.ryu.core.bukkit.Core.get().server.broadcastMessage(ChatColor.translateAlternateColorCodes('&',it))}
    }

    private fun update() {

        val whitelist = dev.ryu.core.bukkit.Core.get().server.whitelistedPlayers.map{it.uniqueId}.toHashSet()

        whitelist.addAll(dev.ryu.core.bukkit.Core.get().server.operators.map{it.uniqueId}.filter{!whitelist.contains(it)})

        server.update = System.currentTimeMillis()
        server.status = if (dev.ryu.core.bukkit.Core.get().server.hasWhitelist()) Status.WHITELISTED else Status.ONLINE
        server.onlinePlayers = dev.ryu.core.bukkit.Core.get().server.onlinePlayers.map{it.uniqueId}.toHashSet()
        server.maximumPlayers = dev.ryu.core.bukkit.Core.get().server.maxPlayers
        server.whitelistedPlayers = whitelist
        server.ticksPerSecond = doubleArrayOf(20.0,20.0,20.0)

        NetworkModule.update(server)
        NetworkModule.server = server
    }

    private fun findAnnouncements():ArrayList<ArrayList<String>> {
        return NetworkModule.findGroupById(serverGroup)?.announcements ?: ArrayList()
    }

    fun sendMessageToNetwork(message: FancyMessage,async: Boolean = false) {
        sendMessageToNetwork(message,null,async)
    }

    fun sendMessageToNetwork(message: FancyMessage,permission: String?,async: Boolean = true) {

        val payload = JsonObject()

        if (permission != null) {
            payload.addProperty("permission",permission)
        }

        payload.addProperty("message",message.toJSONString())

        val runnable = Runnable{
            dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().sendPacket(Jupiter(MESSAGE_ID,payload))
        }

        if (async) {
            Bukkit.getServer().scheduler.runTaskAsynchronously(dev.ryu.core.bukkit.Core.get(),runnable)
        } else {
            runnable.run()
        }

    }

    val replies = HashMap<UUID,UUID>()
    const val MESSAGE_ID = "NETWORK_MESSAGE"

}