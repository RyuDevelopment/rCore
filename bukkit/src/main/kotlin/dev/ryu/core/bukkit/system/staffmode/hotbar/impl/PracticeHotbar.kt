package dev.ryu.core.bukkit.system.staffmode.hotbar.impl

import dev.ryu.core.bukkit.system.staffmode.hotbar.StaffHotbar
import org.bukkit.ChatColor
import org.bukkit.Material

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 2/3/2024 - 17:57
*/

enum class PracticeHotbar(
    override val material: Material,
    override val display: String,
    override val slot: Int,
    override val byte: Short
) : StaffHotbar {

    BAN_HAMMER(Material.IRON_AXE, "${ChatColor.RED}Ban Hammer", 0, 0),
    FREEZE(Material.ICE, "${ChatColor.GOLD}Freeze", 1, 0),
    COMPASS(Material.COMPASS, "${ChatColor.GOLD}Compass", 2, 0),
    TROLL_GUI(Material.IRON_FENCE, "${ChatColor.GOLD}Troll GUI", 4, 0),
    RANDOM_TP(Material.ENDER_PORTAL_FRAME, "${ChatColor.GOLD}Random TP", 6, 0),
    INVENTORY_VIEW(Material.BOOK, "${ChatColor.GOLD}View Inventory", 7, 0),
    VANISH(Material.INK_SACK, "${ChatColor.GREEN}Vanished", 8, 10),

}