package dev.ryu.core.bukkit.listener

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.manager.StaffModeManager
import dev.ryu.core.bukkit.system.staffmode.hotbar.impl.AnnihilationHotbar
import dev.ryu.core.bukkit.system.staffmode.hotbar.impl.PracticeHotbar
import dev.ryu.core.bukkit.system.staffmode.type.StaffModeType
import com.starlight.nexus.util.Color
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPickupItemEvent

object StaffModeHotbarListener : Listener {

    @EventHandler
    fun staffHotbarAction(event: PlayerInteractEvent) {
        if (StaffModeManager.hasStaffModeEnabled(event.player)) {
            when(dev.ryu.core.bukkit.Core.get().staffModeType) {
                StaffModeType.PRACTICE ->  {
                    for (item in PracticeHotbar.entries) {
                        if (event.player.itemInHand.type == item.material && event.player.itemInHand.hasItemMeta() && event.player.itemInHand.itemMeta.displayName == Color.color(item.display)) {
                            when (item) {
                                PracticeHotbar.BAN_HAMMER -> {
                                    event.player.sendMessage("ban hammer")
                                }
                                PracticeHotbar.FREEZE -> {
                                    event.player.sendMessage("freeze")
                                }
                                PracticeHotbar.COMPASS -> {
                                    event.player.sendMessage("compass")
                                }
                                PracticeHotbar.TROLL_GUI -> {
                                    event.player.sendMessage("troll gui")
                                }
                                PracticeHotbar.RANDOM_TP -> {
                                    event.player.sendMessage("random tp")
                                }
                                PracticeHotbar.INVENTORY_VIEW -> {
                                    event.player.sendMessage("inv view")
                                }
                                PracticeHotbar.VANISH -> {
                                    if (StaffModeManager.hasStaffVanished(event.player)) {
                                        StaffModeManager.disableStaffMode(event.player)
                                    } else {
                                        StaffModeManager.enableStaffMode(event.player)
                                    }
                                }
                            }
                        }
                    }
                }
                StaffModeType.ANNIHILATION -> {
                    for (item in AnnihilationHotbar.entries) {
                        if (event.player.itemInHand.type == item.material && event.player.itemInHand.hasItemMeta() && event.player.itemInHand.itemMeta.displayName == Color.color(item.display)) {
                            when (item) {
                                AnnihilationHotbar.VIEW_INVENTORY -> {
                                    event.player.sendMessage("View inventory annihilation")
                                }
                                AnnihilationHotbar.FREEZE -> {
                                    //seguir hacinedo jajajajj
                                }
                                AnnihilationHotbar.KNOCKBACK_STICK -> {
                                    event.isCancelled = false
                                }
                                AnnihilationHotbar.GUI -> {
                                    event.player.performCommand("/staff gui")
                                }
                                AnnihilationHotbar.RANDOM_TELEPORT -> {
                                    val allOnlinePlayers = Bukkit.getOnlinePlayers()
                                    val eligiblePlayers = allOnlinePlayers.filter { !it.hasPermission("core.staff") && !it.isOp }

                                    if (eligiblePlayers.isNotEmpty()) {
                                        val randomPlayer = eligiblePlayers.random()
                                        event.player.teleport(randomPlayer.location)
                                    } else {
                                        event.player.sendMessage("${ChatColor.RED}No eligible players to teleport to.")
                                    }
                                }
                            }
                        }
                    }
                    return
                }
                StaffModeType.GENERAL -> {
                    event.player.sendMessage("${ChatColor.BLUE}[Staff Mode] ${ChatColor.RED}Not implemented.")
                    return
                }
            }
        }
    }

    @EventHandler
    fun noMoveItems(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        if (StaffModeManager.hasStaffModeEnabled(player)) {
            player.sendMessage("${ChatColor.RED}${ChatColor.BOLD}You can't move inventory items while in staff mode!")

            event.isCancelled = true
        }
    }

    @EventHandler
    fun noPickUpItems(event: PlayerPickupItemEvent) {
        if (StaffModeManager.hasStaffModeEnabled(event.player)) {
            event.player.sendMessage("${ChatColor.RED}${ChatColor.BOLD}You can't pickup items while in staff mode!")

            event.isCancelled = true
        }
    }

    @EventHandler
    fun noDropItems(event: PlayerDropItemEvent) {
        if (StaffModeManager.hasStaffModeEnabled(event.player)) {
            event.player.sendMessage("${ChatColor.RED}${ChatColor.BOLD}You can't drop items while in staff mode!")

            event.isCancelled = true
        }
    }

    @EventHandler
    fun noBreakBlocks(event: BlockBreakEvent) {
        if (StaffModeManager.hasStaffModeEnabled(event.player)) {
            event.player.sendMessage("${ChatColor.RED}${ChatColor.BOLD}You can't break blocks while in staff mode!")

            event.isCancelled = true
        }
    }

    @EventHandler
    fun noPlaceBlocks(event: BlockPlaceEvent) {
        if (StaffModeManager.hasStaffModeEnabled(event.player)) {
            event.player.sendMessage("${ChatColor.RED}${ChatColor.BOLD}You can't place blocks while in staff mode!")

            event.isCancelled = true
        }
    }

}