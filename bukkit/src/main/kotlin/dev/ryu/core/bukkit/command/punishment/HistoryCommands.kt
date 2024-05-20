package dev.ryu.core.bukkit.command.punishment

import dev.ryu.core.bukkit.menu.punishment.history.PunishmentMenu
import dev.ryu.core.bukkit.menu.punishment.staffhistory.StaffPunishmentMenu
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.PunishmentModule
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object HistoryCommands {

    @Command(names = ["history","c","hist","check"], description = "View a user's punishments!", permission = "command.history")
    @JvmStatic
    fun history(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        val punishments = PunishmentModule.repository.findByVictim(target.id, Punishment.Type.entries.toMutableList())

        if (punishments.isEmpty()) {
            sender.sendMessage("${target.name}${ChatColor.RED} has no punishments.")
            return
        }

        PunishmentMenu(target,punishments).openMenu(sender)
    }

    @Command(names = ["staffhistory"], description = "View a staff member's punish history!", permission = "command.history.staff")
    @JvmStatic
    fun staffHistory(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
    ) {

        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        val punishments = PunishmentModule.repository.findBySenderOrPardoner(target.id)

        if (punishments.isEmpty()) {
            sender.sendMessage("${target.name} ${ChatColor.RED}has no punishments!")
            return
        }

        StaffPunishmentMenu(target,punishments).openMenu(sender)
    }

}