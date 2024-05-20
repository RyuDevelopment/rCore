package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import dev.ryu.core.shared.system.extra.server.Status
import dev.ryu.core.shared.system.module.NetworkModule
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class Proxy(@SerializedName("_id")val id: String,val port: Int,var group: String) {

    var motd: Array<String> = arrayOf("","")

    var update: Long = 0L
    var status: Status = Status.OFFLINE

    val created: Long = System.currentTimeMillis()

    var onlinePlayers = HashSet<UUID>()
    var bungeeServers = HashSet<String>()

    fun findServers():List<Server> {
        return this.bungeeServers.mapNotNull{ NetworkModule.findServerById(it)}.toList()
    }

    companion object {

        const val UPDATE_ID = "PROXY_UPDATE"
    }

}