package dev.ryu.core.bukkit.menu.friend

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import com.starlight.nexus.menu.ConfirmMenu
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.Button.styleAction
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.Callback
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.TextSplitter
import com.starlight.nexus.util.UnicodeUtil
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.prompt.friend.FriendAddPrompt
import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.bukkit.util.Constants
import dev.ryu.core.bukkit.util.protocol
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.module.GrantModule
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType


class FriendsMenu: PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(p0: Player): String {
        return "${ChatColor.GRAY}Friends"
    }

    override fun getAllPagesButtons(p0: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            val profile = CoreAPI.profileManager.findById(p0.uniqueId)!!

            var index = 0
            profile.friends.forEach { friend ->
                val target = CoreAPI.profileManager.findById(friend)!!
                val targetRank = CoreAPI.grantManager.findBestRank(GrantModule.repository.findAllByPlayer(target.id))

                if (p0.protocol <= 20) {
                    toReturn[index] = MenuButton()
                        .name("${ChatColor.valueOf(targetRank.color)}${target.name}")
                        .icon(Material.PAPER)
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Connected${ChatColor.GRAY}: ${if (target.online) "${ChatColor.GREEN}Online" else "${ChatColor.RED}Offline"}")
                                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Server${ChatColor.GRAY}: ${if (target.online) "${ChatColor.AQUA}${target.currentServer}" else "${ChatColor.RED}N/A"}")
                                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Rank${ChatColor.GRAY}: ${Color.color(targetRank.display)}")
                                lore.add("")
                                lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to follow ${ChatColor.valueOf(targetRank.color)}${target.name} ${ChatColor.GREEN}on the network."))
                                lore.add(styleAction(ChatColor.RED, "RIGHT-CLICK", "to remove ${ChatColor.valueOf(targetRank.color)}${target.name} ${ChatColor.RED}from your friend list."))
                            }
                        }
                        .action(ClickType.LEFT) {
                            if (target.online) {
                                if (profile.currentServer == target.currentServer) {
                                    p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.RED}You are already on ${ChatColor.AQUA}${target.name}'s ${ChatColor.RED}server.");
                                    return@action
                                }
                                sendPlayerToServer(p0, target.currentServer!!)
                            } else {
                                p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name} ${ChatColor.RED}is not connected to the network.")
                            }
                        }
                        .action(ClickType.RIGHT) {
                            ConfirmMenu("¿Sure?",
                                object : Callback<Boolean> {
                                    override fun callback(callback: Boolean) {
                                        if (callback) {
                                            p0.closeInventory()
                                            if (profile.friends.contains(target.id)) {
                                                profile.friends.remove(target.id)
                                                target.friends.remove(p0.uniqueId)
                                                p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.GREEN}You have removed ${ChatColor.AQUA}${target.name}${ChatColor.GREEN} from your friends list.")

                                                if (Bukkit.getPlayer(target.id) != null) {
                                                    Bukkit.getPlayer(target.id).sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${p0.name} ${ChatColor.RED}has removed you from their friends list.")
                                                }

                                                CoreAPI.profileManager.repository.update(profile)
                                                CoreAPI.profileManager.repository.update(target)
                                            } else {
                                                p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is not in your friends list.");
                                            }
                                            FriendsMenu().openMenu(p0)
                                        } else {
                                            p0.closeInventory()
                                            FriendsMenu().openMenu(p0)
                                        }
                                    }
                                },
                            ).openMenu(p0)
                        }
                } else {
                    toReturn[index] = MenuButton()
                        .name("${ChatColor.valueOf(targetRank.color)}${target.name}")
                        .playerTexture(target.name)
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Connected${ChatColor.GRAY}: ${if (target.online) "${ChatColor.GREEN}Online" else "${ChatColor.RED}Offline"}")
                                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Server${ChatColor.GRAY}: ${if (target.online) "${ChatColor.AQUA}${target.currentServer}" else "${ChatColor.RED}N/A"}")
                                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Rank${ChatColor.GRAY}: ${Color.color(targetRank.display)}")
                                lore.add("")
                                lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to follow ${ChatColor.valueOf(targetRank.color)}${target.name} ${ChatColor.GREEN}on the network."))
                                lore.add(styleAction(ChatColor.RED, "RIGHT-CLICK", "to remove ${ChatColor.valueOf(targetRank.color)}${target.name} ${ChatColor.RED}from your friend list."))
                            }
                        }
                        .action(ClickType.LEFT) {
                            if (target.online) {
                                if (profile.currentServer == target.currentServer) {
                                    p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.RED}You are already on ${ChatColor.AQUA}${target.name}'s ${ChatColor.RED}server.");
                                    return@action
                                }
                                sendPlayerToServer(p0, target.currentServer!!)
                            } else {
                                p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name} ${ChatColor.RED}is not connected to the network.")
                            }
                        }
                        .action(ClickType.RIGHT) {
                            ConfirmMenu("¿Sure?",
                                object : Callback<Boolean> {
                                    override fun callback(callback: Boolean) {
                                        if (callback) {
                                            p0.closeInventory()
                                            if (profile.friends.contains(target.id)) {
                                                profile.friends.remove(target.id)
                                                target.friends.remove(p0.uniqueId)
                                                p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.GREEN}You have removed ${ChatColor.AQUA}${target.name}${ChatColor.GREEN} from your friends list.")

                                                if (Bukkit.getPlayer(target.id) != null) {
                                                    Bukkit.getPlayer(target.id).sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${p0.name} ${ChatColor.RED}has removed you from their friends list.")
                                                }

                                                CoreAPI.profileManager.repository.update(profile)
                                                CoreAPI.profileManager.repository.update(target)
                                            } else {
                                                p0.sendMessage("${Lang.FRIEND_PREFIX.value}${ChatColor.AQUA}${target.name}${ChatColor.YELLOW} is not in your friends list.");
                                            }
                                            FriendsMenu().openMenu(p0)
                                        } else {
                                            p0.closeInventory()
                                            FriendsMenu().openMenu(p0)
                                        }
                                    }
                                },
                            ).openMenu(p0)
                        }
                }

                index++
            }
        }
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            if (player.protocol <= 20) {
                toReturn[4] = MenuButton()
                    .name("${ChatColor.GREEN}Add new Friend")
                    .icon(Material.EMERALD)
                    .lore { _ ->
                        arrayListOf<String>().also { lore ->
                            lore.add("")
                            lore.addAll(TextSplitter.split(text = "Adding a new friend is easy! Just type their name in the chat. Expand your social circle and enjoy more connections!"))
                            lore.add("")
                            lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add new friend."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(FriendAddPrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
            } else {
                toReturn[4] = MenuButton()
                    .name("${ChatColor.GREEN}Add new Friend")
                    .texturedIcon(Constants.GREEN_PLUS_TEXTURE)
                    .lore { _ ->
                        arrayListOf<String>().also { lore ->
                            lore.add("")
                            lore.addAll(TextSplitter.split(text = "Adding a new friend is easy! Just type their name in the chat. Expand your social circle and enjoy more connections!"))
                            lore.add("")
                            lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add new friend."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(FriendAddPrompt(player)).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
                    }
            }

            val profile = CoreAPI.profileManager.findById(player.uniqueId)!!
            if (profile.requests.size >= 1) {
                toReturn[40] = MenuButton()
                    .name("${ChatColor.LIGHT_PURPLE}Friend Requests")
                    .icon(Material.CHEST)
                    .lore { _ ->
                        arrayListOf<String>().also { lore ->
                            lore.add("")
                            lore.addAll(TextSplitter.split(text = "Check your friend requests, you might have some and could form a new friendship :)"))
                            lore.add("")
                            lore.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to view your friends requests."))
                        }
                    }
                    .action(ClickType.LEFT) {
                        player.closeInventory()
                        FriendsRequestMenu().openMenu(player)
                    }
            }
        }
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }

    override fun size(player: Player): Int {
        return 45
    }

    private fun sendPlayerToServer(player: Player, serverName: String) {
        val out: ByteArrayDataOutput = ByteStreams.newDataOutput()
        out.writeUTF("Connect")
        out.writeUTF(serverName)
        player.sendPluginMessage(Core.get(), "BungeeCord", out.toByteArray())
    }

}