package dev.ryu.core.bukkit.command.admin

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.TextSplitter
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.bukkit.system.reboot.RebootManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object RebootCommand {

    @Command(names = ["reboot start"], permission = "command.reboot")
    @JvmStatic
    fun execute(
        sender: CommandSender,
        @Param(name = "duration") durationString: String,
        @Param(name = "reason", defaultValue = "Server Restart") reason: String
    ) {
        if (RebootManager.isRebooting()) {
            sender.sendMessage("${ChatColor.DARK_RED}[Server Reboot] ${ChatColor.RESET}${ChatColor.RED}A reboot is already in progress.")
            return
        }

        val timeInMillis = parseDuration(durationString)
        RebootManager.reboot(timeInMillis, reason)
        sender.sendMessage("${ChatColor.DARK_RED}[Server Reboot] ${ChatColor.RESET}${ChatColor.RED}Scheduled a reboot in ${ChatColor.BOLD}${TimeUtil.formatIntoDetailedString(timeInMillis)}")
    }

    @Command(names = ["reboot cancel"], permission = "command.reboot")
    @JvmStatic
    fun execute(
        sender: CommandSender
    ) {
        if (!RebootManager.isRebooting()) {
            sender.sendMessage("${ChatColor.DARK_RED}[Server Reboot] ${ChatColor.RED}No reboot has been scheduled.")
            return
        }
        RebootManager.cancelReboot()
        sender.sendMessage("${ChatColor.DARK_RED}[Server Reboot] ${ChatColor.RED}The scheduled reboot has been canceled.")

        val message = mutableListOf<String>()

        message.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}------------------------------------")
        message.add("    ${ChatColor.RED}✘ ${ChatColor.BOLD}${ChatColor.DARK_RED}Server Reboot Canceled ${ChatColor.RED}✘")
        message.add("")
        message.add("${ChatColor.WHITE}The scheduled reboot has been canceled.")
        message.add("")
        message.addAll(TextSplitter.split(text = "${ChatColor.GRAY}${ChatColor.ITALIC}If you have any concerns, feel free to contact a staff member."))
        message.add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}------------------------------------")

        message.forEach {
            Bukkit.broadcastMessage(Color.color(it))
        }
    }

    private fun parseDuration(durationString: String): Long {
        val regex = Regex("(\\d+)([smhd])")
        val matchResult = regex.findAll(durationString)

        var totalTimeInMillis = 0L

        for (match in matchResult) {
            val (value, unit) = match.destructured
            val timeInMillis = when (unit) {
                "s" -> value.toLong() * 1000
                "m" -> value.toLong() * 1000 * 60
                "h" -> value.toLong() * 1000 * 60 * 60
                "d" -> value.toLong() * 1000 * 60 * 60 * 24
                else -> throw IllegalArgumentException("Invalid time unit: $unit")
            }
            totalTimeInMillis += timeInMillis
        }

        return totalTimeInMillis
    }

}