package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.bukkit.Core
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.concurrent.TimeUnit

object UptimeCommand {

    fun uptimeColor(secs: Long): String {
        return when {
            secs <= TimeUnit.HOURS.toSeconds(16L) -> ChatColor.GREEN.toString()
            secs <= TimeUnit.HOURS.toSeconds(24L) -> ChatColor.YELLOW.toString()
            else -> ChatColor.RED.toString()
        }
    }

    @Command(names = ["uptime"], permission = "core.admin", description = "Check how long the server has been up for")
    @JvmStatic
    fun uptime(sender: CommandSender) {
        val uptimeMillis = System.currentTimeMillis() - Core.get().startupUptime!!
        val uptimeSeconds = TimeUnit.MILLISECONDS.toSeconds(uptimeMillis)
        sender.sendMessage("${ChatColor.GOLD}The server has been running for ${uptimeColor(uptimeSeconds)}${TimeUtil.formatIntoDetailedString(uptimeSeconds)}${ChatColor.GOLD}.")
    }

}