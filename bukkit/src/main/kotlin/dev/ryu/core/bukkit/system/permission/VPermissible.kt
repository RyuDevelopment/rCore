package dev.ryu.core.bukkit.system.permission

import dev.ryu.core.bukkit.Core
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissibleBase
import org.bukkit.permissions.Permission
import org.bukkit.permissions.PermissionAttachment
import org.bukkit.permissions.PermissionAttachmentInfo
import java.util.concurrent.ConcurrentHashMap

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

class VPermissible(player: Player) : PermissibleBase(player) {

    private val uuid = player.uniqueId

    val permissions = ConcurrentHashMap<String,Boolean>()

    init { this.calculate(false) }

    fun calculate(clear: Boolean) {

        if (clear) {
            this.permissions.clear()
        }

        this.calculatePermissions().entries.forEach{this.permissions[it.key.toLowerCase()] = it.value}
    }

    override fun hasPermission(permission: Permission): Boolean {
        return this.hasPermission(permission.name)
    }

    override fun hasPermission(permission: String): Boolean {

        if (this.isOp && (this.permissions[permission] != false)) {
            return true
        }

        return this.permissions[permission.toLowerCase()] ?: false
    }

    override fun isPermissionSet(permission: Permission): Boolean {
        return this.isPermissionSet(permission.name)
    }

    override fun isPermissionSet(name: String): Boolean {
        return this.permissions.containsKey(name.toLowerCase())
    }

    override fun clearPermissions() = this.permissions.clear()
    override fun recalculatePermissions() {}

    private fun calculatePermissions():ConcurrentHashMap<String,Boolean> {

        val permissions = ArrayList<String>()

        permissions.addAll((GrantModule.active[this.uuid] ?: ArrayList()).flatMap{it.getRank()?.permissions ?: HashSet()})
        permissions.addAll(ProfileModule.findById(this.uuid)?.permissions ?: ArrayList())

        val toReturn = ProfileModule.calculatePermissions(permissions,true)

        toReturn[Server.BROADCAST_CHANNEL_USERS] = true

        return toReturn
    }

    override fun getEffectivePermissions():MutableSet<PermissionAttachmentInfo> {
        return this.permissions.entries.map{PermissionAttachmentInfo(this,it.key,PermissionAttachment(dev.ryu.core.bukkit.Core.get(),this),it.value)}.toMutableSet()
    }

}