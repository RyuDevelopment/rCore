package dev.ryu.core.bukkit.command.staff

import dev.ryu.core.bukkit.manager.StaffModeManager
import com.starlight.nexus.command.Command
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 2/3/2024 - 20:13
*/

object StaffCommands {

    @Command(names = ["staffmode","mod","staff"], permission = "core.staff")
    @JvmStatic
    fun execute(
        player: Player
    ) {
        if (StaffModeManager.hasStaffModeEnabled(player)) {
            StaffModeManager.disableStaffMode(player)
        } else {
            StaffModeManager.enableStaffMode(player)
        }
    }

    @Command(names = ["vanish","v"], permission = "core.staff")
    @JvmStatic
    fun vanish(
        player: Player
    ) {
        if (StaffModeManager.hasStaffVanished(player)) {
            StaffModeManager.disableVanish(player)
        } else {
            StaffModeManager.enableVanish(player)
        }
    }

}