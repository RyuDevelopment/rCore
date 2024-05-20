package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

object SlapCommand {

    @Command(names = ["slap"], permission = "core.admin", description = "Teleport a player to height u want")
    @JvmStatic
    fun slap(player: Player, @Param(name = "target") target: Player, @Param(name = "height") eight: Int) {
        player.sendMessage(ChatColor.GREEN.toString() + "Successfully slapped " + target.displayName + ChatColor.GREEN + " + " + eight + " blocks up <o/")
        val location = target.location
        target.teleport(
            Location(
                Bukkit.getWorld(target.world.name),
                target.location.x,
                target.location.y + eight,
                target.location.z,
                target.location.pitch,
                target.location.yaw
            )
        )
    }
    
}