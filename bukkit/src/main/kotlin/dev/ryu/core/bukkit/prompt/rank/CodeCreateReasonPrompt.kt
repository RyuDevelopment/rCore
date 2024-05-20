package dev.ryu.core.bukkit.prompt.rank

import dev.ryu.core.bukkit.Core
import dev.ryu.core.shared.system.Rank
import org.bukkit.ChatColor
import org.bukkit.conversations.*
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 16:34
*/

class CodeCreateReasonPrompt(val creator: Player, val rank: Rank) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a reason for this code to be created, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled creating redeemable code.")
            return Prompt.END_OF_CONVERSATION
        }

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()) {
            context.forWhom.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                CodeCreateDurationPrompt(creator, this.rank, input)
            ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(context.forWhom))
        }

        return Prompt.END_OF_CONVERSATION
    }

}