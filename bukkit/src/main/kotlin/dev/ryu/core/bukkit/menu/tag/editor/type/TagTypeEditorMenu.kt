package dev.ryu.core.bukkit.menu.tag.editor.type

import dev.ryu.core.bukkit.menu.tag.editor.AdminTagEditorMenu
import dev.ryu.core.shared.system.Tag
import dev.ryu.core.shared.system.extra.tag.TagType
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.BackButton
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn © 2024
    * Project: tags
    * Date: 10/3/2024 - 15:54
*/

class TagTypeEditorMenu(
    private val tag: Tag
) : Menu() {

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}Set a type"
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
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to set 'Symbols' type.")
                } }
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    tag.type = TagType.SYMBOLS.name.toUpperCase()
                    tag.save(true)

                    AdminTagEditorMenu(tag).openMenu(player)
                }

            toReturn[22] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.YELLOW}Countries")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to set 'Countries' type.")
                } }
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    tag.type = TagType.COUNTRIES.name.toUpperCase()
                    tag.save(true)

                    AdminTagEditorMenu(tag).openMenu(player)
                }

            toReturn[24] = MenuButton()
                .icon(Material.EMERALD)
                .name("${ChatColor.LIGHT_PURPLE}Partners")
                .lore { arrayListOf<String?>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to set 'Partners' type.")
                } }
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    tag.type = TagType.PARTNERS.name.toUpperCase()
                    tag.save(true)

                    AdminTagEditorMenu(tag).openMenu(player)
                }

            toReturn[36] = BackButton(AdminTagEditorMenu(tag))
        }
    }

    override fun size(player: Player?): Int {
        return 45
    }

}