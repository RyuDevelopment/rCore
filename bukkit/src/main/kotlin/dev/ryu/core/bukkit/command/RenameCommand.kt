package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object RenameCommand {

    var customNameStarter = ChatColor.translateAlternateColorCodes('&', "&b&c&f")

    @Command(
        names = ["rename"],
        permission = "core.rename",
        description = "Rename the item you're currently holding. Supports color codes"
    )
    @JvmStatic
    fun rename(sender: Player, @Param(name = "name", wildcard = true) name: String) {
        var name = name
        val item = sender.itemInHand
        if (sender.hasPermission("core.rename")) {
            name = ChatColor.translateAlternateColorCodes('&', name)
        }
        if (!sender.isOp && name.length >= 40) {
            sender.sendMessage("${ChatColor.RED}The maximum characters you can set to an rename is 40.")
            return
        }
        if (!sender.isOp && (sender.itemInHand.type == Material.INK_SACK || sender.itemInHand.type == Material.TRIPWIRE_HOOK || sender.itemInHand.type == Material.ENDER_CHEST || sender.itemInHand.type == Material.TRAPPED_CHEST || sender.itemInHand.type == Material.CHEST || sender.itemInHand.type == Material.BEACON || sender.itemInHand.type == Material.ANVIL || sender.itemInHand.type == Material.HOPPER || sender.itemInHand.type == Material.HOPPER_MINECART || sender.itemInHand.type == Material.DISPENSER || sender.itemInHand.type == Material.DROPPER || sender.itemInHand.type == Material.STORAGE_MINECART || sender.itemInHand.type == Material.FURNACE)) {
            sender.sendMessage(Color.color("&cYou can't rename this item!"))
            return
        }
        if (!sender.isOp && sender.gameMode != GameMode.CREATIVE) {
            if (item!!.hasItemMeta() && item.itemMeta.hasLore()) {
                for (lore in item.itemMeta.lore) {
                    if (lore == Color.color("&cUnrepairable")) {
                        sender.sendMessage(Color.color("&cYou can't rename this item!"))
                        return
                    }
                }
            }
        }
        if (item == null) {
            sender.sendMessage(ChatColor.RED.toString() + "You must be holding an item.")
            return
        }
        val isCustomEnchant =
            item.hasItemMeta() && item.itemMeta.hasDisplayName() && item.itemMeta.displayName.startsWith(
                customNameStarter
            )
        val meta = item.itemMeta
        meta.displayName =
            if (isCustomEnchant && !name.startsWith(customNameStarter)) customNameStarter + name else name
        item.setItemMeta(meta)
        sender.updateInventory()
        sender.sendMessage(
            ChatColor.GOLD.toString() + "Renamed your " + ChatColor.WHITE + ItemUtils.getName(
                ItemStack(
                    item.type,
                    item.amount,
                    item.durability
                )
            ) + ChatColor.GOLD + " to " + ChatColor.WHITE + name + ChatColor.GOLD + "."
        )
    }

}