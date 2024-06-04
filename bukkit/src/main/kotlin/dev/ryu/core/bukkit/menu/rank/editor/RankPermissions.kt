package dev.ryu.core.bukkit.menu.rank.editor

import dev.ryu.core.bukkit.menu.rank.RankEditor
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import dev.ryu.core.shared.system.Rank
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class RankPermissions(private val rank: Rank) : PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Permissions Editor"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int,Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            this.rank.permissions.forEachIndexed { index, permission ->
                toReturn[index] = object : Button() {

                    override fun getName(player: Player): String {
                        return "${ChatColor.AQUA}$permission"
                    }

                    override fun getMaterial(player: Player): Material {
                        return Material.PAPER
                    }

                    override fun getDescription(player: Player): MutableList<String> {
                        return arrayListOf<String>().also { toReturn ->

                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "LEFT-CLICK", "to remove permission."))
                        }
                    }

                    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                        this@RankPermissions.rank.permissions.remove(permission)
                        dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
                    }
                }
            }

        }
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