package dev.ryu.core.bukkit.command.admin

import dev.ryu.core.bukkit.system.lang.Lang
import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object CoreCommand {

    @Command(names = ["creload tags"], description = "Reload tag system", permission = "core.manage")
    @JvmStatic
    fun reloadTags(
        sender: CommandSender
    ) {
        dev.ryu.core.shared.Shared.moduleManager.reloadModule(dev.ryu.core.shared.Shared.moduleManager.findById(8))

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + "${ChatColor.GREEN}Successfully reload ${dev.ryu.core.shared.Shared.moduleManager.findById(8).moduleName()}!")
    }

    @Command(names = ["creload ranks"], description = "Reload rank system", permission = "core.manage")
    @JvmStatic
    fun reloadRanks(
        sender: CommandSender
    ) {
        dev.ryu.core.shared.Shared.moduleManager.reloadModule(dev.ryu.core.shared.Shared.moduleManager.findById(2))

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + "${ChatColor.GREEN}Successfully reload ${dev.ryu.core.shared.Shared.moduleManager.findById(2).moduleName()}!")
    }

    //hacer los dumps de las base de datos

}