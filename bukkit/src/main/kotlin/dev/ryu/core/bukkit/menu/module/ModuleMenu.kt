package dev.ryu.core.bukkit.menu.module

import dev.ryu.core.bukkit.menu.module.element.ModuleElement
import dev.ryu.core.linker.manager.ModuleManager
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 2:51 PM
*/

class ModuleMenu : PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(p0: Player): String {
        return "${ChatColor.GRAY}Manage Modules"
    }

    override fun getAllPagesButtons(p0: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            var slot = 0
            for (entry in ModuleManager.modules) {
                for (moduleEntry in entry.value) {
                    toReturn[slot] = ModuleElement(moduleEntry.key)
                    slot++
                }
            }

        }
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            for (i in 0..8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }
        }
    }

}