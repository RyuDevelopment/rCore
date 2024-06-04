package dev.ryu.core.bukkit.parameter

import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.RankModule
import com.starlight.nexus.command.data.parameter.ParameterType
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 1/3/2024 - 22:55
*/

class RankParameterType : ParameterType<Rank>{

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return dev.ryu.core.shared.Shared.rankManager.cache.values.filter{ StringUtils.startsWithIgnoreCase(it.id,source)}.sortedBy{it.weight}.reversed().map{it.id}.toList()
    }

    override fun transform(sender: CommandSender, source: String): Rank? {
        val toReturn = RankModule.findById(source)

        if (toReturn == null) {
            sender.sendMessage("${ChatColor.RED}There's not existing rank with that name.")
            return null
        }

        return toReturn
    }

}