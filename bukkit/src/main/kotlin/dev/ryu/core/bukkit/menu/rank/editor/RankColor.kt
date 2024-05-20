package dev.ryu.core.bukkit.menu.rank.editor

import dev.ryu.core.bukkit.menu.rank.RankEditor
import dev.ryu.core.bukkit.menu.rank.editor.element.ColorButton
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import dev.ryu.core.shared.system.Rank
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class RankColor(private val rank: Rank) : PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Color Editor"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            var slot = 0
            for (color in ChatColor.entries) {
                if (!color.isColor) {
                    continue
                }
                val basicColor = ChatColor.getByChar(color.toString()[1])
                toReturn[slot] = ColorButton(rank, basicColor)
                slot++
            }

        }
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0..8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 3)] = GlassButton(7)
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

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun size(player: Player): Int {
        return 36
    }
}