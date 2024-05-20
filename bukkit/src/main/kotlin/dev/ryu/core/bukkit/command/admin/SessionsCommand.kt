package dev.ryu.core.bukkit.command.admin

import dev.ryu.core.bukkit.menu.session.SessionMenu
import dev.ryu.core.shared.system.Profile
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 4:59 PM
*/

object SessionsCommand {

    @Command(names = ["sessions"], description = "See all player sessions", permission = "core.staff")
    @JvmStatic
    fun execute(
        sender: CommandSender,
        @Param("target") target: Profile,
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        if (!sender.isOp) {
            sender.sendMessage("${ChatColor.RED}Under maintenance.")
            return
        }

        SessionMenu(target ).openMenu(sender)

        sender.sendMessage("${ChatColor.RED}Under maintenance.")
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

}