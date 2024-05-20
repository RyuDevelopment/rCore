package dev.ryu.core.bukkit.menu.sound

import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 4/3/2024 - 20:47
*/

class SoundTestMenu : PaginatedMenu() {

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Test a sound!"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            var index = 0
            Sound.entries.forEach { sound ->
                toReturn[index] = MenuButton()
                    .icon(Material.RECORD_4)
                    .name(StringUtils.capitalize(sound.name.toLowerCase()))
                    .lore { arrayListOf<String>().also { desc ->
                        desc.add("")
                        desc.add(styleAction(ChatColor.GREEN, "CLICK", "to play sound!"))
                    } }
                    .action(ClickType.LEFT) {
                        player.playSound(player.location, sound, 0.5f, 0.5f)
                    }
                index++
            }
        }
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }
        }
    }
    
    override fun size(player: Player): Int {
        return 5*9
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 3*9
    }

}