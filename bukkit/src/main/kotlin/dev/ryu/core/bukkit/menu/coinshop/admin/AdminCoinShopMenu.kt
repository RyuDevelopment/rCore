package dev.ryu.core.bukkit.menu.coinshop.admin

import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.TextSplitter
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.coinshop.admin.editor.CoinShopEditorMenu
import dev.ryu.core.bukkit.util.Constants
import dev.ryu.core.bukkit.util.protocol
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class AdminCoinShopMenu : PaginatedMenu() {

    override fun getPrePaginatedTitle(p0: Player): String {
        return "${ChatColor.GRAY}Coin Shop Configuration"
    }

    override fun getAllPagesButtons(p0: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->

            var index = 0
            Core.get().config.getStringList("coinshop_menus").forEach { menu ->

                toReturn[index] = MenuButton()
                    .icon(Material.PAPER)
                    .name("${ChatColor.AQUA}${menu.capitalize()}")
                    .lore {
                        mutableListOf<String>().also { desc ->
                            desc.add("")
                            desc.add(Button.styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage menu"))
                            desc.add(Button.styleAction(ChatColor.RED, "RIGHT-CLICK", "to delete menu"))
                        }
                    }
                    .action(ClickType.LEFT) {
                        p0.closeInventory()
                        CoinShopEditorMenu(menu).open(p0)
                    }

                index++
            }
        }
    }

    override fun getGlobalButtons(p0: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 3)] = GlassButton(7)
            }

            if (p0.protocol <= 20) {
                toReturn[4] = MenuButton()
                    .icon(Material.EMERALD)
                    .name("${ChatColor.GREEN}Create new menu!")
                    .lore {
                        mutableListOf<String>().also { desc ->
                            desc.add("")
                            desc.addAll(TextSplitter.split(text = "Create a new sub menu for the coinshop and configure it to your liking!"))
                            desc.add("")
                            desc.add(Button.styleAction(ChatColor.GREEN, "CLICK", "to create a new menu!"))
                        }
                    }
            } else {
                toReturn[4] = MenuButton()
                    .texturedIcon(Constants.GREEN_PLUS_TEXTURE)
                    .name("${ChatColor.GREEN}Create new menu!")
                    .lore {
                        mutableListOf<String>().also { desc ->
                            desc.add("")
                            desc.addAll(TextSplitter.split(text = "Create a new sub menu for the coinshop and configure it to your liking!"))
                            desc.add("")
                            desc.add(Button.styleAction(ChatColor.GREEN, "CLICK", "to create a new menu!"))
                        }
                    }
            }
        }
    }

    override fun size(player: Player?): Int {
        return 36
    }

    override fun getMaxItemsPerPage(player: Player?): Int {
        return 18
    }

}