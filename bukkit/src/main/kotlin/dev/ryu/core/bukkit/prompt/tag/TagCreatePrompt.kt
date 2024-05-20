package dev.ryu.core.bukkit.prompt.tag

import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.shared.system.Tag
import dev.ryu.core.bukkit.menu.tag.AdminTagMenu
import dev.ryu.core.bukkit.menu.tag.editor.type.finish.TagFinishMenu
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: tags
    * Date: 10/3/2024 - 16:13
*/

class TagCreatePrompt(val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a name for this tag to be created, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled creating tag.")

            AdminTagMenu().openMenu(player)
            return Prompt.END_OF_CONVERSATION
        }

        try {
            val tag = dev.ryu.core.shared.CoreAPI.tagManager.findByName(input)

            if (tag == null) {
                val tagToCreate = Tag(input)

                TagFinishMenu(tagToCreate, input).openMenu(player)
            } else {
                context.forWhom.sendRawMessage(Lang.CORE_PREFIX.value + Lang.TAG_ALREADY_EXISTS_ERROR.value.replace("{tag}", input))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            context.forWhom.sendRawMessage("${ChatColor.RED}There was an issue creating this tag.")
        }

        return Prompt.END_OF_CONVERSATION
    }

}