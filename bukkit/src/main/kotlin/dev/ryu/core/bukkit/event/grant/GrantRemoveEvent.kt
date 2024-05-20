package dev.ryu.core.bukkit.event.grant

import dev.ryu.core.shared.system.Grant
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 17:53
*/

class GrantRemoveEvent(val player: Player, val grant: Grant) : Event() {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }

}