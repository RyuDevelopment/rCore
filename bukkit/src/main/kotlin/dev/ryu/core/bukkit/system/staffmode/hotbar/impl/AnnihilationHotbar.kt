package dev.ryu.core.bukkit.system.staffmode.hotbar.impl

import dev.ryu.core.bukkit.system.staffmode.hotbar.StaffHotbar
import com.starlight.nexus.util.Color
import org.bukkit.Material

/*
    * Author: T4yrn
    * Project: core
    * Date: 4/3/2024 - 21:56
*/

enum class AnnihilationHotbar(
    override val material: Material,
    override val display: String,
    override val slot: Int,
    override val byte: Short
) : StaffHotbar {

    VIEW_INVENTORY(Material.CHEST, Color.color("&aView Inventory"), 0, 0),
    FREEZE(Material.ICE,Color.color("&6Freeze"), 1, 0),
    KNOCKBACK_STICK(Material.STICK,Color.color("&bKnockback"), 4,0),
    GUI(Material.BLAZE_POWDER,Color.color("&dGUI"), 7,0),
    RANDOM_TELEPORT(Material.REDSTONE_TORCH_ON,Color.color("&5Random TP"), 8,0)

}