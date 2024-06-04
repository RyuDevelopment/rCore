package dev.ryu.core.bukkit.menu.rank

import dev.ryu.core.bukkit.menu.rank.editor.*
import dev.ryu.core.bukkit.prompt.rank.RankModifyPrompt
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.CodeModule
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.Color
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
    * Date: 21/2/2024 - 14:54
*/

class RankEditor(
    val rank: Rank
) : Menu() {

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
    }

    override fun getTitle(player: Player): String {
        return "${ChatColor.valueOf(this.rank.color)}${this.rank.id}${ChatColor.GRAY}'s Settings"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            for (i in 0 .. 3) {
                toReturn[getSlot(0, i)] = GlassButton(7)
                toReturn[getSlot(8, i)] = GlassButton(7)
            }

            toReturn[2] = MenuButton()
                .icon(Material.GOLD_NUGGET)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Weight")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.AQUA}${this.rank.weight}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add 1 weight."),
                    styleAction(ChatColor.GREEN, "SHIFT-LEFT", "to add 10 weight."),
                    styleAction(ChatColor.RED, "RIGHT-CLICK", "to subtract 1 weight."),
                    styleAction(ChatColor.RED, "SHIFT-RIGHT", "to subtract 10 weight."),
                    styleAction(ChatColor.YELLOW, "MIDDLE-CLICK", "to reset weight.")
                ))
                .action(ClickType.LEFT) {
                    this.rank.weight++
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }
                .action(ClickType.RIGHT) {
                    if (this.rank.weight == 0) {
                        player.sendMessage("${ChatColor.RED}The rank weight cannot go below 0.")
                        return@action
                    }
                    this.rank.weight--
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }
                .action(ClickType.MIDDLE) {
                    this.rank.weight = 1
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }
                .action(ClickType.SHIFT_LEFT) {
                    this.rank.weight += 10
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }
                .action(ClickType.SHIFT_RIGHT) {
                    val decrementAmount = 10
                    if (this.rank.weight < decrementAmount) {
                        player.sendMessage("${ChatColor.RED}The rank weight cannot go below 0.")
                        return@action
                    }
                    this.rank.weight -= decrementAmount
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[4] = MenuButton()
                .icon(Material.SIGN)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Name")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${this.rank.id}",
                    ""
                ))

            toReturn[6] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Price")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.GREEN}${UnicodeUtil.COINS}${this.rank.price}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify price.")
                ))
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        RankModifyPrompt(this.rank, "rank_editor", "price", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }
            toReturn[20] = MenuButton()
                .icon(Material.ITEM_FRAME)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Display")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${Color.color(this.rank.display)}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify display.")
                ))
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        RankModifyPrompt(this.rank, "rank_editor", "display", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }

            toReturn[21] = MenuButton()
                .icon(Material.NAME_TAG)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Prefix")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (Objects.equals(this.rank.prefix, null)) "${ChatColor.RED}Not Set" else Color.color(this.rank.prefix!!)}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify prefix.")
                ))
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        RankModifyPrompt(this.rank, "rank_editor", "prefix", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }

            toReturn[22] = object : Button() {

                override fun getName(player: Player): String {
                    return "${ChatColor.YELLOW}${ChatColor.BOLD}Color"
                }

                override fun getDescription(player: Player): List<String> {
                    return arrayListOf<String>().also { toReturn ->
                        toReturn.add("")
                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.valueOf(this@RankEditor.rank.color)}${Color.convert(this@RankEditor.rank.color)}")
                        toReturn.add("")
                        toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage color."))
                    }
                }

                override fun getMaterial(player: Player): Material {
                    return Material.WOOL
                }

                override fun getDamageValue(player: Player): Byte {
                    return Color.getWoolData(this@RankEditor.rank.color)
                }

                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                    if (clickType == ClickType.LEFT) {
                        player.closeInventory()
                        RankColor(this@RankEditor.rank).openMenu(player)
                    }
                }

            }

            toReturn[23] = MenuButton()
                .icon(Material.IRON_FENCE)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Permissions")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${this.rank.permissions.size}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add permission."),
                    styleAction(ChatColor.RED, "RIGHT-CLICK", "to manage permission.")
                ))
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        RankModifyPrompt(this.rank, "rank_editor", "permissions", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }
                .action(ClickType.RIGHT) {
                    player.closeInventory()
                    RankPermissions(this.rank).openMenu(player)
                }

            toReturn[24] = MenuButton()
                .icon(Material.RAILS)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Inherits")
                .lore(listOf(
                    "",
                    " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${this.rank.inherits.size}",
                    "",
                    styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage inherits.")
                ))
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    RankInherits(this.rank).openMenu(player)
                }

            toReturn[36] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    RankCommand().openMenu(player)
                }

            if (this.rank.donator) {

                toReturn[39] = MenuButton()
                    .icon(Material.HOPPER)
                    .name("${ChatColor.YELLOW}${ChatColor.BOLD}Metadata")
                    .lore(listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Discord ID${ChatColor.GRAY}: ${if (Objects.equals(this.rank.discordId, null)) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${this.rank.discordId}"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Website Color${ChatColor.GRAY}: ${if (Objects.equals(this.rank.websiteColor, null)) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${this.rank.websiteColor}"}",
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Staff${ChatColor.GRAY}: ${if (this.rank.isStaff()) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Media${ChatColor.GRAY}: ${if (this.rank.isMedia()) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Hidden${ChatColor.GRAY}: ${if (this.rank.isHidden()) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Default${ChatColor.GRAY}: ${if (this.rank.isDefault()) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Grantable${ChatColor.GRAY}: ${if (this.rank.isGrantable()) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Donator${ChatColor.GRAY}: ${if (this.rank.isDonator()) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage metadata.")
                    ))
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        RankMetadata(this.rank).openMenu(player)
                    }

                val codesOfRank = CodeModule.repository.findAllByRank(rank).size

                toReturn[41] = MenuButton()
                    .icon(Material.PAPER)
                    .name("${ChatColor.YELLOW}${ChatColor.BOLD}Redeemable Codes")
                    .lore(listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}$codesOfRank",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage redeemable codes.")
                    ))
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        RankCodes(this.rank).openMenu(player)
                    }

            } else {

                toReturn[40] = MenuButton()
                    .icon(Material.HOPPER)
                    .name("${ChatColor.YELLOW}${ChatColor.BOLD}Metadata")
                    .lore(listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Discord ID${ChatColor.GRAY}: ${if (Objects.equals(this.rank.discordId, null)) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${this.rank.discordId}"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Website Color${ChatColor.GRAY}: ${if (Objects.equals(this.rank.websiteColor, null)) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${this.rank.websiteColor}"}",
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Staff${ChatColor.GRAY}: ${if (this.rank.staff) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Media${ChatColor.GRAY}: ${if (this.rank.media) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Hidden${ChatColor.GRAY}: ${if (this.rank.hidden) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Default${ChatColor.GRAY}: ${if (this.rank.default) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Grantable${ChatColor.GRAY}: ${if (this.rank.grantable) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Donator${ChatColor.GRAY}: ${if (this.rank.donator) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to manage metadata.")
                    ))
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        RankMetadata(this.rank).openMenu(player)
                    }

            }

        }
    }

    override fun size(player: Player): Int {
        return 45
    }

}