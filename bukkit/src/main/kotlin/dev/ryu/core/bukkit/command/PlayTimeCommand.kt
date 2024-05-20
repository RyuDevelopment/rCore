package dev.ryu.core.bukkit.command

import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.SessionModule
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 4:04 PM
*/

object PlayTimeCommand {

    @Command(names = ["playtime", "pt", "ptime"], description = "See a player playtime", permission = "")
    @JvmStatic
    fun execute(
        sender: CommandSender,
        @Param(name = "player") target: Profile
    ) {
        val session = SessionModule.repository.findMostRecentById(target.id)

        if (session != null) {
            val elapsedTimeMillis = System.currentTimeMillis() - session.joinedAt
            val hours = elapsedTimeMillis / (1000 * 60 * 60)
            val minutes = (elapsedTimeMillis % (1000 * 60 * 60)) / (1000 * 60)
            val seconds = ((elapsedTimeMillis % (1000 * 60 * 60)) % (1000 * 60)) / 1000

            val formattedTime = buildString {
                if (hours > 0) {
                    append("$hours ${if (hours.toInt() == 1) "hour" else "hours"}, ")
                }
                if (minutes > 0 || hours > 0) {
                    append("$minutes ${if (minutes.toInt() == 1) "minute" else "minutes"}, ")
                }
                append("$seconds ${if (seconds.toInt() == 1) "second" else "seconds"}")
            }

            val totalPlaytimeMillis = SessionModule.repository.findTotalPlaytimeById(target.id)

            val hoursTotal = totalPlaytimeMillis / (1000 * 60 * 60)
            val minutesTotal = (totalPlaytimeMillis % (1000 * 60 * 60)) / (1000 * 60)
            val secondsTotal = ((totalPlaytimeMillis % (1000 * 60 * 60)) % (1000 * 60)) / 1000

            val formattedTimeTotal = buildString {
                if (hoursTotal > 0) {
                    append("$hoursTotal ${if (hoursTotal.toInt() == 1) "hour" else "hours"}, ")
                }
                if (minutesTotal > 0 || hoursTotal > 0) {
                    append("$minutesTotal ${if (minutesTotal.toInt() == 1) "minute" else "minutes"}, ")
                }
                append("$secondsTotal ${if (secondsTotal.toInt() == 1) "second" else "seconds"}")
            }

            sender.sendMessage("${ChatColor.LIGHT_PURPLE}${target.name}'s Playtime${ChatColor.GRAY}: ${ChatColor.YELLOW}$formattedTime ${ChatColor.GRAY}(Total: ${formattedTimeTotal})")
        }
    }

}