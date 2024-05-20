package dev.ryu.core.bukkit.menu.session

import dev.ryu.core.bukkit.menu.session.element.SessionElement
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.SessionModule
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 5:00 PM
*/

class SessionMenu(
    val target: Profile
) : PaginatedMenu() {

    init {
        isAutoUpdate = true
    }

    override fun getPrePaginatedTitle(p0: Player): String {
        return "${ChatColor.GRAY}${target.name} Sessions"
    }

    override fun getAllPagesButtons(p0: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            val sessions = SessionModule.repository.findAllSessionsById(target.id).sortedByDescending { it.joinedAt }

            var slot = 0
            sessions.forEach {
                toReturn[slot] = SessionElement(it)
                slot++
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