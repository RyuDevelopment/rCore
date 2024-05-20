package dev.ryu.core.bukkit.prompt.server

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import dev.ryu.core.bukkit.menu.server.deploy.custom.plugin.PluginsMenu
import org.bukkit.ChatColor
import org.bukkit.conversations.*
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 28/2/2024 - 15:51
*/

class CustomPresetPluginNameDownloadPrompt(val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a name for this plugin download, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled download external plugin.")

            PluginsMenu(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        if (CustomServerInfo.pluginsAvailables.contains(input)) {
            player.sendMessage("${ChatColor.RED}It seems that the plugin is already in our cache. Please enter a different plugin.")
            PluginsMenu(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()) {
            context.forWhom.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                CustomPresetDownloadPluginPrompt(player, input)
            ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(context.forWhom))
        }

        return Prompt.END_OF_CONVERSATION
    }

}