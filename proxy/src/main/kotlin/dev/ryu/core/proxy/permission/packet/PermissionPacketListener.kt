package dev.ryu.core.proxy.permission.packet

import com.google.gson.JsonObject
import dev.ryu.core.proxy.Core
import dev.ryu.core.shared.system.Profile
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class PermissionPacketListener(private val instance: Core) : OrbitListener {

    @Orbit(Profile.PERMISSION_UPDATE_PACKET)
    fun onPermissionUpdate(data: JsonObject) {

        val player = this.instance.proxy.getPlayer(UUID.fromString(data["_id"].asString)) ?: return

        val remove = data["remove"].asBoolean
        val permission = data["permission"].asString

        player.setPermission(permission,!remove)
    }

}