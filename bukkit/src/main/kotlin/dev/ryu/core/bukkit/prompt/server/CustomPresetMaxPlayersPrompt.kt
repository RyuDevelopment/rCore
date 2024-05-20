package dev.ryu.core.bukkit.prompt.server

import dev.ryu.core.bukkit.menu.server.deploy.custom.CustomPreset
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
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

class CustomPresetMaxPlayersPrompt(val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a name for this server to be created, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled creating rank.")

            CustomPreset(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        val maxPlayers: Int = try {
            input.toInt()
        } catch (e: NumberFormatException) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Please enter a valid max players value.")

            CustomPreset(player).openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        CustomServerInfo.maxPlayers = maxPlayers
        CustomPreset(player).openMenu(player)

        return Prompt.END_OF_CONVERSATION
    }

}