package dev.ryu.core.bukkit.event.permission

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

class PermissionUpdateEvent(val player: Player) : Event(){

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }


}