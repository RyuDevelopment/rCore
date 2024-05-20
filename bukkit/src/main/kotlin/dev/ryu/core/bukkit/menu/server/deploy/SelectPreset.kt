package dev.ryu.core.bukkit.menu.server.deploy

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.server.ServerManager
import dev.ryu.core.bukkit.menu.server.deploy.custom.CustomPreset
import dev.ryu.core.bukkit.menu.server.deploy.preset.GeneralPresets
import dev.ryu.core.bukkit.menu.server.deploy.private.PrivatePresets
import dev.ryu.core.bukkit.util.protocol
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.TextSplitter
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.metadata.FixedMetadataValue

/*
    * Author: T4yrn
    * Project: core
    * Date: 27/2/2024 - 12:25
*/

class SelectPreset : Menu() {

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}Select a preset"
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

                    ServerManager().openMenu(player)
                }

            if (player.protocol <= 20) {
                toReturn[20] = MenuButton()
                    .icon(Material.GOLD_BLOCK)
                    .name("${ChatColor.AQUA}General Presets")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.addAll(TextSplitter.split(text = "${ChatColor.GRAY}Here, you can view presets for generating a new server based on existing configurations."))
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to browse deployable servers."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        GeneralPresets().openMenu(player)
                    }

                toReturn[22] = MenuButton()
                    .icon(Material.COMMAND)
                    .name("${ChatColor.GOLD}Custom Preset")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.addAll(TextSplitter.split(text = "${ChatColor.GRAY}You can customize your own server deployment preset here."))
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "deploy a server with your custom setup."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        CustomPreset(player).openMenu(player)
                    }

                toReturn[24] = MenuButton()
                    .icon(Material.IRON_FENCE)
                    .name("${ChatColor.LIGHT_PURPLE}Private Presets")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.addAll(TextSplitter.split(text = "${ChatColor.GRAY}Here, you can find presets for creating your own private server."))
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to access privately deployable servers."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        PrivatePresets().openMenu(player)
                    }
            } else {
                toReturn[20] = MenuButton()
                    .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjc1MDVjYjQ1YTBkZmM0ZWMwYjc0MWFkYmNlNmI1MDU2ZWQ1MWFiYTYzZmVhOWIyZDY2ZDc1OGNhYzBmMjQxMiJ9fX0=")
                    .name("${ChatColor.AQUA}General Presets")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.addAll(TextSplitter.split(text = "${ChatColor.GRAY}Here, you can view presets for generating a new server based on existing configurations."))
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to browse deployable servers."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        PrivatePresets().openMenu(player)
                    }

                toReturn[22] = MenuButton()
                    .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmUxOTY1YzdiYzhhMjYzMDE2N2MxZGEyZTUwZWIyM2Y2MjU2NGVhMTRiM2NlYmI1ZDdjZTQyOTk1ZGZhOWUwYiJ9fX0=")
                    .name("${ChatColor.GOLD}Custom Preset")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.addAll(TextSplitter.split(text = "${ChatColor.GRAY}You can customize your own server deployment preset here."))
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "deploy a server with your custom setup."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.setMetadata("creatingCustomServer", FixedMetadataValue(dev.ryu.core.bukkit.Core.get(), "creatingCustomServer"))
                        CustomPreset(player).openMenu(player)
                    }

                toReturn[24] = MenuButton()
                    .playerTexture(player.name)
                    .name("${ChatColor.LIGHT_PURPLE}Private Presets")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.addAll(TextSplitter.split(text = "${ChatColor.GRAY}Here, you can find presets for creating your own private server."))
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to access privately deployable servers."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        PrivatePresets().openMenu(player)
                    }
            }
        }
    }

    override fun size(player: Player): Int {
        return 45
    }

}