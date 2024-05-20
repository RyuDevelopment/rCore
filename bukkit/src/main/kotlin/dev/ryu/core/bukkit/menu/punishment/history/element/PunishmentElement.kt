package dev.ryu.core.bukkit.menu.punishment.history.element

import dev.ryu.core.bukkit.menu.punishment.history.HistoryMenu
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.TextSplitter
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.InventoryView

class PunishmentElement(private val type: Punishment.Type, private val punishments: Set<Punishment>, private val profile: Profile) : Button() {

    override fun getName(player: Player): String {
        return "${this.type.color}${StringUtils.capitalize(this.type.name.toLowerCase().replace("_"," "))}s"
    }

    override fun getMaterial(player: Player): Material {
        return if (this.type == Punishment.Type.BLACKLIST) Material.BEDROCK else Material.WOOL
    }

    override fun getDescription(player: Player): MutableList<String> {
        return mutableListOf<String>().also { toReturn ->
            toReturn.add("")
            toReturn.addAll(TextSplitter.split(text = "Here all the ${type.name.toLowerCase()}s of the ${profile.name} will be displayed."))
            toReturn.add("")
            toReturn.add(styleAction(ChatColor.GREEN, "CLICK", "to view ${profile.name}'s ${type.name.toLowerCase()}s"))
        }
    }

    override fun getDamageValue(player: Player): Byte {
        return this.type.woolData.toByte()
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()

        if (!player.hasPermission("punishment.blacklist")) {
            return
        }

        HistoryMenu(this.type,this.punishments,this.profile).openMenu(player)
    }

}