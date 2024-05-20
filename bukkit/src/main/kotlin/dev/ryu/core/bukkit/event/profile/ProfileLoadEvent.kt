package dev.ryu.core.bukkit.event.profile

import dev.ryu.core.shared.system.Profile
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 10:01
*/

class ProfileLoadEvent(val profile: Profile) : Event(true) {

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    companion object {
        @JvmStatic val handlerList = HandlerList()
    }

}