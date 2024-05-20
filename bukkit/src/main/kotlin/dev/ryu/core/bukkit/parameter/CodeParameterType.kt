package dev.ryu.core.bukkit.parameter

import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.module.CodeModule
import com.starlight.nexus.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 1/3/2024 - 22:54
*/

class CodeParameterType : ParameterType<Code> {

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return listOf()
    }

    override fun transform(sender: CommandSender, source: String): Code? {
        val toReturn = CodeModule.repository.findByCode(source)

        if (toReturn == null) {
            sender.sendMessage("${ChatColor.RED}There's not existing redeemable code.")
            return null
        }

        return toReturn
    }

}