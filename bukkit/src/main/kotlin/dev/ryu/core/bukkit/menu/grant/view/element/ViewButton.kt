package dev.ryu.core.bukkit.menu.grant.view.element

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.grant.view.GrantsMenu
import dev.ryu.core.bukkit.prompt.grant.GrantRemoveReasonPrompt
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.util.time.TimeUtil
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 12:58
*/

class ViewButton(private val grant: Grant, val target: Profile) : Button() {

    private val rank = this.grant.getRank()
    private val voided = this.grant.isVoided()
    private val removed = this.grant.isRemoved()

    override fun getName(player: Player): String {
        return "${ChatColor.GOLD}${TimeUtil.formatIntoCalendarString(Date(this.grant.created))}"
    }

    override fun getMaterial(player: Player): Material {
        return Material.WOOL
    }

    override fun getDescription(player: Player): List<String> {
        val toReturn = ArrayList<String>()

        toReturn.add("")

        if (this.grant.sender == UUID.fromString(Profile.CONSOLE_UUID)) {
            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}By${ChatColor.GRAY}: ${ChatColor.DARK_RED}${ChatColor.BOLD}Console")
        } else {
            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}By${ChatColor.GRAY}: ${ChatColor.WHITE}${ChatColor.valueOf(
                GrantModule.findBestRank(this.grant.sender).color)}${ProfileModule.findById(this.grant.sender)!!.name}")
        }

        toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Rank${ChatColor.GRAY}: ${ChatColor.WHITE}${Color.color(this.rank!!.display)}")
        toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reason${ChatColor.GRAY}: ${ChatColor.WHITE}${this.grant.reason}")

        if (!this.grant.isPermanent() && !this.voided) {
            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Duration: ${ChatColor.WHITE}${TimeUtil.formatIntoDetailedString(this.grant.duration)}")
        }

        when {
            this.removed -> {
                toReturn.add("")
                toReturn.add("${ChatColor.RED}${ChatColor.BOLD}Removed")
                toReturn.add(" ")

                toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}By${ChatColor.GRAY}: ${ChatColor.WHITE}${ProfileModule.findById(this.grant.remover!!)!!.name}")
                toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reason${ChatColor.GRAY}: ${ChatColor.WHITE}${this.grant.removedReason}")
                toReturn.add("")
                toReturn.add("${ChatColor.GOLD}${TimeUtil.formatIntoCalendarString(Date(this.grant.removed!!))}")
                toReturn.add("")
                toReturn.add(styleAction(ChatColor.RED, "CLICK", "to remove rank cache from ${target.name} grants"))
            } this.voided -> {
                toReturn.add("")
                toReturn.add("${ChatColor.RED}${ChatColor.BOLD}Expired")
                toReturn.add("")
                toReturn.add("${ChatColor.GOLD}${TimeUtil.formatIntoCalendarString(Date(this.grant.getVoidedAt()))}")
                toReturn.add("")
                toReturn.add(styleAction(ChatColor.RED, "CLICK", "to remove rank cache from ${target.name} grants"))
            } else -> {
                toReturn.add("")
                if (this.grant.rank == "Default") {
                    toReturn.add("${ChatColor.RED}Grant not removable.")
                } else {
                    toReturn.add("${ChatColor.RED}Click to remove grant.")
                }
            }
        }

        return toReturn
    }

    override fun getDamageValue(player: Player): Byte {
        return (if (this.removed) DyeColor.RED else if (this.voided) DyeColor.ORANGE else DyeColor.GREEN).woolData
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {

        if (this.grant.rank == "Default") {
            return
        }

        val senderProfile = ProfileModule.findById(player.uniqueId)!!

        if (this.voided || this.removed) {

            if (senderProfile.isSuperUser) {
                player.closeInventory()

                GrantModule.repository.delete(this.grant)

                val allGrants: Set<Grant> = GrantModule.repository.findAllByPlayer(this.target.id)

                GrantsMenu(allGrants, target).openMenu(player)
                return
            }

            return
        }

        player.closeInventory()
        player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
            GrantRemoveReasonPrompt(this.grant, player, target)
        ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
    }

}