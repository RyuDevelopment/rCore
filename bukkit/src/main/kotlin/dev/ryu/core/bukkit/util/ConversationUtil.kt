package dev.ryu.core.bukkit.util

import dev.ryu.core.bukkit.Core
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.Prompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 12:58
*/

object ConversationUtil {

    @JvmStatic
    fun startConversation(player: Player, prompt: Prompt) {
        if (player.openInventory != null) {
            player.closeInventory()
        }

        val factory = ConversationFactory(dev.ryu.core.bukkit.Core.get())
            .withModality(false)
            .withFirstPrompt(prompt)
            .withLocalEcho(false)
            .thatExcludesNonPlayersWithMessage("Go away evil console!")

        player.beginConversation(factory.buildConversation(player))
    }

}