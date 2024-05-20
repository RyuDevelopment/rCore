package dev.ryu.core.bukkit.menu.rank

import com.google.common.collect.Lists
import com.starlight.nexus.menu.ConfirmMenu
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.Callback
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.TextSplitter
import com.starlight.nexus.util.UnicodeUtil
import dev.ryu.core.bukkit.prompt.rank.RankCreatePrompt
import dev.ryu.core.bukkit.util.Constants
import dev.ryu.core.bukkit.util.protocol
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.RankModule
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 14:46
*/

class RankCommand : PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Rank Editor"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            val ranks = getAllowedRanks()
            for (i in ranks.indices) {
                val currentRank = ranks[i]

                toReturn[i] = object : Button() {

                    override fun getName(player: Player): String {
                        return Color.color(currentRank.display)
                    }

                    override fun getDescription(player: Player): List<String> {
                        return arrayListOf<String>().also { toReturn->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Color${ChatColor.GRAY}: ${ChatColor.valueOf(currentRank.color)}${Color.convert(currentRank.color)}")
                            if (currentRank.id != RankModule.defaultRank.id) {
                                toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Prefix${ChatColor.GRAY}: ${ChatColor.WHITE}${if (Objects.equals(currentRank.prefix, null)) "${ChatColor.RED}Not Set" else Color.color(currentRank.prefix!!)}")
                            }
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Weight${ChatColor.GRAY}: ${ChatColor.WHITE}${currentRank.weight}")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Permissions${ChatColor.GRAY}: ${ChatColor.WHITE}${currentRank.permissions.size}")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage rank"))
                            toReturn.add(styleAction(ChatColor.RED, "RIGHT-CLICK", "to delete rank"))
                        }
                    }

                    override fun getMaterial(player: Player): Material {
                        return Material.WOOL
                    }

                    override fun getDamageValue(player: Player): Byte {
                        return Color.getWoolData(currentRank.color)
                    }

                    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                        if (clickType.isRightClick) {
                            if (currentRank.id == dev.ryu.core.shared.CoreAPI.rankManager.defaultRank.id) {
                                player.sendMessage("${ChatColor.RED}Rank '${currentRank.id}' is not deletable.")
                                playFail(player)
                                return
                            }

                            playNeutral(player)
                            ConfirmMenu("¿Sure?",
                                object : Callback<Boolean> {
                                    override fun callback(callback: Boolean) {
                                        if (callback) {
                                            player.sendMessage("${ChatColor.GREEN}Rank '${Color.color(currentRank.display)}${ChatColor.GREEN}' is now successfully deleted.")
                                            dev.ryu.core.shared.CoreAPI.rankManager.repository.delete(currentRank)
                                            dev.ryu.core.shared.CoreAPI.rankManager.cache.remove(currentRank.id)
                                            player.closeInventory()
                                            RankCommand().openMenu(player)
                                        } else {
                                            player.closeInventory()
                                            RankCommand().openMenu(player)
                                        }
                                    }
                                },
                            ).openMenu(player)

                        } else if (clickType.isLeftClick) {
                            player.closeInventory()
                            RankEditor(currentRank).openMenu(player)
                        }
                    }

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

            if (player.protocol <= 20) {
                toReturn[4] = MenuButton()
                    .icon(Material.EMERALD)
                    .name("${ChatColor.GREEN}Create new Rank")
                    .lore(arrayListOf<String>().also { toReturn ->
                        toReturn.add("")
                        toReturn.addAll(TextSplitter.split(text = "Create a new rank following the creation instructions"))
                        toReturn.add("")
                        toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to create new Rank!"))
                    })
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                            RankCreatePrompt(player)
                        ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
            } else {
                toReturn[4] = MenuButton()
                    .texturedIcon(Constants.GREEN_PLUS_TEXTURE)
                    .name("${ChatColor.GREEN}Create new Rank")
                    .lore(arrayListOf<String>().also { toReturn ->
                        toReturn.add("")
                        toReturn.addAll(TextSplitter.split(text = "Create a new rank following the creation instructions"))
                        toReturn.add("")
                        toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to create new Rank!"))
                    })
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                            RankCreatePrompt(player)
                        ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
            }

        }
    }

    override fun size(player: Player): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    private fun getAllowedRanks(): List<Rank> {
        val rankRepository = dev.ryu.core.shared.CoreAPI.rankManager.repository.findAllRanks()
        val ranks: MutableList<Rank> = Lists.newArrayList()
        ranks.addAll(rankRepository)
        ranks.sortWith { o1, o2 -> o2.weight - o1.weight }
        return ranks
    }

}