package dev.ryu.core.bukkit.menu.rank.editor

import dev.ryu.core.bukkit.menu.rank.RankEditor
import dev.ryu.core.shared.system.Rank
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class RankInherits(private val rank: Rank) : PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Inherits Editor"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        val toReturn = HashMap<Int,Button>()

        dev.ryu.core.shared.Shared.rankManager.repository.findAllRanks().filter{it.id != this.rank.id}.sortedBy{it.weight}.forEach{toReturn[toReturn.size] = object : Button() {

            override fun getName(player: Player): String {
                return Color.color(it.display)
            }

            override fun getMaterial(player: Player): Material {
                return Material.WOOL
            }

            override fun getDamageValue(player: Player): Byte {
                if (it.color.isEmpty()) {
                    return 0
                }

                return Color.getWoolData(it.color)
            }

            override fun getDescription(player: Player): MutableList<String> {
                return arrayListOf<String>().also { toReturn ->
                    if (rank.inherits.contains(it.id)) {
                        toReturn.add("")
                        toReturn.add(styleAction(ChatColor.RED, "LEFT-CLICK", "to remove inherit."))
                    } else {
                        toReturn.add("")
                        toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add inherit."))
                    }
                }
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {

                if (!rank.inherits.contains(it.id)) {
                    rank.inherits.add(it.id)
                    playNeutral(player)
                } else {
                    playNeutral(player)
                    rank.inherits.remove(it.id)
                }

                dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
            }

        }}

        return toReturn
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int,Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0..8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
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
        return 45
    }

}