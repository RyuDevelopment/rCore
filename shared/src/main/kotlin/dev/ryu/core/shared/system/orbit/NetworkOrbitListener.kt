package dev.ryu.core.shared.system.orbit

import com.google.gson.JsonObject
import dev.ryu.core.shared.system.Group
import dev.ryu.core.shared.system.Proxy
import dev.ryu.core.shared.system.Server
import dev.ryu.core.shared.system.extra.server.Status
import dev.ryu.core.shared.system.module.NetworkModule
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 09:43
*/

class NetworkOrbitListener : OrbitListener {

    @Orbit(Group.UPDATE_ID)
    fun onGroupUpdate(data: JsonObject) {
        val id = data["_id"].asString

        val group = NetworkModule.findGroupById(id) ?: Group(id)

        group.announcements.clear()

        for (element in data["announcements"].asJsonArray) {

            val announcement = ArrayList<String>()

            val array = element.asJsonArray.map{it.asString}.toList()

            announcement.addAll(array)

            group.announcements.add(announcement)
        }

        if (NetworkModule.findGroupById(group.id) != null) {
            return
        }

        NetworkModule.groups.add(group)
    }

    @Orbit(Proxy.UPDATE_ID)
    fun onProxyUpdate(data: JsonObject) {

        val id = data["_id"].asString

        val proxy = NetworkModule.findProxyById(id) ?: Proxy(id,data["port"].asInt,data["group"].asString)

        proxy.group = data["group"].asString
        proxy.motd = data["motd"].asJsonArray.map{it.asString}.toTypedArray()
        proxy.update = System.currentTimeMillis()
        proxy.status = Status.valueOf(data["status"].asString)
        proxy.onlinePlayers = data["onlinePlayers"].asJsonArray.map{UUID.fromString(it.asString)}.toHashSet()

        if (NetworkModule.findProxyById(proxy.id) != null) {
            return
        }

        NetworkModule.proxies.add(proxy)
    }

    @Orbit(Server.UPDATE_ID)
    fun onServerUpdate(data: JsonObject) {

        val id = data["_id"].asString
        val server = NetworkModule.findServerById(id) ?: Server(id,data["port"].asInt,data["group"].asString)

        server.group = data["group"].asString
        server.update = System.currentTimeMillis()
        server.status = Status.valueOf(data["status"].asString)
        server.onlinePlayers = data["onlinePlayers"].asJsonArray.map{UUID.fromString(it.asString)}.toHashSet()
        server.whitelistedPlayers = data["whitelistedPlayers"].asJsonArray.map{UUID.fromString(it.asString)}.toHashSet()
        server.maximumPlayers = data["maximumPlayers"].asInt
        server.ticksPerSecond =  data["ticksPerSecond"].asJsonArray.map{it.asDouble}.toDoubleArray()

        if (NetworkModule.findServerById(server.id) != null) {
            return
        }

        NetworkModule.servers.add(server)
    }

}