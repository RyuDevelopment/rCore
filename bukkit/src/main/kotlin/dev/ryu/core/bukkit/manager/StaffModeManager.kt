package dev.ryu.core.bukkit.manager

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.system.staffmode.hotbar.impl.AnnihilationHotbar
import dev.ryu.core.bukkit.system.staffmode.hotbar.impl.PracticeHotbar
import dev.ryu.core.bukkit.system.staffmode.type.StaffModeType.*
import com.starlight.nexus.logging.LogHandler
import com.starlight.nexus.util.ItemBuilder
import com.starlight.nexus.util.Tasks
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.scheduler.BukkitRunnable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.math.tan

object StaffModeManager {

    private val staffInModMode: ConcurrentMap<Player, Boolean> = ConcurrentHashMap()
    private val staffVanished: ConcurrentMap<Player, Boolean> = ConcurrentHashMap()
    private val playerInFreezeMode: ConcurrentMap<Player, Boolean> = ConcurrentHashMap()
    private val playerInventoryMap: MutableMap<String, StaffModeManager> = HashMap()
    private var armorContents: Array<ItemStack?> = arrayOfNulls(4)
    private var inventoryContents: Array<ItemStack?> = arrayOfNulls(36)

    @JvmStatic
    fun enableStaffMode(player: Player) {
        staffInModMode[player] = true

        savePlayerInventory(player)

        player.setMetadata("staffmode", FixedMetadataValue(dev.ryu.core.bukkit.Core.get(), "staffmode"))

        when (dev.ryu.core.bukkit.Core.get().staffModeType) {
            PRACTICE -> {
                val vanishItem = PracticeHotbar.VANISH

                for (item in PracticeHotbar.entries) {
                    if (item == vanishItem) {
                        if (hasStaffVanished(player)) {
                            player.inventory.setItem(item.slot, ItemBuilder.of(item.material).name(item.display).data(10).build())
                        } else {
                            player.inventory.setItem(item.slot, ItemBuilder.of(item.material).name(item.display).data(8).build())
                        }
                    } else {
                        player.inventory.setItem(item.slot, ItemBuilder.of(item.material).name(item.display).data(item.byte).build())
                        player.gameMode = GameMode.CREATIVE

                        player.updateInventory()
                    }
                }

                player.sendMessage("${ChatColor.GREEN}You have entered 'Practice' staff mode.")
                return
            }
            ANNIHILATION -> {
                val knockbackStick = AnnihilationHotbar.KNOCKBACK_STICK
                for (item in AnnihilationHotbar.entries) {
                    if (item == knockbackStick) {
                        player.inventory.setItem(knockbackStick.slot, ItemBuilder.of(knockbackStick.material).enchant(Enchantment.KNOCKBACK, 1).name(knockbackStick.display).data(knockbackStick.byte).build())
                    } else {
                        player.inventory.setItem(item.slot, ItemBuilder.of(item.material).name(item.display).data(item.byte).build())
                    }
                    player.gameMode = GameMode.CREATIVE

                    player.updateInventory()
                }

                player.sendMessage("${ChatColor.GREEN}You have entered 'Annihilation' staff mode.")
                return
            }
            GENERAL -> {
                player.sendMessage("${ChatColor.BLUE}[Staff Mode] ${ChatColor.RED}Not implemented.")
                return
            }
        }
    }

    @JvmStatic
    fun disableStaffMode(player: Player) {
        staffInModMode[player] = false
        staffInModMode.remove(player)

        restorePlayerInventory(player)

        player.removeMetadata("staffMode", dev.ryu.core.bukkit.Core.get())

        player.gameMode = GameMode.SURVIVAL

        player.sendMessage("${ChatColor.RED}You have exited staff mode.")
    }

    @JvmStatic
    fun enableVanish(staff: Player) {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (!player.hasPermission("core.staff")) {
                player.hidePlayer(staff)
            }
        }

        val vanishItem = PracticeHotbar.VANISH

