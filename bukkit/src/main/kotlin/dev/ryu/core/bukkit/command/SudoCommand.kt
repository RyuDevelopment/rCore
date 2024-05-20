package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object SudoCommand {

    @Command(names = ["sudo"], permission = "core.admin", description = "Force a player to perform a command")
    @JvmStatic
    fun sudo(
        sender: CommandSender,
        @Param(name = "player") target: Player,
        @Param(name = "command", wildcard = true) command: String
    ) {
        target.chat("/$command")
        sender.sendMessage(ChatColor.GOLD.toString() + "Forced " + ChatColor.WHITE + target.displayName + ChatColor.GOLD + " to run " + ChatColor.WHITE + "'/" + command + "'" + ChatColor.GOLD + ".")
    }

    @Command(names = ["sudoall"], permission = "core.admin", description = "Froce all players to perform a command")
    @JvmStatic
    fun sudoall(
        sender: CommandSender,
        @Param(name = "command", wildcard = true) command: String
    ) {
        for (target in Bukkit.getOnlinePlayers()) {
            target.chat("/$command")
        }
        sender.sendMessage(ChatColor.GOLD.toString() + "Forced " + ChatColor.WHITE + "all players" + ChatColor.GOLD + " to run " + ChatColor.WHITE + "'/" + command + "'" + ChatColor.GOLD + ".")
    }

}