package dev.ryu.core.bukkit.parameter

import com.starlight.nexus.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 1/3/2024 - 22:54
*/

class ColorParameterType : ParameterType<ChatColor> {

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        val suggestion = mutableListOf<String>()

        ChatColor.entries.filter { !it.isFormat }.forEach {
            suggestion.add(it.name)
        }

        return suggestion
    }

    override fun transform(sender: CommandSender, source: String): ChatColor? {
        return try {
            ChatColor.valueOf(source.toUpperCase())
        } catch (e: IllegalArgumentException) {
            sender.sendMessage("${ChatColor.RED}There's no existing color with that name.")
            null
        }
    }

}