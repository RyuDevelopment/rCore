package dev.ryu.core.bukkit.command.friend

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import dev.ryu.core.bukkit.menu.friend.FriendsMenu
import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Profile
import mkremins.fanciful.FancyMessage
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player

object FriendCommand {

    @Command(names = ["friends"], description = "Open your friends menu.", permission = "")
    @JvmStatic
    fun friends(
        sender: Player
    ) {
        FriendsMenu().openMenu(sender)

        sender.sendMessage("")
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To get the complete list of commands use '" + ChatColor.GOLD + "/friend" + ChatColor.GRAY + "'.")
        sender.sendMessage("")

        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

    @Command(names = ["friend add"], description = "Add a friend to your list.", permission = "")
    @JvmStatic
    fun add(
        sender: Player,
        @Param(name = "player") target: Profile
    ) {
        if (target.name == sender.name) {
            sender.sendMessage("${ChatColor.RED}You can't send a friend request to yourself")
            return
        }

        if (!target.friends.contains(sender.uniqueId)) {
            if (!target.requests.contains(sender.uniqueId)) {
                target.requests.add(sender.uniqueId)
                sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.GREEN}You have sent a friend request to ${ChatColor.AQUA}${target.name}${ChatColor.GREEN}.")

                if (Bukkit.getPlayer(target.id) != null) {
                    val message = FancyMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.YELLOW}You have received a friend request from ${ChatColor.AQUA}${sender.name} ")
                    message.then("${ChatColor.GREEN}[Click to Accept]").tooltip("${ChatColor.GREEN}Click to accept ${ChatColor.AQUA}${target.name} ${ChatColor.GREEN}friend request!").toJSONString()
                    message.command("/friend accept ${sender.name}")
                    message.send(Bukkit.getPlayer(target.id))
                }

                Shared.profileManager.repository.update(target)
            } else {
                sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name} ${ChatColor.RED}already has a pending friend request from you.")
            }
        } else {
            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is already your friend.")
        }
    }

    @Command(names = ["friend remove"], description = "Remove a friend from your list.", permission = "")
    @JvmStatic
    fun remove(
        sender: Player,
        @Param(name = "player") target: Profile
    ) {
        val profile = Shared.profileManager.findById(sender.uniqueId)!!

        if (profile.friends.contains(target.id)) {
            profile.friends.remove(target.id)
            target.friends.remove(sender.uniqueId)
            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.GREEN}You have removed ${ChatColor.AQUA}${target.name}${ChatColor.GREEN} from your friends list.")

            if (Bukkit.getPlayer(target.id) != null) {
                Bukkit.getPlayer(target.id).sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${sender.name} ${ChatColor.RED}has removed you from their friends list.")
            }

            Shared.profileManager.repository.update(profile)
            Shared.profileManager.repository.update(target)
        } else {
            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is not in your friends list.");
        }
    }

    @Command(names = ["friend accept"], description = "Accept a friend request.", permission = "")
    @JvmStatic
    fun accept(
        sender: Player,
        @Param(name = "player") target: Profile
    ) {
        val profile = Shared.profileManager.findById(sender.uniqueId)!!

        if (profile.requests.contains(target.id)) {
            profile.friends.add(target.id)
            profile.requests.remove(target.id)
            Shared.profileManager.repository.update(profile)

            target.friends.add(sender.uniqueId)
            target.requests.remove(sender.uniqueId)
            Shared.profileManager.repository.update(target)

            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.GREEN}You have accepted the friend request from ${ChatColor.AQUA}${target.name}${ChatColor.GREEN}.")

            if (Bukkit.getPlayer(target.id) != null) {
                Bukkit.getPlayer(target.id).sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${sender.name} ${ChatColor.YELLOW}and you are now friends.")
            }
        } else {
            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is not in your friend requests list.")
        }
    }

    @Command(names = ["friend deny"], description = "Deny a friend request.", permission = "")
    @JvmStatic
    fun deny(
        sender: Player,
        @Param(name = "player") target: Profile
    ) {
        val profile = Shared.profileManager.findById(sender.uniqueId)!!

        if (profile.requests.contains(target.id)) {
            target.requests.remove(sender.uniqueId)
            profile.requests.remove(target.id)
            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.RED}You have denied the friend request from ${ChatColor.AQUA}${target.name}${ChatColor.RED}.")

            if (Bukkit.getPlayer(target.id) != null) {
                Bukkit.getPlayer(target.id).sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${sender.name} ${ChatColor.RED}has denied your friend request.")
            }

            Shared.profileManager.repository.update(target)
        } else {
            sender.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is not in your friend requests list.")
        }

    }

    @JvmStatic
    fun list() {

    }

    @JvmStatic
    fun info(
        sender: Player,
        @Param(name = "player") target: Profile
    ) {

    }

}