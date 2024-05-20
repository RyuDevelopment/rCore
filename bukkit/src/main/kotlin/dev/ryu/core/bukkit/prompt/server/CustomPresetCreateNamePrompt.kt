package dev.ryu.core.bukkit.prompt.server

import dev.ryu.core.bukkit.menu.server.deploy.custom.CustomPreset
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import dev.ryu.core.shared.system.module.NetworkModule
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 27/2/2024 - 23:02
*/

class CustomPresetCreateNamePrompt(val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a name for this server to be created, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled set server name.")

            CustomPreset(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        if (NetworkModule.findServerById(input) != null) {
            player.sendMessage("${ChatColor.RED}The server already exists, please enter a different name.")
            CustomPreset(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        CustomServerInfo.serverName = input
        CustomPreset(player).openMenu(player)

        return Prompt.END_OF_CONVERSATION
    }

}