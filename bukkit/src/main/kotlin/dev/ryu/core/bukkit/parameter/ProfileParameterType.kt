package dev.ryu.core.bukkit.parameter

import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.ProfileModule
import com.starlight.nexus.command.data.parameter.ParameterType
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 1/3/2024 - 22:54
*/

class ProfileParameterType : ParameterType<Profile> {

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return ProfileModule.cache.values.filter{ StringUtils.startsWithIgnoreCase(it.name,source)}.map{it.name!!}.toList()
    }

    override fun transform(sender: CommandSender, source: String): Profile? {
        val toReturn = ProfileModule.findByName(source)

        if (toReturn == null) {
            sender.sendMessage("${ChatColor.RED}There's not existing profile with that name.")
            return  null
        }

        return toReturn
    }

}