package dev.ryu.core.bukkit.menu.grant.view

import dev.ryu.core.bukkit.menu.grant.view.element.ViewButton
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.RankModule
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.util.time.TimeUtil
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 12:58
*/

class GrantsMenu(private val grants: Set<Grant>, val profile: Profile) : PaginatedMenu() {

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.valueOf(GrantModule.findBestRank(this.profile.id).color)}${this.profile.name}${ChatColor.GRAY}'s grants"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        return this.grants.filter{player.hasPermission("core.grant.${it.rank}")}.sortedByDescending{it.created}.withIndex().associate{it.index to ViewButton(it.value, this.profile) }.toMutableMap()
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            val currentGrant = this.grants.maxByOrNull { it.getRank()!!.weight }!!

            toReturn[4] = MenuButton()
                .name("${ChatColor.valueOf(GrantModule.findBestRank(this.profile.id).color)}${this.profile.name}")
                .playerTexture(this.profile.name!!)
                .lore(
                    arrayListOf<String>().also { toReturn ->

                        toReturn.add("${ChatColor.GOLD}${ChatColor.BOLD}Profile")
                        toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Id${ChatColor.GRAY}: ${ChatColor.WHITE}${this.profile.id.toString().replace("-", "")}")
                        toReturn.add("")
                        toReturn.add("${ChatColor.GOLD}${ChatColor.BOLD}Rank")
                        toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Name${ChatColor.GRAY}: ${ChatColor.WHITE}${Color.color(
                            GrantModule.findBestRank(this.profile.id).display)}")
                        if (currentGrant.getRank()!!.id != RankModule.defaultRank.id) {
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Prefix${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (currentGrant.getRank()?.prefix == null) "${ChatColor.RED}Not Set" else Color.color(
                                GrantModule.findBestRank(this.profile.id).prefix!!)}")
                        }
                        toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Weight${ChatColor.GRAY}: ${ChatColor.WHITE}${GrantModule.findBestRank(this.profile.id).weight}")
                        toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Expires${ChatColor.GRAY}: ${ChatColor.WHITE}${if (currentGrant.isPermanent()) "Never" else TimeUtil.formatIntoDetailedString(currentGrant.duration)}")
                    }
                )


        }
    }

    override fun size(player: Player): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

}