package dev.ryu.core.bukkit.command.admin

import dev.ryu.core.bukkit.menu.module.ModuleMenu
import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.shared.CoreAPI
import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ManageCommand {

    @Command(names = ["manage modules"], description = "Manage all core modules", permission = "core.manage")
    @JvmStatic
    fun manageModules(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        ModuleMenu().openMenu(sender)
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

}