package dev.ryu.core.bukkit.menu.server.deploy.custom.spigot

import dev.ryu.core.bukkit.menu.server.deploy.custom.CustomPreset
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn
    * Project: core
    * Date: 27/2/2024 - 23:40
*/

class SpigotsMenu : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}All Spigots"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for ((index, spigot) in CustomServerInfo.spigotsAvailables.withIndex()) {
                toReturn[index] = MenuButton()
                    .icon(Material.COMMAND)
                    .name("${ChatColor.AQUA}${StringUtils.capitalize(spigot.toLowerCase().replace("_"," "))} Spigot")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select spigot."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        CustomServerInfo.spigot = StringUtils.capitalize(spigot.toLowerCase().replace("_"," "))
                        CustomPreset(player).openMenu(player)
                    }
            }

        }
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            toReturn[4] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    CustomPreset(player).openMenu(player)
                }
        }
    }

    override fun size(player: Player): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

}