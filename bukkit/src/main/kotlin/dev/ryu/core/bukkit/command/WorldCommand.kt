package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerTeleportEvent

object WorldCommand {

    @Command(names = ["world", "changeworld"], permission = "command.world")
    @JvmStatic
    fun world(
        player: Player,
        @Param(name = "world") worldName: String
    ) {
        val world = Bukkit.getWorld(worldName)
        if (world == null) {
            player.sendMessage(ChatColor.RED.toString() + "World '" + worldName + "' not found.")
            return
        }
        if (player.world == world) {
            player.sendMessage(ChatColor.RED.toString() + "You are already in that world.")
            return
        }
        val origin = player.location
        val location = Location(world, origin.x, origin.y, origin.z, origin.yaw, origin.pitch)
        player.sendMessage(ChatColor.GOLD.toString() + "Teleporting...")
        player.teleport(location, PlayerTeleportEvent.TeleportCause.COMMAND)
    }

}