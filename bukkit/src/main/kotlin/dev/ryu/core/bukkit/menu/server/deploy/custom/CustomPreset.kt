package dev.ryu.core.bukkit.menu.server.deploy.custom

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.server.deploy.SelectPreset
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import dev.ryu.core.bukkit.menu.server.deploy.custom.plugin.PluginsMenu
import dev.ryu.core.bukkit.menu.server.deploy.custom.spigot.SpigotsMenu
import dev.ryu.core.bukkit.prompt.server.CustomPresetCreateNamePrompt
import dev.ryu.core.bukkit.prompt.server.CustomPresetMaxPlayersPrompt
import dev.ryu.core.bukkit.prompt.server.CustomPresetPortPrompt
import dev.ryu.core.bukkit.util.Constants
import dev.ryu.core.bukkit.util.CustomServerUtil.createCustomServer
import dev.ryu.core.bukkit.util.JarDetection
import dev.ryu.core.bukkit.util.protocol
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.TextSplitter
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn
    * Project: core
    * Date: 27/2/2024 - 12:08
*/

class CustomPreset(val player: Player) : Menu() {

    init {
        isAutoUpdate = true

        CustomServerInfo.spigotsAvailables.clear()

        if (!player.hasMetadata("pluginsSelected")) {
            CustomServerInfo.pluginsAvailables.clear()
            CustomServerInfo.pluginsAvailables.putAll(CustomServerInfo.pluginsDefault)
        }

        CustomServerInfo.spigotsAvailables.addAll(JarDetection.detectSpigotsJar("/home/deploys/settings/spigots"))
        val newPlugins = JarDetection.detectPluginsJars("/home/deploys/settings/plugins")

        newPlugins.forEach { (plugin, isEnabled) ->
            if (!CustomServerInfo.pluginsAvailables.containsKey(plugin)) {
                CustomServerInfo.pluginsAvailables[plugin] = isEnabled
            }
        }
    }

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}Custom Preset"
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
                    .icon(Material.PAPER)
                    .name("${ChatColor.GOLD}Server Name")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.serverName == null) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${CustomServerInfo.serverName}" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to set a server name."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt( CustomPresetCreateNamePrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
                toReturn[21] = MenuButton()
                    .icon(Material.COMMAND)
                    .name("${ChatColor.GOLD}Spigot")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.spigot == null) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${CustomServerInfo.spigot} Spigot" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select a spigot."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        SpigotsMenu().openMenu(player)
                    }
                toReturn[22] = MenuButton()
                    .icon(Material.BEACON)
                    .name("${ChatColor.GOLD}Port")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.port == null) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${CustomServerInfo.port}" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to set a port."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt( CustomPresetPortPrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
                toReturn[23] = MenuButton()
                    .icon(Material.EXP_BOTTLE)
                    .name("${ChatColor.GOLD}Max Players")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.maxPlayers == 250) "${ChatColor.WHITE}250" else "${ChatColor.WHITE}${CustomServerInfo.maxPlayers}" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to set a max players."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt( CustomPresetMaxPlayersPrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }

                val enabledPluginsSize = CustomServerInfo.pluginsAvailables.values.count { it }
                toReturn[24] = MenuButton()
                    .icon(Material.REDSTONE_LAMP_OFF)
                    .name("${ChatColor.GOLD}Plugins")
                    .lore { _ ->
                        arrayListOf<String>().also { lore ->
                            lore.add("")
                            lore.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Total Plugins${ChatColor.GRAY}: ${ChatColor.WHITE}$enabledPluginsSize")
                            lore.add("")
                            lore.addAll(TextSplitter.split(text = CustomServerInfo.pluginsAvailables.filterValues { it }.keys.joinToString(", ")))
                            lore.add("")
                            lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select and see plugins."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        PluginsMenu(player).openMenu(player)
                    }
            } else {
                toReturn[20] = MenuButton()
                    .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWFlYTNjYWM2MDk5YjY5ZDJjZjdjNWE2MWU1YTk1NDI5MTdmNDU4MzFmN2Y2OWRkZmFmMzJlNjc2ZGJiODhkYyJ9fX0=")
                    .name("${ChatColor.GOLD}Server Name")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.serverName == null) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${CustomServerInfo.serverName}" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to set a server name."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt( CustomPresetCreateNamePrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
                toReturn[21] = MenuButton()
                    .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDJmMjc1MmNkZjhiYjY2NWEzZWNhYjA3YzEyNjFhOGU1OTRkYTc5MTdiY2FmNTYyNjc0N2EwOTcyMTk2MDk5MyJ9fX0=")
                    .name("${ChatColor.GOLD}Spigot")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.spigot == null) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${CustomServerInfo.spigot} Spigot" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select a spigot."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        SpigotsMenu().openMenu(player)
                    }
                toReturn[22] = MenuButton()
                    .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTJjY2I4NGY5YzBjYmIyZWI3YjlhYmJiN2E0ZmM3YjMxODYxNTBkNWU3ZGIwMTg1MmFjNmQ0OTVkZTE3ZTgxMiJ9fX0=")
                    .name("${ChatColor.GOLD}Port")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.port == null) "${ChatColor.RED}Not Set" else "${ChatColor.WHITE}${CustomServerInfo.port}" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to set a port."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt( CustomPresetPortPrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
                toReturn[23] = MenuButton()
                    .playerTexture(player.name)
                    .name("${ChatColor.GOLD}Max Players")
                    .lore { _ ->
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (CustomServerInfo.maxPlayers == 250) "${ChatColor.WHITE}250" else "${ChatColor.WHITE}${CustomServerInfo.maxPlayers}" }")
                            toReturn.add("")
                            toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to set a max players."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt( CustomPresetMaxPlayersPrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }

                val enabledPluginsSize = CustomServerInfo.pluginsAvailables.values.count { it }
                toReturn[24] = MenuButton()
                    .texturedIcon("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTgzODNlZGRjZmFiOTExMDJhYjRhZTEwYjM0YjZlMjI3YzY4NTljOTRkMjgwNDk3N2VhMDFiMTZiOTk2MGNkMyJ9fX0=")
                    .name("${ChatColor.GOLD}Plugins")
                    .lore { _ ->
                        arrayListOf<String>().also { lore ->
                            lore.add("")
                            lore.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Total Plugins${ChatColor.GRAY}: ${ChatColor.WHITE}$enabledPluginsSize")
                            lore.add("")
                            lore.addAll(TextSplitter.split(text = CustomServerInfo.pluginsAvailables.filterValues { it }.keys.joinToString(", ")))
                            lore.add("")
                            lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select and see plugins."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        PluginsMenu(player).openMenu(player)
                    }
            }
            if (player.hasMetadata("pluginsSelected")) {
                if (player.protocol <= 20) {
                    toReturn[40] = MenuButton()
                        .icon(Material.EMERALD)
                        .name("${ChatColor.GREEN}Deploy!")
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to deploy custom server."))
                            }
                        }
                        .action(ClickType.LEFT) {
                            player.closeInventory()
                            player.sendMessage("${ChatColor.GREEN}The server is setting up. Please wait!")
                            player.removeMetadata("pluginsSelected", Core.get())

                            createCustomServer(player)

                            CustomServerInfo.serverName = null
                            CustomServerInfo.port = null
                            CustomServerInfo.spigot = null
                            CustomServerInfo.maxPlayers = 250
                            CustomServerInfo.pluginsAvailables.clear()
                            CustomServerInfo.spigotsAvailables.clear()

                            CustomServerInfo.pluginsAvailables.putAll(
                                hashMapOf(
                                    "Core" to true,
                                    "WorldEdit" to true,
                                    "ViaVersion" to true,
                                    "ProtocolLib" to true,
                                    "Spark" to true
                                )
                            )
                        }
                } else {
                    toReturn[40] = MenuButton()
                        .texturedIcon(Constants.GREEN_PLUS_TEXTURE)
                        .name("${ChatColor.GREEN}Deploy!")
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to deploy custom server."))
                            }
                        }
                        .action(ClickType.LEFT) {
                            player.closeInventory()
                            player.sendMessage("${ChatColor.GREEN}The server is setting up. Please wait!")
                            player.removeMetadata("pluginsSelected", Core.get())
                            createCustomServer(player)

                            CustomServerInfo.serverName = null
                            CustomServerInfo.port = null
                            CustomServerInfo.spigot = null
                            CustomServerInfo.maxPlayers = 250
                            CustomServerInfo.pluginsAvailables.clear()
                            CustomServerInfo.spigotsAvailables.clear()

                            CustomServerInfo.pluginsAvailables.putAll(
                                hashMapOf(
                                    "Core" to true,
                                    "WorldEdit" to true,
                                    "ViaVersion" to true,
                                    "ProtocolLib" to true,
                                    "Spark" to true
                                )
                            )
                        }
                }
            } else {
                if (player.protocol <= 20) {
                    toReturn[40] = MenuButton()
                        .icon(Material.REDSTONE)
                        .name("${ChatColor.RED}Deploy!")
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.addAll(TextSplitter.split(text = "You can't deploy this setup because some settings are incomplete, please check name, spigot, port or plugins"))
                                lore.add("")
                            }
                        }
                } else {
                    toReturn[40] = MenuButton()
                        .texturedIcon(Constants.RED_PLUS_TEXTURE)
                        .name("${ChatColor.RED}Deploy!")
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.addAll(TextSplitter.split(text = "You can't deploy this setup because some settings are incomplete, please check name, spigot, port or plugins"))
                                lore.add("")
                            }
                        }
                }
            }
        }
    }

    override fun size(player: Player): Int {
        return 45
    }

}