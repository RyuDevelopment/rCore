package dev.ryu.core.bukkit.menu.friend

import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import dev.ryu.core.bukkit.util.protocol
import dev.ryu.core.shared.Shared
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

class FriendsRequestMenu : PaginatedMenu() {

    override fun getPrePaginatedTitle(p0: Player): String {
        return "${ChatColor.GRAY}Your friend requests!"
    }

    override fun getAllPagesButtons(p0: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            val profile = Shared.profileManager.findById(p0.uniqueId)!!

            var index = 0
            profile.requests.forEach {request ->
                val requestProfile = Shared.profileManager.findById(request)!!

                if (p0.protocol <= 20) {
                    toReturn[index] = MenuButton()
                        .name("${ChatColor.AQUA}${requestProfile.name}")
                        .icon(Material.PAPER)
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.add(Button.styleAction(ChatColor.GREEN, "LEFT-CLICK", "to add new friend."))
                            }
                        }
                        .action(ClickType.LEFT) {
                            p0.performCommand("friend accept ${requestProfile.name}")
                        }
                } else {
                    toReturn[index] = MenuButton()
                        .name("${ChatColor.AQUA}${requestProfile.name}")
                        .playerTexture(requestProfile.name)
                        .lore { _ ->
                            arrayListOf<String>().also { lore ->
                                lore.add("")
                                lore.add(Button.styleAction(ChatColor.GREEN, "LEFT-CLICK", "to accept ${ChatColor.AQUA}${requestProfile.name}'s ${ChatColor.GREEN}friend request."))
                            }
                        }
                        .action(ClickType.LEFT) {
                            p0.performCommand("friend accept ${requestProfile.name}")
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

            toReturn[36] = MenuButton()
                .icon(Material.ARROW)
                .name("${ChatColor.RED}Go Back")
                .action(ClickType.LEFT) {
                    player.closeInventory()
                    FriendsMenu().openMenu(player)
                }
        }
    }

    override fun size(player: Player): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }
}