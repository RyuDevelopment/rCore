package dev.ryu.core.bukkit.command.tag

import dev.ryu.core.bukkit.menu.tag.TagMenu
import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: tags
    * Date: 13/2/2024 - 19:53
*/

object TagCommand {

    @Command(names = ["tag", "tags", "prefix", "prefix", "prefixes"], description = "")
    @JvmStatic
    fun tag(
            sender: CommandSender
        ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to execute this command.")
            return
        }

        TagMenu().openMenu(sender)
    }

}