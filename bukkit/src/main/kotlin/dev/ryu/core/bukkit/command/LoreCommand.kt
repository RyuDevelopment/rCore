package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Consumer

object LoreCommand {

    @Command(names = ["addlore"], permission = "core.admin")
    @JvmStatic
    fun addLore(player: Player, @Param(name = "lore", wildcard = true) name: String) {
        val item = player.itemInHand
        if (item == null || item.type == Material.AIR) {
            player.sendMessage(ChatColor.RED.toString() + "You need to put a item in your hand.")
            return
        }
        val itemMeta = item.itemMeta
        if (!itemMeta.hasLore()) {
            itemMeta.lore = ArrayList()
        }
        val add: String = Color.color(name)
        val lore = itemMeta.lore
        lore.add(add)
        itemMeta.lore = lore
        item.setItemMeta(itemMeta)
        player.updateInventory()
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully added a new lore to the item.")
    }

    @Command(names = ["editlore list"], permission = "core.admin")
    @JvmStatic
    fun editLoreList(player: Player) {
        val itemStack = player.itemInHand
        if (itemStack == null) {
            player.sendMessage(ChatColor.RED.toString() + "Invalid item in hand.")
            return
        }
        if (!itemStack.hasItemMeta()) {
            player.sendMessage(ChatColor.RED.toString() + "No information found in the item.")
            return
        }
        player.sendMessage("")
        player.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Item Information:")
        player.sendMessage(ChatColor.WHITE.toString() + "Name: " + ChatColor.LIGHT_PURPLE + if (itemStack.itemMeta.hasDisplayName()) itemStack.itemMeta.displayName else "None")
        player.sendMessage(ChatColor.WHITE.toString() + "Lore:")
        if (itemStack.itemMeta.hasLore()) {
            val id = AtomicInteger(0)
            itemStack.itemMeta.lore.forEach(Consumer<String> { lore: String ->
                player.sendMessage(
                    Color.color("&7" + id.getAndIncrement() + ". &f" + lore)
                )
            })
        } else {
            player.sendMessage(ChatColor.GRAY.toString() + "- This item has no Lore.")
        }
        player.sendMessage("")
    }

    @Command(names = ["editlore removelore", "editlore remlore"], permission = "core.admin")
    @JvmStatic
    fun editLoreRemove(player: Player, @Param(name = "id") line: Int) {
        val item = player.itemInHand
        if (item == null || item.type == Material.AIR) {
            player.sendMessage(ChatColor.RED.toString() + "You have to put an item in your hand.")
            return
        }
        val itemMeta = item.itemMeta
        if (!itemMeta.hasLore() || line >= itemMeta.lore.size) {
            player.sendMessage(ChatColor.RED.toString() + "Failed to remove lore on this item.")
            return
        }
        val lore = itemMeta.lore
        lore.removeAt(line)
        itemMeta.lore = lore
        item.setItemMeta(itemMeta)
        player.updateInventory()
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully removed that line from the lore.")
    }

    @Command(names = ["editlore addlore"], permission = "core.admin")
    @JvmStatic
    fun editLoreAdd(player: Player, @Param(name = "name", wildcard = true) name: String) {
        val item = player.itemInHand
        if (item == null || item.type == Material.AIR) {
            player.sendMessage(ChatColor.RED.toString() + "You need to put a item in your hand.")
            return
        }
        val itemMeta = item.itemMeta
        if (!itemMeta.hasLore()) {
            itemMeta.lore = ArrayList()
        }
        val add: String = Color.color(name)
        val lore = itemMeta.lore
        lore.add(add)
        itemMeta.lore = lore
        item.setItemMeta(itemMeta)
        player.updateInventory()
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully added a new lore to the item.")
    }

    @Command(names = ["editlore editline", "editlore setline"], permission = "core.admin")
    @JvmStatic
    fun editLoreLine(
        player: Player,
        @Param(name = "id") id: Int,
        @Param(name = "line", wildcard = true) name: String
    ) {
        val item = player.itemInHand
        if (item == null || item.type == Material.AIR) {
            player.sendMessage(ChatColor.RED.toString() + "You need to put a item in your hand.")
            return
        }
        val itemMeta = item.itemMeta
        if (!itemMeta.hasLore()) {
            itemMeta.lore = ArrayList()
        }
        val add: String = Color.color(name)
        val lore = itemMeta.lore
        lore.add(id, add)
        itemMeta.lore = lore
        item.setItemMeta(itemMeta)
        player.updateInventory()
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully added a new lore to the item.")
    }
    
}