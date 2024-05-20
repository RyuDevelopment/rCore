package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object KillCommand {

    @Command(names = ["kill"], permission = "core.admin", description = "Kill a player")
    @JvmStatic
    fun kill(sender: Player, @Param(name = "player", defaultValue = "self") player: Player) {
        if (!sender.hasPermission("core.staff")) {
            sender.health = 0.0
            sender.sendMessage(ChatColor.GOLD.toString() + "You have been killed.")
            return
        }
        if (player.name.equals("JavaStrings", ignoreCase = true) || player.name.equals("Jzwy", ignoreCase = true)) {
            sender.inventory.clear()
            sender.health = 0.0
            sender.sendMessage(ChatColor.GOLD.toString() + "You have been killed.")
            sender.sendMessage(ChatColor.GOLD.toString() + "Nice try.")
            return
        }
        player.health = 0.0
        if (player == sender) {
            sender.sendMessage(ChatColor.GOLD.toString() + "You have been killed.")
        } else {
            sender.sendMessage(player.displayName + ChatColor.GOLD + " has been killed.")
        }
    }
    
}