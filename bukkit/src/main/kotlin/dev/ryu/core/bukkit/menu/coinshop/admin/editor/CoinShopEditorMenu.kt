package dev.ryu.core.bukkit.menu.coinshop.admin.editor

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.ItemBuilder
import dev.ryu.core.bukkit.Core
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class CoinShopEditorMenu(
    val name: String
) : Listener {

    val inventory = Bukkit.createInventory(
        null,
        Core.get().getCoinShopFileById(name).config.getInt("MENU.SIZE"),
        Color.color(Core.get().getCoinShopFileById(name).config.getString("MENU.TITLE"))
    )

    ///NO ANDA NI PINGO ESTO LPM

    init {
        Core.get().server.pluginManager.registerEvents(this, Core.get())

        val repeat = Core.get().getCoinShopFileById(name).config.getConfigurationSection("ITEMS-REPEATED")
        val section = Core.get().getCoinShopFileById(name).config.getConfigurationSection("ITEMS")

        for (keys in repeat.getKeys(false)) {
            for (i in repeat.getIntegerList("$keys.SLOT")) {
                val lore = mutableListOf<String>()

                if (!repeat.getStringList("$keys.LORE").contains("{CLEAR_LORE}")) {
                    lore.addAll(Color.color(repeat.getStringList("$keys.LORE")))
                }

                inventory.setItem(i, ItemBuilder.of(Material.valueOf(repeat.getString("$keys.ICON")))
                    .name(Color.color(repeat.getString("$keys.DISPLAY")))
                    .data(repeat.getInt("$keys.DATA").toShort())
                    .setLore(Color.color(lore))
                    .build()
                )
            }
        }

        for (keys in section.getKeys(false)) {
            val lore = mutableListOf<String>()

            if (!section.getStringList("$keys.LORE").contains("{CLEAR_LORE}")) {
                lore.addAll(Color.color(section.getStringList("$keys.LORE")))
            }

            inventory.setItem(section.getInt("$keys.SLOT"), ItemBuilder.of(Material.valueOf(section.getString("$keys.ICON")))
                .name(Color.color(section.getString("$keys.DISPLAY")))
                .data(section.getInt("$keys.DATA").toShort())
                .setLore(Color.color(lore))
                .build()
            )
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val player = event.player as Player

        val inventory = event.inventory
        val itemsJson = mutableMapOf<String, Any>()

        for (slot in 0 until inventory.size) {
            val itemStack = inventory.getItem(slot)
            if (itemStack != null) {
                val itemJson = mutableMapOf<String, Any>()
                itemJson["SLOT"] = slot
                itemJson["DISPLAY"] = itemStack.itemMeta?.displayName ?: "&7"
                itemJson["DATA"] = itemStack.durability.toInt()
                itemJson["ICON"] = itemStack.type.toString()
                itemJson["LORE"] = itemStack.itemMeta?.lore ?: mutableListOf<String>()

                itemsJson["ITEM_$slot"] = itemJson
            }
        }

        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(itemsJson)

        Core.get().getCoinShopFileById(name).config.set("ITEMS", gson.fromJson(jsonString, JsonObject::class.java))
        Core.get().getCoinShopFileById(name).save()

        player.sendMessage("${ChatColor.GREEN}${name.capitalize()} Coin Shop Menu has been successfully modified.")
        player.playSound(player.location, Sound.LEVEL_UP, 15.0f, 25.0f)
    }

    fun open(player: Player) {
        player.openInventory(inventory)
    }

}