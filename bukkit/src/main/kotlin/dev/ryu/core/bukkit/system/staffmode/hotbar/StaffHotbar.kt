package dev.ryu.core.bukkit.system.staffmode.hotbar

import org.bukkit.Material

/*
    * Author: T4yrn
    * Project: core
    * Date: 1/3/2024 - 21:15
*/

interface StaffHotbar {

    val material: Material
    val display: String
    val slot: Int
    val byte: Short

}