package dev.ryu.core.bukkit.prompt.punishment

import dev.ryu.core.bukkit.Core
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.PunishmentModule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class PunishmentRemoveReasonPrompt(private val punishment: Punishment, private val profile: Profile) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a reason for this punishment to be pardoned, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled pardoning punishment.")
            return Prompt.END_OF_CONVERSATION
        }

        val senderName = if (context.forWhom is Player) (context.forWhom as Player).name else "${ChatColor.DARK_RED}${ChatColor.BOLD}Console"

        Bukkit.getServer().scheduler.runTaskAsynchronously(dev.ryu.core.bukkit.Core.get()) {
            PunishmentModule.pardon(this.punishment,(context.forWhom as Player).uniqueId,input,true,profile.name!!,senderName)
        }

        return Prompt.END_OF_CONVERSATION
    }

}