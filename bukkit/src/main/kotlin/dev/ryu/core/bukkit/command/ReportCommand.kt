package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import dev.ryu.core.bukkit.menu.report.ReportMenu
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Profile
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player

object ReportCommand {

    @Command(names = ["report", "testreport"], permission = "", description = "Report a player")
    @JvmStatic
    fun report(
        sender: Player,
        @Param(name = "player") target: Profile,
        @Param(name = "reason", wildcard = true, defaultValue = "Cheating") reason: String
    ) {
        if (sender.name == target.name) {
            sender.sendMessage("${ChatColor.RED}You cannot report yourself.")
            return
        }

        val senderProfile = CoreAPI.profileManager.findById(sender.uniqueId)!!

        if (System.currentTimeMillis() < senderProfile.reportCooldown) {
            sender.sendMessage(ChatColor.RED.toString() + "Please wait, before sending a report again.")
            return
        }

        CoreAPI.reportManager.report(target.id, target.name!!, sender.uniqueId, sender.name, senderProfile.currentServer!!, reason)
        senderProfile.reportCooldown = (System.currentTimeMillis() + 300 * 1000L)
        CoreAPI.profileManager.repository.update(senderProfile)

        sender.sendMessage(ChatColor.GREEN.toString() + "We have received your report.")
    }

    @Command(names = ["reports"], permission = "core.staff", description = "View all player reports")
    @JvmStatic
    fun reports(
        sender: Player,
        @Param(name = "player") target: Profile
    ) {
        if (target.getTotalReports() <= 0) {
            sender.sendMessage("${ChatColor.RED}Oops! It seems this player doesn't have any reports.")
            return
        }

        ReportMenu(target).openMenu(sender)

        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

}