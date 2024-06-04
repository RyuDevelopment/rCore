package dev.ryu.core.bukkit.menu.rank.editor

import dev.ryu.core.bukkit.menu.rank.RankEditor
import dev.ryu.core.bukkit.prompt.rank.RankModifyPrompt
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import dev.ryu.core.shared.system.Rank
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.button.Button.styleAction
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import java.util.*

class RankMetadata(private val rank: Rank) : Menu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}Metadata Editor"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 5)] = GlassButton(7)
            }

            for (i in 0 .. 4) {
                toReturn[getSlot(0, i)] = GlassButton(7)
                toReturn[getSlot(8, i)] = GlassButton(7)
            }

            toReturn[20] = MenuButton()
                .icon(Material.DIAMOND)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Discord ID")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (Objects.equals(this.rank.discordId, null)) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${this.rank.discordId!!}"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify discord id.")
                    )
                )
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        RankModifyPrompt(this.rank, "metadata", "discordId", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }

            toReturn[29] = MenuButton()
                .icon(
                    ItemStack(
                        Material.BANNER,
                        1,
                        15.toShort()
                    )
                )
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Website Color")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (Objects.equals(this.rank.websiteColor, null)) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}#${this.rank.websiteColor!!}"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify website color.")
                    )
                )
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        RankModifyPrompt(this.rank, "metadata", "websiteColor", player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }

            toReturn[22] = MenuButton()
                .icon(Material.BEACON)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Staff")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (this.rank.staff) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify staff status.")
                    )
                )
                .action(ClickType.LEFT) {
                    this.rank.staff = !this.rank.staff
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[23] = MenuButton()
                .icon(Material.QUARTZ)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Media")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (this.rank.media) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify media status.")
                    )
                )
                .action(ClickType.LEFT) {
                    this.rank.media = !this.rank.media
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[24] = MenuButton()
                .icon(Material.IRON_DOOR)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Hidden")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (this.rank.hidden) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify hidden status.")
                    )
                )
                .action(ClickType.LEFT) {
                    this.rank.hidden = !this.rank.hidden
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[31] = MenuButton()
                .icon(Material.EYE_OF_ENDER)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Default")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (this.rank.default) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify default status.")
                    )
                )
                .action(ClickType.LEFT) {
                    this.rank.default = !this.rank.default
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[32] = MenuButton()
                .icon(Material.LEVER)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Grantable")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (this.rank.grantable) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify grantable status.")
                    )
                )
                .action(ClickType.LEFT) {
                    this.rank.grantable = !this.rank.grantable
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[33] = MenuButton()
                .icon(Material.DIAMOND)
                .name("${ChatColor.YELLOW}${ChatColor.BOLD}Donator")
                .lore(
                    listOf(
                        "",
                        " ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${if (this.rank.donator) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}",
                        "",
                        styleAction(ChatColor.GREEN, "LEFT-CLICK", "to modify donator status.")
                    )
                )
                .action(ClickType.LEFT) {
                    this.rank.donator = !this.rank.donator
                    dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                }

            toReturn[4] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    RankEditor(this.rank).openMenu(player)
                }

        }

    }

    override fun size(player: Player): Int {
        return 54
    }

}