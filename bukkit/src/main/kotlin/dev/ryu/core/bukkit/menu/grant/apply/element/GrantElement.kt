package dev.ryu.core.bukkit.menu.grant.apply.element

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.prompt.grant.GrantReasonPrompt
import com.starlight.nexus.menu.button.Button
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.GrantModule
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 10:52
*/

class GrantElement(private val rank: Rank, private val target: Profile) : Button() {

    override fun getName(player: Player): String {
        return Color.color(this.rank.display)
    }

    override fun getMaterial(player: Player): Material {
        return Material.WOOL
    }

    override fun getDescription(player: Player): MutableList<String> {
        return arrayListOf<String>().also { toReturn ->

            if (!this.rank.isGrantable()) {
                toReturn.add("")
                toReturn.add("${ChatColor.RED}That rank is not grantable.")
            } else {
                toReturn.add("")
                toReturn.add(styleAction(ChatColor.GREEN, "CLICK", "to give that rank to player."))
            }

        }
    }

    override fun getDamageValue(player: Player): Byte {
        return Color.getWoolData(this.rank.color)
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (this.rank == dev.ryu.core.shared.CoreAPI.rankManager.defaultRank) {
            player.sendMessage("${ChatColor.RED}That rank can't be granted as it is default rank.")
            return
        }

        if (GrantModule.repository.findAllByPlayer(target.id).any { it.getRank()!!.id == rank.id && !it.isRemoved() }) {
            player.sendMessage("${ChatColor.RED}Grant cannot be given as the player already possesses it.")
            return
        }

        player.closeInventory()
        player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(GrantReasonPrompt(this.rank,this.target, player)
        ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
    }

}