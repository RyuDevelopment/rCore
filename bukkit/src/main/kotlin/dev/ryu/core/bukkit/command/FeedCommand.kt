package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object FeedCommand {

    @Command(names = ["fed", "feed"], permission = "core.feed", description = "Feed a player")
    @JvmStatic
    fun feed(
        sender: Player,
        @Param(name = "player", defaultValue = "self") target: Player
    ) {
        if (sender != target && !sender.hasPermission("core.staff")) {
            sender.sendMessage("${ChatColor.RED}No permission to feed other players.")
            return
        }
        target.foodLevel = 20
        target.saturation = 10.0f
        if (sender != target) {
            sender.sendMessage("${target.displayName}${ChatColor.GOLD} has been fed.")
        }
        target.sendMessage("${ChatColor.GOLD}You have been fed.")
    }

}