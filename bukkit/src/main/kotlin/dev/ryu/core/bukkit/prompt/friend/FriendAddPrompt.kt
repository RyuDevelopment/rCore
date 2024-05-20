package dev.ryu.core.bukkit.prompt.friend

import dev.ryu.core.bukkit.menu.friend.FriendsMenu
import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.shared.CoreAPI
import mkremins.fanciful.FancyMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.entity.Player

class FriendAddPrompt(val sender: Player) : StringPrompt() {

    override fun getPromptText(context: ConversationContext): String {
        return "${ChatColor.YELLOW}Please type a name to send friend request, or type ${ChatColor.RED}\"cancel\"${ChatColor.YELLOW} to cancel."
    }

    override fun acceptInput(context: ConversationContext, input: String): Prompt? {

        if (input.equals("cancel",true)) {
            context.forWhom.sendRawMessage("${ChatColor.RED}Cancelled adding friend.")

            FriendsMenu().openMenu(sender)
            return Prompt.END_OF_CONVERSATION
        }

        try {
            val target = CoreAPI.profileManager.findByName(input)!!

            if (input == sender.name) {
                sender.sendMessage("${ChatColor.RED}You can't send a friend request to yourself")
                return Prompt.END_OF_CONVERSATION
            }

            if (!target.friends.contains(sender.uniqueId)) {
                if (!target.requests.contains(sender.uniqueId)) {
                    target.requests.add(sender.uniqueId)
                    sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.GREEN}You have sent a friend request to ${ChatColor.AQUA}${target.name}${ChatColor.GREEN}.")

                    if (Bukkit.getPlayer(target.id) != null) {
                        val message = FancyMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.YELLOW}You have received a friend request from ${ChatColor.AQUA}${sender.name} ")
                        message.then("${ChatColor.GREEN}[Click to Accept]").toJSONString()
                        message.tooltip("${ChatColor.GREEN}Click to accept ${ChatColor.AQUA}${target.name} ${ChatColor.GREEN}friend request!")
                        message.command("/friend accept ${sender.name}")
                        message.send(Bukkit.getPlayer(target.id))
                    }

                    CoreAPI.profileManager.repository.update(target)
                } else {
                    sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name} ${ChatColor.RED}already has a pending friend request from you.")
                }
            } else {
                sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is already your friend.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            context.forWhom.sendRawMessage("${ChatColor.RED}There was an issue to adding a friend.")
        }

        return Prompt.END_OF_CONVERSATION
    }

}