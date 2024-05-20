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

object WarnCommand {

    @Command(names = ["warn"], description = "Warn a user!", permission = "command.warn")
    @JvmStatic
    fun warn(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "reason") reason: String
    ) {

        val senderUuid = if (sender is Player) sender.uniqueId else UUID.fromString(Profile.CONSOLE_UUID)
        val senderName = if (sender is Player) sender.name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        PunishmentModule.punish(Punishment.Type.WARN,target.id,senderUuid,reason,
            ServerManager.server.id,0L,false,target.name!!,senderName)
    }

}