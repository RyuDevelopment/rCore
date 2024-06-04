package dev.ryu.core.bukkit.command.admin

import com.starlight.nexus.command.Command
import dev.ryu.core.bukkit.menu.coinshop.admin.AdminCoinShopMenu
import dev.ryu.core.bukkit.menu.module.ModuleMenu
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

    @Command(names = ["manage coinshop"], description = "Manage all coinshop menus", permission = "core.manage")
    @JvmStatic
    fun manageCoinShop(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        AdminCoinShopMenu().openMenu(sender)
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

}