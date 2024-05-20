package dev.ryu.core.bukkit.menu.coinshop

import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.Color
import dev.ryu.core.bukkit.Core
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class CoinShopMenu: Menu() {

    override fun getTitle(player: Player): String {
        return Color.color(Core.get().coinshopFiles[0].config.getString("MENU.TITLE"))
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            val section = Core.get().coinshopFiles[0].config.getConfigurationSection("ITEMS")
            val repeat = Core.get().coinshopFiles[0].config.getConfigurationSection("ITEMS-REPEATED")

            for (keys in repeat.getKeys(false)) {
                for (i in repeat.getIntegerList("$keys.SLOT")) {
                    toReturn[i] = MenuButton()
                        .name(Color.color(repeat.getString("$keys.DISPLAY")))
                        .icon(Material.valueOf(repeat.getString("$keys.ICON")))
                        .data(repeat.getInt("$keys.DATA").toByte())
                        .lore { _ ->
                            arrayListOf<String>().also { desc ->
                                if (!repeat.getStringList("$keys.LORE").contains("{CLEAR_LORE}")) {
                                    desc.addAll(Color.color(repeat.getStringList("$keys.LORE")))
                                }
                            }
                        }
                        .action(ClickType.valueOf(repeat.getString("$keys.CLICK_TYPE"))) {
                            when (repeat.getString("$keys.ACTION")) {
                                "{CLOSE_MENU}" -> player.closeInventory()
                                "{NONE}" -> return@action
                            }
                        }
                }
            }

            for (keys in section.getKeys(false)) {
                toReturn[section.getInt("$keys.SLOT")] = MenuButton()
                    .name(Color.color(section.getString("$keys.DISPLAY")))
                    .data(section.getInt("$keys.DATA").toByte())
                    .icon(Material.valueOf(section.getString("$keys.ICON")))
                    .lore { _ ->
                        arrayListOf<String>().also { desc ->
                            if (!section.getStringList("$keys.LORE").contains("{CLEAR_LORE}")) {
                                desc.addAll(Color.color(section.getStringList("$keys.LORE")))
                            }
                        }
                    }
                    .action(ClickType.valueOf(section.getString("$keys.CLICK_TYPE"))) {
                        when (section.getString("$keys.ACTION")) {
                            //OPENS THE RANK SHOP MENU
                            "{OPEN_RANK_SHOP}" -> {

                            }

                            //OPENS THE TAGS SHOP MENU
                            "{OPEN_TAGS_SHOP}" -> {

                            }

                            //OPENS THE CUSTOM NICK SHOP
                            "{OPEN_CUSTOM_NICK_SHOP}" -> {

                            }

                            //OPENS THE CHAT COLOR SHOP
                            "{OPEN_CHAT_COLOR_SHOP}" -> {

                            }

                            //OPENS THE EXTRA MENU SHOP
                            "{OPEN_EXTRA_MENU}" -> {

                            }

                            //OPENS THE DISGUISE TOKEN SHOP
                            "{OPEN_DISGUISE_SHOP}" -> {

                            }

                            //OPENS THE COSMETIC SHOP MENU
                            "{OPEN_COSMETIC_SHOP}" -> {

                            }

                            //OPENS THE COUPONS MENU
                            "{OPEN_COUPONS_MENU}" -> {

                            }

                            //OPENS THE CART MENU
                            "{OPEN_CART_MENU}" -> {

                            }

                            "{CLOSE_MENU}" -> player.closeInventory()
                            "{NONE}" -> return@action
                        }
                    }
            }
        }
    }

    override fun size(player: Player): Int {
        return Core.get().coinshopFiles[0].config.getInt("MENU.SIZE")
    }

}