package dev.ryu.core.bukkit.prompt.rank

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.menu.rank.editor.RankCodes
import dev.ryu.core.bukkit.system.webhook.impl.CodesLogWebhook
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.CodeModule
import com.starlight.nexus.util.time.TimeUtil
import org.apache.commons.lang.StringUtils
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player
import java.text.SimpleDateFormat
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 16:34
*/

class CodeCreateDurationPrompt(val creator: Player, val rank: Rank, val reason: String) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a duration for this code to be created, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled creating redeemable code.")
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
        } else if (duration == 0L && !startsWithPerm) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Invalid duration. Please enter a valid duration in the format '1h30m' or '30s'.")
            return Prompt.END_OF_CONVERSATION
        }

        val maxLength = 14
        val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        var randomCode: String

        do {
            randomCode = (1..maxLength).map { characters.random() }.joinToString("")
        } while (CodeModule.repository.findAllByRank(this.rank).any { it.code == randomCode })

        val createdAt = System.currentTimeMillis()

        CodeModule.code(UUID.randomUUID(), randomCode, this.rank.id, duration, this.creator.uniqueId, createdAt)
        RankCodes(rank).openMenu(creator)

        if (dev.ryu.core.bukkit.Core.get().webhookEnabled) {
            val data = JsonObject()
            data.addProperty("rank", rank.id)
            data.addProperty("duration", if (duration == 0L) "Permanent" else TimeUtil.formatIntoDetailedString(duration))
            data.addProperty("generatedBy", creator.name)

            val date = Date(createdAt)
            val format = SimpleDateFormat("EEEE MMMM d h:mm a yyyy", Locale.getDefault())
            val finalDate = format.format(date)

            data.addProperty("code", randomCode)
            data.addProperty("generatedAt", finalDate)
            data.addProperty("senderUUID", creator.uniqueId.toString())
            data.addProperty("reason", reason)

            CodesLogWebhook.onCodeCreated(data)
        }

        return Prompt.END_OF_CONVERSATION
    }

}