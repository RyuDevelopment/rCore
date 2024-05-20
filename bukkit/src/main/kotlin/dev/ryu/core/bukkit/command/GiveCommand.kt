package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object GiveCommand {

    @Command(names = ["item", "i", "get"], permission = "core.admin", description = "Spawn yourself an item")
    @JvmStatic
    fun item(
        sender: Player,
        @Param(name = "item") item: ItemStack,
        @Param(name = "amount", defaultValue = "64") amount: Int
    ) {
        if (amount < 1) {
            sender.sendMessage(ChatColor.RED.toString() + "The amount must be greater than zero.")
            return
        }
        item.amount = amount
        sender.inventory.addItem(item)
        sender.sendMessage(
            ChatColor.GOLD.toString() + "Giving " + ChatColor.WHITE + amount + ChatColor.GOLD + " of " + ChatColor.WHITE + ItemUtils.getName(
                item
            ) + ChatColor.GOLD + "."
        )
    }

    @Command(names = ["give"], permission = "core.admin", description = "Spawn a player an item")
    @JvmStatic
    fun give(
        sender: Player,
        @Param(name = "player") target: Player,
        @Param(name = "item") item: ItemStack,
        @Param(name = "amount", defaultValue = "1") amount: Int
    ) {
        if (amount < 1) {
            sender.sendMessage(ChatColor.RED.toString() + "The amount must be greater than zero.")
            return
        }
        item.amount = amount
        target.inventory.addItem(item)
        sender.sendMessage(
            ChatColor.GOLD.toString() + "Giving " + ChatColor.WHITE + target.displayName + ChatColor.WHITE + " " + amount + ChatColor.GOLD + " of " + ChatColor.WHITE + ItemUtils.getName(
                item
            ) + ChatColor.GOLD + "."
        )
    }

}