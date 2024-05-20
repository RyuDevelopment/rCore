package dev.ryu.core.bukkit.menu.punishment.staffhistory.element

import dev.ryu.core.bukkit.menu.punishment.staffhistory.StaffHistoryMenu
import dev.ryu.core.bukkit.menu.punishment.staffhistory.StaffPunishmentMenu.RemoveType
import com.starlight.nexus.menu.button.Button
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import org.apache.commons.lang3.StringUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.InventoryView

class StaffPunishmentButton(private val punishments: Set<Punishment>, private val profile: Profile) : Button() {

    var type: Punishment.Type? = null
    var removeType: RemoveType? = null
    var filteredPunishments: MutableSet<Punishment> = HashSet()

    constructor(type: Punishment.Type, punishments: Set<Punishment>, profile: Profile):this(punishments,profile) {
        this.type = type
        this.filteredPunishments = punishments.filter{it.type == type && it.sender == this.profile.id}.toMutableSet()
    }

    constructor(type: RemoveType, punishments: Set<Punishment>, profile: Profile):this(punishments,profile) {
        this.removeType = type
        this.filteredPunishments = punishments.filter{it.isPardoned() && type.parent.contains(it.type) && it.pardoner!! == this.profile.id}.toMutableSet()
    }

    override fun getName(player: Player): String {

        if (this.type != null) {
            return "${this.type!!.color}${StringUtils.capitalize(this.type!!.name.toLowerCase().replace("_"," "))}s"
        }

        return "${this.removeType!!.parent[0].color}${StringUtils.capitalize(this.removeType!!.name.toLowerCase().replace("_",""))}s"
    }

    override fun getDescription(player: Player): MutableList<String> {
        return arrayListOf()
    }

    override fun getAmount(player: Player): Int {
        return if (this.filteredPunishments.size >= 64) 64 else if (this.filteredPunishments.isEmpty()) 1 else this.filteredPunishments.size
    }

    override fun getMaterial(player: Player): Material {

        if (this.type != null) {
            return if (this.type == Punishment.Type.BLACKLIST) Material.BEDROCK else Material.WOOL
        }

        return this.removeType!!.material
    }

    override fun getDamageValue(player: Player): Byte {
        if (this.type != null) {
            return this.type!!.woolData.toByte()
        }

        return 0
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()

        if (this.type != null) {
            StaffHistoryMenu(this.type!!,this.punishments,this.filteredPunishments,this.profile).openMenu(player)
        } else {
            StaffHistoryMenu(this.removeType!!,this.punishments,this.filteredPunishments,this.profile).openMenu(player)
        }

    }

}