package dev.ryu.core.bukkit.menu.server.element

import dev.ryu.core.shared.system.Server
import dev.ryu.core.shared.system.extra.server.Status
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.TPSUtil
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.InventoryView

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:52
*/

class ServerButton(
    val server: Server
) : Button() {

    override fun getName(player: Player): String {
        return "${ChatColor.GOLD}${server.id}"
    }

    override fun getDescription(player: Player): List<String> {
        return arrayListOf<String>().also { toReturn ->

            toReturn.add("")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Status${ChatColor.GRAY}: ${ChatColor.WHITE}${Color.color("${server.status.color}${StringUtils.capitalize(this.server.status.name.toLowerCase().replace("_"," "))}")}")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Group${ChatColor.GRAY}: ${ChatColor.WHITE}${server.group}")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Players${ChatColor.GRAY}: ${ChatColor.WHITE}${server.onlinePlayers.size}/${server.maximumPlayers}")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}TPS${ChatColor.GRAY}: ${ChatColor.WHITE}${TPSUtil.formatTPS(server.ticksPerSecond[0], true)}")
            toReturn.add("")
            toReturn.add(styleAction(ChatColor.GREEN, "CLICK", "to view ${server.id}'s information."))
            toReturn.add("")

        }
    }

    override fun getMaterial(player: Player): Material {
        return Material.WOOL
    }

    override fun getDamageValue(player: Player): Byte {
        return when (server.status) {
            Status.ONLINE -> {
                5
            }
            Status.WHITELISTED -> {
                4
            }
            Status.OFFLINE -> {
                14
            }
        }
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.sendMessage("sexo")
    }

}