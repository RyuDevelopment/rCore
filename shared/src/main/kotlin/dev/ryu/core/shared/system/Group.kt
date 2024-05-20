package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import dev.ryu.core.shared.system.module.NetworkModule

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class Group(@SerializedName("_id")val id: String) {

    var announcements = ArrayList<ArrayList<String>>()

    fun hasServer(server: Server):Boolean {
        return this.findServers().any{it.id.equals(server.id,true) && it.port == server.port}
    }

    fun findServers():MutableSet<Server> {
        return NetworkModule.servers.filter{it.group.equals(this.id,true)}.toMutableSet()
    }

    companion object {
        const val UPDATE_ID = "SERVER_GROUP_UPDATE"
    }

}