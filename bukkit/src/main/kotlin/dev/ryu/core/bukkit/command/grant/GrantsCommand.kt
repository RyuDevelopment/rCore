package dev.ryu.core.bukkit.command.grant

import dev.ryu.core.bukkit.menu.grant.view.GrantsMenu
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.GrantModule
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 20:49
*/

object GrantsCommand {

    @Command(names = ["grants"], description = "View all granted ranks of player", permission = "core.grant")
    @JvmStatic
    fun grants(
        sender: CommandSender,
        @Param(name = "profile") profile: Profile,
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        val grants = GrantModule.repository.findAllByPlayer(profile.id)

        if (grants.isEmpty()) {
            sender.sendMessage("${profile.name}${ChatColor.RED} has no grants.")
            return
        }

        GrantsMenu(grants, profile).openMenu(sender)
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

}