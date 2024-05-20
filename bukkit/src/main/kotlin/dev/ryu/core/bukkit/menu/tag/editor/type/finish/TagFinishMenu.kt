package dev.ryu.core.bukkit.menu.tag.editor.type.finish

import dev.ryu.core.bukkit.menu.tag.AdminTagMenu
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Tag
import dev.ryu.core.shared.system.extra.tag.TagType
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn © 2024
    * Project: tags
    * Date: 10/3/2024 - 15:54
*/

class TagFinishMenu(
    private val tag: Tag,
    private val input: String
) : Menu() {

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}Select a type"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            for (i in 0 until 9) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            for (i in 0 until 4) {
                toReturn[getSlot(0, i)] = GlassButton(7)
                toReturn[getSlot(8, i)] = GlassButton(7)
            }

            toReturn[20] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.GOLD}Symbols")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to finalize with 'Symbols' Tag Type.")
                } }
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    tag.name = input
                    tag.type = TagType.SYMBOLS.name.toUpperCase()
                    tag.display = input
                    tag.permission = "tags.$input"
                    tag.priority = 0

                    tag.save(true)
                    dev.ryu.core.shared.CoreAPI.tagManager.tags[tag.name] = tag

                    AdminTagMenu().openMenu(player)

                    player.playSound(player.location, Sound.NOTE_PLING, 0.2F, 1.5F)
                }

            toReturn[22] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.YELLOW}Countries")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to finalize with 'Countries' Tag Type.")
                } }
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    tag.name = input
                    tag.type = TagType.COUNTRIES.name.toUpperCase()
                    tag.display = input
                    tag.permission = "tags.$input"
                    tag.priority = 0

                    tag.save(true)
                    dev.ryu.core.shared.CoreAPI.tagManager.tags[tag.name] = tag

                    AdminTagMenu().openMenu(player)

                    player.playSound(player.location, Sound.NOTE_PLING, 0.2F, 1.5F)
                }

            toReturn[24] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.LIGHT_PURPLE}Partners")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to finalize with 'Partners' Tag Type.")
                } }
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    tag.name = input
                    tag.type = TagType.PARTNERS.name.toUpperCase()
                    tag.display = input
                    tag.permission = "tags.$input"
                    tag.priority = 0

                    tag.save(true)
                    dev.ryu.core.shared.CoreAPI.tagManager.tags[tag.name] = tag

                    AdminTagMenu().openMenu(player)

                    player.playSound(player.location, Sound.NOTE_PLING, 0.2F, 1.5F)
                }
        }
    }

    override fun size(player: Player?): Int {
        return 45
    }

}