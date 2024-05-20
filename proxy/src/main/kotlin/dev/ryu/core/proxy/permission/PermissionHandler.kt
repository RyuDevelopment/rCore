package dev.ryu.core.proxy.permission

import dev.ryu.core.proxy.Core
import dev.ryu.core.proxy.permission.listener.PermissionListener
import dev.ryu.core.proxy.permission.packet.PermissionPacketListener
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import net.md_5.bungee.api.connection.ProxiedPlayer

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class PermissionHandler(private val instance: Core) {

    init {
        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().addListener(PermissionPacketListener(this.instance))
        this.instance.proxy.pluginManager.registerListener(this.instance, PermissionListener(this.instance))
    }

    fun update(player: ProxiedPlayer) {

        val permissions = ArrayList<String>()

        permissions.addAll((GrantModule.active[player.uniqueId] ?: ArrayList()).flatMap{it.getRank()?.permissions ?: HashSet()})
        permissions.addAll(ProfileModule.findById(player.uniqueId)?.permissions ?: ArrayList())

        ProfileModule.calculatePermissions(permissions,true)
    }

}