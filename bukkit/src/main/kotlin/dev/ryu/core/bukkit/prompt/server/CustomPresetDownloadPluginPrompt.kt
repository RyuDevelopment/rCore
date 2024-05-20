package dev.ryu.core.bukkit.prompt.server

import dev.ryu.core.bukkit.util.CustomServerUtil
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 28/2/2024 - 15:51
*/

class CustomPresetDownloadPluginPrompt(val player: Player, val pluginName: String) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a url to download plugin jar, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled download external plugin.")

            dev.ryu.core.bukkit.menu.server.deploy.custom.plugin.PluginsMenu(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        val urlPattern = Regex("^https?://.*")
        if (urlPattern.matches(input)) {
            CustomServerUtil.downloadPlugin(input, pluginName)
            dev.ryu.core.bukkit.menu.server.deploy.custom.plugin.PluginsMenu(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        player.sendMessage("${ChatColor.RED}Only URLs are accepted for plugin input.")
        dev.ryu.core.bukkit.menu.server.deploy.custom.plugin.PluginsMenu(player).openMenu(player)
        return Prompt.END_OF_CONVERSATION
    }

}