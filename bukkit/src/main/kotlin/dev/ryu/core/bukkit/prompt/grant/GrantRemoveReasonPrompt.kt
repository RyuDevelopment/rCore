package dev.ryu.core.bukkit.prompt.grant

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.grant.view.GrantsMenu
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.GrantModule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 21:44
*/

class GrantRemoveReasonPrompt(private val grant: Grant, private val sender: Player, private val target: Profile) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a reason for this grant to be removed, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled removing grant.")
            return Prompt.END_OF_CONVERSATION
        }

        Bukkit.getServer().scheduler.runTaskAsynchronously(dev.ryu.core.bukkit.Core.get()) {

            if (GrantModule.remove(this.grant,(context.forWhom as Player).uniqueId,input)) {
                context.forWhom.sendRawMessage("${ChatColor.GREEN}Removed grant successfully.")

                val allGrants: Set<Grant> = GrantModule.repository.findAllByPlayer(this.target.id)
                GrantsMenu(allGrants, target).openMenu(sender)

                return@runTaskAsynchronously
            }

            context.forWhom.sendRawMessage("${ChatColor.RED}There was an issue removing this grant.")
        }

        return Prompt.END_OF_CONVERSATION
    }

}