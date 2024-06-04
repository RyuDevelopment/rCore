package dev.ryu.core.bukkit.menu.grant.apply

import dev.ryu.core.bukkit.menu.grant.apply.element.GrantElement
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.PaginatedMenu
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 10:52
*/

class GrantMenu(
    val profile: Profile
) : PaginatedMenu() {

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GOLD}${ChatColor.BOLD}Choose a Rank"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val senderProfile = ProfileModule.findById(player.uniqueId)!!

        val allRanks = if (senderProfile.isSuperUser) {
            dev.ryu.core.shared.Shared.rankManager.repository.findAllRanks().filter { GrantModule.findBestRank(player.uniqueId).weight >= it.weight}.sortedByDescending { it.weight }
        } else {
            dev.ryu.core.shared.Shared.rankManager.repository.findAllRanks().filter {!it.isDefault() && !it.isHidden() && GrantModule.findBestRank(player.uniqueId).weight >= it.weight}.sortedByDescending { it.weight }
        }

        return allRanks.withIndex().associate { it.index to GrantElement(it.value, this.profile) }.toMutableMap()
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
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