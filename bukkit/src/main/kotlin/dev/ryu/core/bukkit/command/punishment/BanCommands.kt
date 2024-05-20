package dev.ryu.core.bukkit.command.punishment

import dev.ryu.core.bukkit.manager.ServerManager
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.PunishmentModule
import com.starlight.nexus.util.time.Duration
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

object BanCommands {

    @Command(names = ["ban","tempban"], description = "Restrict a user from joining the network!", permission = "command.ban")
    @JvmStatic
    fun ban(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "duration") duration: Duration,
        @Param(name = "reason") reason: String
    ) {
        val senderUuid = if (sender is Player) sender.uniqueId else UUID.fromString(Profile.CONSOLE_UUID)
        val senderName = if (sender is Player) sender.name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        PunishmentModule.punish(Punishment.Type.BAN,target.id,senderUuid,reason,
            ServerManager.server.id,duration.get(),false,target.name!!,senderName)
    }

    @Command(names = ["ipban","banip","ipban","ban-ip","ip-ban"], description = "Restrict user's on address from joining the network!", permission = "command.ipban")
    @JvmStatic
    fun banIP(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "duration") duration: Duration,
        @Param(name = "reason") reason: String
    ) {

        val senderUuid = if (sender is Player) sender.uniqueId else UUID.fromString(Profile.CONSOLE_UUID)
        val senderName = if (sender is Player) sender.name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        PunishmentModule.punish(Punishment.Type.BAN,target.id,target.addresses,senderUuid,reason,
            ServerManager.server.id,duration.get(),false,target.name!!,senderName)
    }

    @Command(names = ["unban","pardon"], description = "Remove restriction of user joining the network.", permission = "command.unban")
    @JvmStatic
    fun pardon(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "reason") reason: String
    ) {

        val punishment = PunishmentModule.repository.findMostRecentPunishment(PunishmentModule.repository.findByVictim(target.id, Punishment.Type.BAN))

        if (punishment == null) {
            sender.sendMessage("${ChatColor.RED}${target.name} ${ChatColor.RED}does not have any active bans.")
            return
        }

        val senderUuid = if (sender is Player) sender.uniqueId else UUID.fromString(Profile.CONSOLE_UUID)
        val senderName = if (sender is Player) sender.name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        PunishmentModule.pardon(punishment,senderUuid,reason,false,target.name!!,senderName)

    }

}