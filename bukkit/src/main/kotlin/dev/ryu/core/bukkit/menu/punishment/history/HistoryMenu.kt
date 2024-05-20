package dev.ryu.core.bukkit.menu.punishment.history

import dev.ryu.core.bukkit.menu.punishment.history.element.HistoryElement
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.menu.PaginatedMenu
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class HistoryMenu(private val type: Punishment.Type, private val punishments: Set<Punishment>, private val profile: Profile) : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player): String {
        return "${this.type.color}${StringUtils.capitalize(this.type.name.toLowerCase().replace("_"," "))}'s"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        val toReturn = HashMap<Int,Button>()

        this.punishments.filter{if (this.type == Punishment.Type.MUTE) (it.type == Punishment.Type.MUTE) else it.type == this.type}.sortedBy{it.created}.reversed().forEach{toReturn[toReturn.size] = HistoryElement(it,this.profile,this) }

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

                    PunishmentMenu(this.profile, this.punishments).openMenu(player)
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