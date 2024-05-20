package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.flag.Flag
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.ItemUtils
import com.starlight.nexus.util.enchantment.EnchantmentWrapper
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

object EnchantCommand {

    @Command(names = ["enchant"], permission = "core.admin", description = "Enchant an item")
    @JvmStatic
    fun enchant(
        sender: Player,
        @Flag(value = ["h", "hotbar"], description = "Enchant your entire hotbar") hotbar: Boolean,
        @Param(name = "enchantment") enchantment: Enchantment,
        @Param(name = "level", defaultValue = "1") level: Int
    ) {
        if (level <= 0) {
            sender.sendMessage(ChatColor.RED.toString() + "The level must be greater than 0.")
            return
        }
        if (!hotbar) {
            val item = sender.itemInHand
            if (item == null) {
                sender.sendMessage(ChatColor.RED.toString() + "You must be holding an item.")
                return
            }
            val wrapper: EnchantmentWrapper = EnchantmentWrapper.parse(enchantment)
            if (level > wrapper.maxLevel) {
                if (!sender.hasPermission("core.enchant.bypass")) {
                    sender.sendMessage(ChatColor.RED.toString() + "The maximum enchanting level for " + wrapper.friendlyName + " is " + level + ". You provided " + level + ".")
                    return
                }
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "WARNING: " + ChatColor.YELLOW + "You added " + wrapper.friendlyName + " " + level + " to this item. The default maximum value is " + wrapper.maxLevel + ".")
            }
            wrapper.enchant(item, level)
            sender.updateInventory()
            sender.sendMessage(ChatColor.GOLD.toString() + "Enchanted your " + ChatColor.WHITE + ItemUtils.getName(item) + ChatColor.GOLD + " with " + ChatColor.WHITE + wrapper.friendlyName + ChatColor.GOLD + " level " + ChatColor.WHITE + level + ChatColor.GOLD + ".")
        } else {
            val wrapper2: EnchantmentWrapper = EnchantmentWrapper.parse(enchantment)
            if (level > wrapper2.maxLevel && !sender.hasPermission("core.enchant.bypass")) {
                sender.sendMessage(ChatColor.RED.toString() + "The maximum enchanting level for " + wrapper2.friendlyName + " is " + level + ". You provided " + level + ".")
                return
            }
            var enchanted = 0
            for (slot in 0..8) {
                val item2 = sender.inventory.getItem(slot)
                if (item2 == null || !wrapper2.canEnchantItem(item2)) continue
                wrapper2.enchant(item2, level)
                ++enchanted
            }
            if (enchanted == 0) {
                sender.sendMessage(ChatColor.RED.toString() + "No items in your hotbar can be enchanted with " + wrapper2.friendlyName + ".")
                return
            }
            if (level > wrapper2.maxLevel) {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "WARNING: " + ChatColor.YELLOW + "You added " + wrapper2.friendlyName + " " + level + " to these items. The default maximum value is " + wrapper2.maxLevel + ".")
            }
            sender.sendMessage(ChatColor.GOLD.toString() + "Enchanted " + ChatColor.WHITE + enchanted + ChatColor.GOLD + " items with " + ChatColor.WHITE + wrapper2.friendlyName + ChatColor.GOLD + " level " + ChatColor.WHITE + level + ChatColor.GOLD + ".")
            sender.updateInventory()
        }
    }
    
}