package dev.ryu.core.bukkit.prompt.rank

import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.bukkit.menu.rank.RankCommand
import dev.ryu.core.shared.system.Rank
import dev.t4yrn.jupiter.Jupiter
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

class RankCreatePrompt(val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a name for this rank to be created, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled creating rank.")

            RankCommand().openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        try {
            var rank = dev.ryu.core.shared.CoreAPI.rankManager.findById(input)

            if (rank == null) {
                rank = Rank(input)

                rank.display = "&7${rank.id}"
                rank.createdAt = System.currentTimeMillis()

                dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)
                dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().sendPacket(Jupiter(Rank.RANK_CREATED, rank))

                RankCommand().openMenu(player)
            } else {
                context.forWhom.sendRawMessage(Lang.CORE_PREFIX.value + Lang.RANK_ALREADY_EXISTS_ERROR.value.replace("{rank}", input))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            context.forWhom.sendRawMessage("${ChatColor.RED}There was an issue creating this rank.")
        }

        return Prompt.END_OF_CONVERSATION
    }

}