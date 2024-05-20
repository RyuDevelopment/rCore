package dev.ryu.core.bukkit.prompt.tag

import dev.ryu.core.bukkit.menu.tag.editor.AdminTagEditorMenu
import dev.ryu.core.shared.system.Tag
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

class TagModifyPrompt(val tag: Tag, val toEdit: String, val player: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a value for modify this tag, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled modify tag.")
            return Prompt.END_OF_CONVERSATION
        }

        try {
            val field = Tag::class.java.getDeclaredField(toEdit)
            field.isAccessible = true

            field.set(tag, input)
            tag.save(true)

            AdminTagEditorMenu(tag).openMenu(player)
        } catch (e: NoSuchFieldException) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Error: Property $toEdit not found.")
        } catch (e: Exception) {
            context.forWhom.sendRawMessage("${ChatColor.RED}An error occurred while modifying the tag.")
            e.printStackTrace()
        }


        return Prompt.END_OF_CONVERSATION
    }

}