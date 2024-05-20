package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object SuicideCommand {

    @Command(names = ["suicide"], permission = "core.staff", description = "Take your own life")
    @JvmStatic
    fun suicide(sender: Player) {
        sender.health = 0.0
        sender.sendMessage(ChatColor.GOLD.toString() + "You have been killed.")
    }

}