package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object EnderChestCommand {

    @Command(
        names = ["enderchest", "chest", "echest", "ec"],
        permission = "command.enderchest",
        description = "Open a player's Ender Chest"
    )
    @JvmStatic
    fun enderchest(sender: Player, @Param(name = "player", defaultValue = "self") player: Player) {
        if (player.uniqueId != sender.uniqueId && !sender.hasPermission("core.staff")) {
            sender.sendMessage(ChatColor.RED.toString() + "You do not have permission to open other players' Ender Chests!")
            return
        }
        sender.openInventory(player.enderChest)
    }

}