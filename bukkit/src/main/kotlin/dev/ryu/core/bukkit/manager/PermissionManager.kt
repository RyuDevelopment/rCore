package dev.ryu.core.bukkit.manager

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.listener.PermissionListener
import dev.ryu.core.bukkit.listener.orbit.PermissionOrbitListener
import dev.ryu.core.bukkit.system.permission.VPermissible
import dev.ryu.core.bukkit.event.permission.PermissionUpdateEvent
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.extra.IManager
import dev.ryu.core.shared.system.module.ProfileModule
import dev.t4yrn.jupiter.Jupiter
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity
import org.bukkit.entity.Player
import org.bukkit.permissions.Permissible
import java.lang.reflect.Field

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

object PermissionManager : IManager {

    override fun onEnable() {
        HUMAN_ENTITY_PERMISSIBLE_FIELD.isAccessible = true

        dev.ryu.core.bukkit.Core.get().server.pluginManager.registerEvents(PermissionListener, dev.ryu.core.bukkit.Core.get())
        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().addListener(PermissionOrbitListener())
    }

    override fun onDisable() {

    }

    fun update(player: Player,clear: Boolean):Boolean {

        val permissible = getPermissible(player)

        if (permissible !is VPermissible) {
            return false
        }

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()){
            dev.ryu.core.bukkit.Core.get().server.pluginManager.callEvent(
            PermissionUpdateEvent(player)
        )}

        permissible.calculate(clear)
        return true
    }

    fun update(profile: Profile, permission: String, remove: Boolean):Boolean {

        if (Bukkit.isPrimaryThread()) {
            throw IllegalStateException("Cannot update permission on main thread.")
        }

        val toReturn = ProfileModule.repository.update(profile)

        if (toReturn) {

            val jsonObject = JsonObject()

            jsonObject.addProperty("_id",profile.id.toString())
            jsonObject.addProperty("remove",remove)
            jsonObject.addProperty("permission",permission)

            dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().sendPacket(Jupiter(Profile.PERMISSION_UPDATE_PACKET,jsonObject))
        }

        return toReturn
    }

    fun getPermissible(player: Player):Permissible {
        return HUMAN_ENTITY_PERMISSIBLE_FIELD.get(player) as Permissible
    }

    fun setPermissible(player: Player,permissible: Permissible) {
        HUMAN_ENTITY_PERMISSIBLE_FIELD.set(player,permissible)
    }

    private val HUMAN_ENTITY_PERMISSIBLE_FIELD: Field = CraftHumanEntity::class.java.getDeclaredField("perm")

}