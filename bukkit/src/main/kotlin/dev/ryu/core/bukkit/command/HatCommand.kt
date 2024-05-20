package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object HatCommand {

    @Command(names = ["hat", "head"], permission = "core.hat")
    @JvmStatic
    fun hat(
        sender: CommandSender
    ) {
        val player = sender as Player
        val stack = player.itemInHand
        if (stack == null || stack.type == Material.AIR) {
            sender.sendMessage(ChatColor.RED.toString() + "You are not holding anything.")
            return
        }
        if (stack.type.maxDurability.toInt() != 0) {
            sender.sendMessage(ChatColor.RED.toString() + "The item you are holding is not suitable to wear for a hat.")
            return
        }
        val inventory = player.inventory
        var helmet = inventory.helmet
        if (helmet != null && helmet.type != Material.AIR) {
            sender.sendMessage(ChatColor.RED.toString() + "You are already wearing something as your hat.")
            return
        }
        var amount = stack.amount
        if (amount > 1) {
            --amount
            stack.amount = amount
        } else {
            player.itemInHand = ItemStack(Material.AIR, 1)
        }
        helmet = stack.clone()
        helmet.amount = 1
        inventory.helmet = helmet
    }

}