        staff.inventory.setItem(vanishItem.slot, ItemBuilder.of(vanishItem.material).name(vanishItem.display).data(10).build())
        staff.updateInventory()

        staff.spigot().collidesWithEntities = false;
        staff.setMetadata("invisible", FixedMetadataValue(dev.ryu.core.bukkit.Core.get(), true))
        staff.sendMessage("${ChatColor.GREEN}You have entered vanish mode! Now only staff members can see you.")
    }

    @JvmStatic
    fun disableVanish(staff: Player) {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (!player.hasPermission("core.staff")) {
                player.showPlayer(staff)
            }
        }

        val vanishItem = PracticeHotbar.VANISH

        staff.inventory.setItem(vanishItem.slot, ItemBuilder.of(vanishItem.material).name(vanishItem.display).data(8).build())
        staff.updateInventory()

        staff.spigot().collidesWithEntities = true;
        staff.removeMetadata("invisible", dev.ryu.core.bukkit.Core.get())
        staff.sendMessage("${ChatColor.YELLOW}You have exited vanish mode. Now all players can see you again!")
    }

    @JvmStatic
    fun freezePlayer(sender: Player, target: Player) {
        if (!target.isOp) {
            this.playerInFreezeMode[target] = true

            savePlayerInventory(target)

            target.setMetadata("frozen", FixedMetadataValue(dev.ryu.core.bukkit.Core.get(), "frozen"))
            sender.sendMessage("${ChatColor.RED}${ChatColor.BOLD}${target.name} is now frozen.")

        } else {
            sender.sendMessage("${ChatColor.RED}That player cannot be frozen.")
        }
    }

    @JvmStatic
    fun unFreezePlayer(sender: Player, target: Player) {
        if (!target.hasMetadata("frozen")) {
            sender.sendMessage("${ChatColor.RED}${ChatColor.BOLD}${target.name} is not currently frozen.")
        } else {
            this.playerInFreezeMode.remove(target)

            restorePlayerInventory(target)

            target.removeMetadata("frozen", dev.ryu.core.bukkit.Core.get())
            sender.sendMessage("${ChatColor.GREEN}${ChatColor.BOLD}${target.name} is now unfrozen.")
        }
    }

    @JvmStatic
    fun isPlayerFrozen(player: Player): Boolean {
        return playerInFreezeMode[player] == true || player.hasMetadata("frozen")
    }

    @JvmStatic
    fun hasStaffModeEnabled(player: Player): Boolean {
        return staffInModMode[player] == true
    }

    @JvmStatic
    fun hasStaffVanished(player: Player) : Boolean {
        return staffVanished[player] == true
    }

    @JvmStatic
    fun getAllStaffInStaffMode(): List<Player> {
        return staffInModMode.entries.filter { it.value }.map { it.key }
    }

    @JvmStatic
    fun getAllStaffVanished(): List<Player> {
        return staffVanished.entries.filter { it.value }.map { it.key }
    }

    private fun savePlayerInventory(player: Player) {
        if (player.inventory != null) {
            for (i in 0 until 4) {
                if (player.inventory.armorContents[i] != null) {
                    armorContents[i] = player.inventory.armorContents[i].clone()
                }
            }

            for (i in 0 until 36) {
                if (player.inventory.contents[i] != null) {
                    inventoryContents[i] = player.inventory.contents[i].clone()
                }
            }

            val playerName = player.name ?: return
            playerInventoryMap[playerName] = this

            player.inventory.armorContents = null
            player.inventory.clear()
        }
    }

    private fun restorePlayerInventory(player: Player) {
        if (player.inventory != null) {
            player.inventory.armorContents = armorContents.clone()
            player.inventory.contents = inventoryContents.clone()

            val playerName = player.name ?: return
            if (playerInventoryMap.containsKey(playerName)) {
                playerInventoryMap.remove(playerName)
            }
        }

        armorContents = arrayOfNulls(4)
        inventoryContents = arrayOfNulls(36)
        playerInventoryMap.clear()
    }

}