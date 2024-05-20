package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object MoreCommand {

    @Command(names = ["more"], permission = "core.admin", description = "Give yourself more of the item you're holding")
    @JvmStatic
    fun more(sender: Player, @Param(name = "amount", defaultValue = "42069420") amount: Int) {
        if (sender.itemInHand == null) {
            sender.sendMessage(ChatColor.RED.toString() + "You must be holding an item.")
            return
        }
        if (amount == 42069420) {
            sender.itemInHand.amount = 64
        } else {
            sender.itemInHand.amount = Math.min(64, sender.itemInHand.amount + amount)
        }
        sender.sendMessage(ChatColor.GOLD.toString() + "There you go.")
    }
    
}