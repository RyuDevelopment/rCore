package dev.ryu.core.bukkit.command.servermanager

import dev.ryu.core.bukkit.menu.server.ServerManager
import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:48
*/

object ServerManagerCommand {

    @Command(names = ["servermanager", "sm"], description = "", permission = "core.admin")
    @JvmStatic
    fun serverManager(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        ServerManager().openMenu(sender)
    }

}