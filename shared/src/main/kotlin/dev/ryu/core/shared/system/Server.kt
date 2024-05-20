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

class Server(@SerializedName("_id")val id: String,val port: Int,var group: String) {

    var update: Long = 0L
    var status: Status = Status.OFFLINE
    val created: Long = System.currentTimeMillis()

    var onlinePlayers = HashSet<UUID>()
    var maximumPlayers: Int = 0
    var whitelistedPlayers = HashSet<UUID>()

    var ticksPerSecond: DoubleArray = DoubleArray(3)

    fun isHub(): Boolean {
        return this.id.startsWith("Hub",true) || this.id.startsWith("Lobby",true) || this.group.startsWith("Hub",true) || this.group.startsWith("Lobby",true)
    }

    fun isWhitelisted(uuid: UUID):Boolean {
        return this.whitelistedPlayers.contains(uuid)
    }

    fun findGroup(): Group? {
        return NetworkModule.findGroupById(this.group)
    }

    companion object {
        const val UPDATE_ID = "SERVER_UPDATE"
    }

}
