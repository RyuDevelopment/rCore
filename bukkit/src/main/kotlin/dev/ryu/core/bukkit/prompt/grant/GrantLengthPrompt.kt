package dev.ryu.core.bukkit.prompt.grant

import dev.ryu.core.bukkit.Core
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.GrantModule
import com.starlight.nexus.util.time.TimeUtil
import com.starlight.nexus.menu.ConfirmMenu
import com.starlight.nexus.util.Callback
import com.starlight.nexus.util.Color
import org.apache.commons.lang.StringUtils
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

class GrantLengthPrompt(private val rank: Rank, private val reason: String, val profile: Profile, val sender: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a duration for this grant, (\"perm\" for permanent) or type ${ChatColor.RED}\"cancel\" ${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext,input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Granting cancelled.")
            return Prompt.END_OF_CONVERSATION
        }

        var duration = 0L
        val startsWithPerm = StringUtils.startsWithIgnoreCase(input,"perm")

        if (!startsWithPerm) {
            duration = TimeUtil.parseTime(input)
        }

        if (duration < 0L && !startsWithPerm) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Invalid duration.")
            return Prompt.END_OF_CONVERSATION
        }

        ConfirmMenu(
            "¿Sure?",
            object : Callback<Boolean> {
                override fun callback(callback: Boolean) {
                    if (callback) {
                        Bukkit.getServer().scheduler.runTaskAsynchronously(dev.ryu.core.bukkit.Core.get()) {
                            GrantModule.grant(rank, profile.id, sender.uniqueId, reason, duration)
                        }

                        sender.sendMessage("${ChatColor.GREEN}Granted ${ChatColor.WHITE}${ChatColor.valueOf(GrantModule.findBestRank(profile.id).color)}${profile.name} ${Color.color(rank.display)}${ChatColor.GREEN} rank${if (duration == 0L) "" else " for ${TimeUtil.formatIntoDetailedString(duration)}"}.")
                    } else {
                        sender.closeInventory()
                    }
                }
            },
        ).openMenu(sender)

        return Prompt.END_OF_CONVERSATION;
    }

}