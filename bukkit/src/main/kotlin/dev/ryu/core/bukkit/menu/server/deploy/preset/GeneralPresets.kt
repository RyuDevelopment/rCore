package dev.ryu.core.bukkit.menu.server.deploy.preset

import dev.ryu.core.bukkit.menu.server.deploy.SelectPreset
import dev.ryu.core.bukkit.util.protocol
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn
    * Project: core
    * Date: 27/2/2024 - 12:08
*/

class GeneralPresets : Menu() {

    init {
        isAutoUpdate = true
    }

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}General Presets"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            for (i in 0 .. 3) {
                toReturn[getSlot(0, i)] = GlassButton(7)
                toReturn[getSlot(8, i)] = GlassButton(7)
            }

            toReturn[4] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    SelectPreset().openMenu(player)
                }

            if (player.protocol <= 20) {
                toReturn[20] = MenuButton()
                    .icon(Material.REDSTONE)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[21] = MenuButton()
                    .icon(Material.REDSTONE)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[22] = MenuButton()
                    .icon(Material.REDSTONE)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[23] = MenuButton()
                    .icon(Material.REDSTONE)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[24] = MenuButton()
                    .icon(Material.REDSTONE)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
            } else {
                toReturn[20] = MenuButton()
                    .icon(Material.BARRIER)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[21] = MenuButton()
                    .icon(Material.BARRIER)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[22] = MenuButton()
                    .icon(Material.BARRIER)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[23] = MenuButton()
                    .icon(Material.BARRIER)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
                toReturn[24] = MenuButton()
                    .icon(Material.BARRIER)
                    .name("${ChatColor.RED}Soon...")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.RED, "Coming soon...", ""))
                        }
                    }
            }
        }
    }

    override fun size(player: Player): Int {
        return 45
    }

}