package dev.ryu.core.bukkit.listener.orbit

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.manager.PermissionManager
import dev.ryu.core.bukkit.system.permission.VPermissible
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.ProfileModule
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import java.util.*

/**
 * @project venom
 *
 * @date 19/04/2020
 * @author xanderume@gmail.com
 */
class PermissionOrbitListener : OrbitListener {

    @Orbit(Profile.PERMISSION_UPDATE_PACKET)
    fun onPermissionUpdate(data: JsonObject) {

        val profile = ProfileModule.findById(UUID.fromString(data["_id"].asString)) ?: return

        val remove = data["remove"].asBoolean
        val permission = data["permission"].asString

        if (remove) profile.permissions.remove(permission) else if (!profile.permissions.contains(permission)) profile.permissions.add(permission)

        val player = dev.ryu.core.bukkit.Core.get().server.getPlayer(profile.id) ?: return

        val permissible = PermissionManager.getPermissible(player)

        if (permissible !is VPermissible) {
            return
        }

        if (remove) {
            permissible.permissions.remove(permission.toLowerCase())
        } else {
            permissible.permissions[permission.toLowerCase()] = !permission.startsWith("-")
        }

    }

}