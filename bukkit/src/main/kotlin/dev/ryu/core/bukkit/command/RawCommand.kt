package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object RawCommand {

    @Command(
        names = ["raw", "bcraw"],
        permission = "core.admin",
        description = "Broadcast a raw message. Supports color codes"
    )
    @JvmStatic
    fun raw(
        sender: CommandSender?,
        @Param(name = "message", defaultValue = " ", wildcard = true) message: String?
    ) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message))
    }

    @Command(
        names = ["msgraw"],
        permission = "core.admin",
        description = "Send a raw message to a player. Supports color codes"
    )
    @JvmStatic
    fun msgraw(
        sender: CommandSender?,
        @Param(name = "player") target: Player,
        @Param(name = "message", defaultValue = " ", wildcard = true) message: String?
    ) {
        target.sendMessage(ChatColor.translateAlternateColorCodes('&', message))
    }
    
}