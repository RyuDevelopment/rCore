package dev.ryu.core.bukkit.menu.server

import dev.ryu.core.bukkit.menu.server.deploy.SelectPreset
import dev.ryu.core.bukkit.menu.server.element.ServerButton
import dev.ryu.core.bukkit.util.protocol
import dev.ryu.core.shared.system.module.NetworkModule
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.util.TextSplitter
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:37
*/

class ServerManager : PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Server Manager"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        return NetworkModule.servers.sortedByDescending{it.created}.withIndex().associate{it.index to ServerButton(it.value) }.toMutableMap()
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

        for (i in 0 .. 8) {
            toReturn[getSlot(i,0)] = GlassButton(7)
            toReturn[getSlot(i,3)] = GlassButton(7)
        }

        val lore: MutableList<String> = mutableListOf()

        lore.add("")
        lore.addAll(TextSplitter.split(text = "${ChatColor.GRAY}Deploy a new server pre-configured according to your needs, you can choose different categories or create your own custom preset."))
        lore.add("")
        lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to deploy a server!"))

        if (player.protocol <= 20) {
            toReturn[4] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.GREEN}Deploy a new Server!")
                .lore(lore)
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    SelectPreset().openMenu(player)
                }
        } else {
            toReturn[4] = MenuButton()
                .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2Q3YmNhNDMwNmI0ZmY3YWE4YzQ5MzM1YzA5MWMyYjE4OWI3MTNmMTc2ZDI1OTY2NDdjYzNmNThhOTRkOTlkIn19fQ==")
                .name("${ChatColor.GREEN}Deploy a new Server!")
                .lore(lore)
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    SelectPreset().openMenu(player)
                }
            }
        }
    }

    override fun size(player: Player): Int {
        return 4*9
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 18
    }

}