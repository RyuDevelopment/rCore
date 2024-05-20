package dev.ryu.core.bukkit.command.punishment

import dev.ryu.core.bukkit.manager.ServerManager
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.PunishmentModule
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

object BlacklistCommands {

    @Command(names = ["blacklist"], description = "Blacklist a user the network!", permission = "command.blacklist")
    @JvmStatic
    fun blacklist(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "reason") reason: String
    ) {

        val senderUuid = if (sender is Player) sender.uniqueId else UUID.fromString(Profile.CONSOLE_UUID)
        val senderName = if (sender is Player) sender.name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        PunishmentModule.punish(Punishment.Type.BLACKLIST,target.id,if (target.address != null) target.addresses else ArrayList(),senderUuid,reason,
            ServerManager.server.id,false,target.name!!,senderName)
    }

    @Command(names = ["unblacklist"], description = "Remove a user from the blacklist.", permission = "command.blacklist")
    @JvmStatic
    fun pardon(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "reason") reason: String
    ) {

        val punishment = PunishmentModule.repository.findMostRecentPunishment(PunishmentModule.repository.findByVictim(target.id, Punishment.Type.BLACKLIST))

        if (punishment == null) {
            sender.sendMessage("${ChatColor.RED}${target.name} ${ChatColor.RED}does not have any active blacklists.")
            return
        }

        val senderUuid = if (sender is Player) sender.uniqueId else UUID.fromString(Profile.CONSOLE_UUID)
        val senderName = if (sender is Player) sender.name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        PunishmentModule.pardon(punishment,senderUuid,reason,false,target.name!!,senderName)
    }

}