package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.util.ItemUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.util.*

object RepairCommand {

    @Command(names = ["repair", "fix"], permission = "core.repair")
    @JvmStatic
    fun repair(sender: Player) {
        if (sender.hasPermission("core.repair.all")) {
            sender.sendMessage(ChatColor.RED.toString() + "/repair <hand|all>")
        } else if (sender.hasPermission("core.repair")) {
            sender.sendMessage(ChatColor.RED.toString() + "/repair <hand>")
        }
    }

    @Command(
        names = ["repair hand", "fix hand"],
        permission = "core.repair",
        description = "Repair the item you're currently holding"
    )
    @JvmStatic
    fun repair_hand(sender: Player) {
        val item = sender.itemInHand
        if (item == null || item.type == Material.AIR) {
            sender.sendMessage(ChatColor.RED.toString() + "You must be holding an item.")
            return
        }
        if (item.durability.toInt() == 0) {
            sender.sendMessage(ChatColor.RED.toString() + "That " + ChatColor.WHITE + ItemUtils.getName(item) + ChatColor.RED + " already has max durability.")
            return
        }
        item.durability = 0.toShort()
        sender.sendMessage(ChatColor.GOLD.toString() + "Your " + ChatColor.WHITE + ItemUtils.getName(item) + ChatColor.GOLD + " has been repaired.")
    }

    @Command(
        names = ["repair all", "fix all"],
        permission = "core.repair.all",
        description = "Repair all items in your inventory"
    )
    @JvmStatic
    fun repair_all(sender: Player) {
        val toRepair = HashSet<ItemStack>()
        val playerInventory = sender.inventory
        toRepair.addAll(Arrays.asList(*playerInventory.contents))
        toRepair.addAll(Arrays.asList(*playerInventory.armorContents))
        for (item in toRepair) {
            if (item == null || item.type == Material.AIR || !isRepairble(item) || item.durability.toInt() == 0) continue
            item.durability = 0.toShort()
        }
        sender.sendMessage(ChatColor.GOLD.toString() + "All " + ChatColor.WHITE + "items" + ChatColor.GOLD + " in your inventory has been repaired.")
    }

    private fun isRepairble(item: ItemStack?): Boolean {
        return item != null && (item.type == Material.WOOD_AXE || item.type == Material.WOOD_HOE || item.type == Material.WOOD_SWORD || item.type == Material.WOOD_SPADE || item.type == Material.WOOD_PICKAXE || item.type == Material.STONE_AXE || item.type == Material.STONE_HOE || item.type == Material.STONE_SWORD || item.type == Material.STONE_SPADE || item.type == Material.STONE_PICKAXE || item.type == Material.GOLD_AXE || item.type == Material.GOLD_HOE || item.type == Material.GOLD_SWORD || item.type == Material.GOLD_SPADE || item.type == Material.GOLD_PICKAXE || item.type == Material.IRON_AXE || item.type == Material.IRON_HOE || item.type == Material.IRON_SWORD || item.type == Material.IRON_SPADE || item.type == Material.IRON_PICKAXE || item.type == Material.DIAMOND_AXE || item.type == Material.DIAMOND_HOE || item.type == Material.DIAMOND_SWORD || item.type == Material.DIAMOND_SPADE || item.type == Material.DIAMOND_PICKAXE || item.type == Material.DIAMOND_BOOTS || item.type == Material.DIAMOND_CHESTPLATE || item.type == Material.DIAMOND_HELMET || item.type == Material.DIAMOND_LEGGINGS || item.type == Material.IRON_BOOTS || item.type == Material.IRON_CHESTPLATE || item.type == Material.IRON_HELMET || item.type == Material.IRON_LEGGINGS || item.type == Material.GOLD_BOOTS || item.type == Material.GOLD_CHESTPLATE || item.type == Material.GOLD_HELMET || item.type == Material.GOLD_LEGGINGS || item.type == Material.DIAMOND_BOOTS || item.type == Material.DIAMOND_CHESTPLATE || item.type == Material.DIAMOND_HELMET || item.type == Material.DIAMOND_LEGGINGS || item.type == Material.CHAINMAIL_HELMET || item.type == Material.CHAINMAIL_CHESTPLATE || item.type == Material.CHAINMAIL_LEGGINGS || item.type == Material.CHAINMAIL_BOOTS || item.type == Material.LEATHER_HELMET || item.type == Material.LEATHER_CHESTPLATE || item.type == Material.LEATHER_LEGGINGS || item.type == Material.LEATHER_BOOTS || item.type == Material.FISHING_ROD || item.type == Material.CARROT_STICK || item.type == Material.SHEARS || item.type == Material.FLINT_AND_STEEL || item.type == Material.BOW)
    }

}