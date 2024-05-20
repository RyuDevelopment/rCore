package dev.ryu.core.bukkit.menu.rank.editor

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.rank.RankEditor
import dev.ryu.core.bukkit.menu.rank.editor.element.CodeButton
import dev.ryu.core.bukkit.prompt.rank.CodeCreateDurationPrompt
import dev.ryu.core.bukkit.prompt.rank.CodeCreateReasonPrompt
import dev.ryu.core.bukkit.util.Constants
import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.CodeModule
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 16:11
*/

class RankCodes(val rank: Rank) : PaginatedMenu(){

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Codes Editor"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        val allCodes = CodeModule.repository.findAllByRank(rank).sortedWith(compareBy(Code::isRedeemed).thenByDescending { it.createdAt })

        val finalList = allCodes.mapIndexed { index, code -> index to CodeButton(code, this.rank) }.toMap().toMutableMap()

        return finalList
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0..8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            toReturn[3] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    RankEditor(this.rank).openMenu(player)
                }

            toReturn[5] = MenuButton()
                .texturedIcon(Constants.GREEN_PLUS_TEXTURE)
                .name("${ChatColor.GREEN}Generate Code")
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(CodeCreateReasonPrompt(player, this.rank)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
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