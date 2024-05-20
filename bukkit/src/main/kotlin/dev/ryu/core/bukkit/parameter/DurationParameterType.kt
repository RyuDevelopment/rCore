package dev.ryu.core.bukkit.parameter

import com.starlight.nexus.util.time.Duration
import com.starlight.nexus.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.time.format.DateTimeParseException

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 1/3/2024 - 22:54
*/

class DurationParameterType : ParameterType<Duration> {

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        return listOf()
    }

    override fun transform(sender: CommandSender, source: String): Duration? {
        return try {
            Duration.parse(source)
        } catch (e: DateTimeParseException) {
            sender.sendMessage("${ChatColor.RED}There's an invalid duration.")
            null
        }
    }

}