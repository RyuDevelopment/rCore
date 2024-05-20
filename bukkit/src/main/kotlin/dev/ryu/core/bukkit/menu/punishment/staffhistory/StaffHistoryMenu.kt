package dev.ryu.core.bukkit.menu.punishment.staffhistory

import dev.ryu.core.bukkit.menu.punishment.staffhistory.StaffPunishmentMenu.RemoveType
import dev.ryu.core.bukkit.menu.punishment.staffhistory.element.StaffHistoryButton
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

class StaffHistoryMenu(private val punishments: Set<Punishment>, private val filteredPunishments: Set<Punishment>, private val profile: Profile) : PaginatedMenu() {

    var type: Punishment.Type? = null
    var removeType: RemoveType? = null

    constructor(
        type: Punishment.Type,
        punishments: Set<Punishment>,
        filteredPunishments: Set<Punishment>,
        profile: Profile
    ) : this(punishments, filteredPunishments, profile) {
        this.type = type
    }

    constructor(
        type: RemoveType,
        punishments: Set<Punishment>,
        filteredPunishments: Set<Punishment>,
        profile: Profile
    ) : this(punishments, filteredPunishments, profile) {
        this.removeType = type
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return if (this.type != null) "${ChatColor.GRAY}${this.type!!.color}${
            StringUtils.capitalize(
                this.type!!.name.toLowerCase().replace("_", " ")
            )
        }'s${ChatColor.GRAY}" else "${ChatColor.GRAY}${this.removeType!!.parent[0].color}${
            StringUtils.capitalize(
                this.removeType!!.name.toLowerCase().replace("_", "")
            )
        }'s${ChatColor.GRAY}"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        val toReturn = HashMap<Int, Button>()

        this.filteredPunishments.sortedByDescending { it.created }
            .forEach { toReturn[toReturn.size] = StaffHistoryButton(it, this.profile, this) }

        return toReturn
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
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

                    StaffPunishmentMenu(this.profile, this.punishments).openMenu(player)
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