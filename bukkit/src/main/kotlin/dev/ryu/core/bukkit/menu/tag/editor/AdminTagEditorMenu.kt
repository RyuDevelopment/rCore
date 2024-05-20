package dev.ryu.core.bukkit.menu.tag.editor

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.tag.AdminTagMenu
import dev.ryu.core.bukkit.menu.tag.editor.type.TagTypeEditorMenu
import dev.ryu.core.bukkit.prompt.tag.TagModifyPrompt
import dev.ryu.core.shared.system.Tag
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.BackButton
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.UnicodeUtil
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn © 2024
    * Project: tags
    * Date: 10/3/2024 - 15:08
*/

class AdminTagEditorMenu(
    private val tag: Tag
) : Menu() {

    override fun getTitle(player: Player): String {
        return ChatColor.translateAlternateColorCodes('&', "${tag.display}")
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            for (i in 0 until 9) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            for (i in 0 until 4) {
                toReturn[getSlot(0, i)] = GlassButton(7)
                toReturn[getSlot(8, i)] = GlassButton(7)
            }

            toReturn[4] = MenuButton()
                .icon(Material.GOLD_NUGGET)
                .name("${ChatColor.GOLD}Priority")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.AQUA}${tag.priority}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to add 1 priority.")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}SHIFT-LEFT ${ChatColor.GREEN}to add 10 priority.")
                    toReturn.add("${ChatColor.RED}${ChatColor.BOLD}RIGHT-CLICK ${ChatColor.RED}to subtract 1 priority.")
                    toReturn.add("${ChatColor.RED}${ChatColor.BOLD}SHIFT-RIGHT ${ChatColor.RED}to subtract 10 priority.")
                    toReturn.add("${ChatColor.YELLOW}${ChatColor.BOLD}MIDDLE-CLICK ${ChatColor.YELLOW}to reset priority.")
                    }
                }
                .action(ClickType.LEFT) {
                    tag.priority++
                    tag.save(true)
                }
                .action(ClickType.RIGHT) {
                    if (tag.priority == 0) {
                        player.sendMessage("${ChatColor.RED}The tag priority cannot go below 0.")
                        return@action
                    }
                    tag.priority--
                    tag.save(true)
                }
                .action(ClickType.MIDDLE) {
                    tag.priority = 1
                    tag.save(true)
                }
                .action(ClickType.SHIFT_LEFT) {
                    tag.priority += 10
                    tag.save(true)
                }
                .action(ClickType.SHIFT_RIGHT) {
                    val decrementAmount = 10
                    if (tag.priority < decrementAmount) {
                        player.sendMessage("${ChatColor.RED}The tag priority cannot go below 0.")
                        return@action
                    }
                    tag.priority -= decrementAmount
                    tag.save(true)
                }

            toReturn[20] = MenuButton()
                .icon(Material.NAME_TAG)
                .name("${ChatColor.GOLD}Display")
                .lore { arrayListOf<String?>().also { toReturn ->
                        toReturn.add("")
                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.RESET}${ChatColor.translateAlternateColorCodes('&', tag.display)}")
                        toReturn.add("")
                        toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to modify tag display.")
                    }
                }
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(
                        ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                            TagModifyPrompt(tag, "display", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }

            toReturn[22] = MenuButton()
                .icon(Material.DIAMOND)
                .name("${ChatColor.GOLD}Type")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.RESET}${StringUtils.capitalize(tag.type!!.toLowerCase())}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to modify tag type.")
                    }
                }
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    TagTypeEditorMenu(tag).openMenu(player)
                }

            toReturn[24] = MenuButton()
                .icon(Material.BOOK)
                .name("${ChatColor.GOLD}Permission")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.RESET}${tag.permission}")
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to modify tag usage permission.")
                    }
                }
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(
                        ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(TagModifyPrompt(tag, "permission", player)
                        ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }

            toReturn[36] = BackButton(AdminTagMenu())
        }
    }

    override fun size(player: Player?): Int {
        return 45
    }

}