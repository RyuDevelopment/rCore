package dev.ryu.core.bukkit.menu.rank.editor.element

import dev.ryu.core.bukkit.menu.rank.RankEditor
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Rank
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.InventoryView

class ColorButton(private val rank: Rank, private val color: ChatColor) : Button() {

    override fun getName(player: Player): String {
        return color.toString() + Color.convert(color.name) + " Color"
    }

    override fun getDescription(player: Player): List<String> {
        return arrayListOf<String>().also { toReturn ->
            toReturn.add("")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Preview Color${ChatColor.GRAY}: $color${rank.id}")
            toReturn.add("")
            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select color."))
        }
    }

    override fun getMaterial(player: Player): Material {
        return Material.WOOL
    }

    override fun getDamageValue(player: Player): Byte {
        return Color.getWoolData(color.name)
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()

        rank.color = color.name
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        RankEditor(rank).openMenu(player)
    }
}