package dev.ryu.core.bukkit.menu.server.deploy.custom.plugin

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.server.deploy.custom.CustomPreset
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo.pluginsDefault
import dev.ryu.core.bukkit.prompt.server.CustomPresetPluginNameDownloadPrompt
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.TextSplitter
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.metadata.FixedMetadataValue

/*
    * Author: T4yrn
    * Project: core
    * Date: 28/2/2024 - 00:17
*/

class PluginsMenu(val player: Player) : PaginatedMenu() {

    init {
        isUpdateAfterClick = true
        isAutoUpdate = true
        player.setMetadata("pluginsSelected", FixedMetadataValue(dev.ryu.core.bukkit.Core.get(), "pluginsSelected"))
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}All Plugins"
    }

    override fun getAllPagesButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            CustomServerInfo.pluginsAvailables.entries.forEachIndexed { index, entry ->
                val pluginName = entry.key
                val isEnabled = entry.value
                val isDefaultPlugin = pluginsDefault.containsKey(pluginName)

                toReturn[index] = MenuButton()
                    .icon(Material.PAPER)
                    .name("${if (isEnabled) if (isDefaultPlugin) "${ChatColor.YELLOW}" else "${ChatColor.GREEN}" else "${ChatColor.RED}"}${StringUtils.capitalize(pluginName.replace("_", " "))} Plugin")
                    .lore { _ ->
                        arrayListOf<String>().also { lore ->
                            lore.add("")
                            if (isEnabled) {
                                if (isDefaultPlugin) {
                                    lore.add("${ChatColor.RED}Plugin is default, cannot be modified.")
                                } else {
                                    lore.add(styleAction(ChatColor.RED, "LEFT-CLICK", "to unselect plugin."))
                                }
                            } else {
                                if (isDefaultPlugin) {
                                    lore.add("${ChatColor.RED}Plugin is default, cannot be modified.")
                                } else {
                                    lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to select plugin."))
                                }
                            }
                        }
                    }
                    .action(ClickType.LEFT) {
                        if (!isDefaultPlugin) {
                            CustomServerInfo.pluginsAvailables[pluginName] = !isEnabled
                        }
                    }
            }
        }
    }

    override fun getGlobalButtons(player: Player): Map<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            toReturn[4] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()

                    CustomPreset(player).openMenu(player)
                }

            toReturn[40] = MenuButton()
                .icon(Material.FIREWORK)
                .name("${ChatColor.GREEN}Download External")
                .lore { _ ->
                    arrayListOf<String>().also { lore ->
                        lore.add("")
                        lore.addAll(TextSplitter.split(text = "${ChatColor.GRAY}To download an external plugin you only need a valid url that directly downloads the .jar of the plugin."))
                        lore.add("")
                        lore.add(styleAction(ChatColor.GREEN, "CLICK", "to download an external plugin from a URL."))
                    }
                }
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                        CustomPresetPluginNameDownloadPrompt(player)
                    ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                }
        }
    }

    override fun size(player: Player): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

}