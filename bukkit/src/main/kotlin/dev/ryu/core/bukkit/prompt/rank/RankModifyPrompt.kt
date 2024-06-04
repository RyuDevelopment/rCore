package dev.ryu.core.bukkit.prompt.rank

import dev.ryu.core.bukkit.menu.rank.RankEditor
import dev.ryu.core.bukkit.menu.rank.editor.RankMetadata
import dev.ryu.core.shared.system.Rank
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 01:45
*/

class RankModifyPrompt(val rank: Rank, val menu: String, val toEdit: String, val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a value for modify this rank, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled modify rank.")
            if (this.menu == "metadata") {
                RankMetadata(rank).openMenu(player)
            } else {
                RankEditor(rank).openMenu(player)
            }
            return Prompt.END_OF_CONVERSATION
        }

        try {
            val field = Rank::class.java.getDeclaredField(toEdit)
            field.isAccessible = true

            if (field.name == "permissions") {
                val permissionsList = field.get(rank) as MutableList<String>
                permissionsList.add(input)

                if (permissionsList.contains(input)) {
                    RankEditor(rank).openMenu(player)
                    player.sendMessage("${ChatColor.RED}That rank already have this permission.")

                    return Prompt.END_OF_CONVERSATION
                }

                field.set(rank, permissionsList)
                dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
            } else if (field.name == "price") {
                val priceValue = input.toIntOrNull() ?: -1
                field.set(rank, priceValue)
                dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
            } else if (field.name == "prefix") {
                rank.prefix = input
                dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
            } else {
                field.set(rank, input)
                dev.ryu.core.shared.Shared.rankManager.repository.update(rank)
            }

            if (this.menu == "metadata") {
                RankMetadata(rank).openMenu(player)
            } else {
                RankEditor(rank).openMenu(player)
            }

        } catch (e: NoSuchFieldException) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Error: Property $toEdit not found.")
        } catch (e: Exception) {
            context.forWhom.sendRawMessage("${ChatColor.RED}An error occurred while modifying the rank.")
            e.printStackTrace()
        }


        return Prompt.END_OF_CONVERSATION
    }

}