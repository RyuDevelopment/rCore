package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

object TopCommand {

    @Command(
        names = ["top", "teleporttohighposition"],
        permission = "core.staff",
        description = "Teleport to highest location."
    )
    @JvmStatic
    fun top(player: Player) {
        val location: Location? = getHighestLocation(player.location)
        if (location != null) {
            player.sendMessage("${ChatColor.GOLD}Teleported to the highest position.")
            player.teleport(location)
        }
    }

    fun getHighestLocation(origin: Location): Location? {
        origin ?: throw IllegalArgumentException("The location cannot be null")
        val cloned = origin.clone()
        val world = cloned.world
        val x = cloned.blockX
        val z = cloned.blockZ
        for (y in origin.blockY downTo 0) {
            val block = world.getBlockAt(x, y, z)
            if (!block.isEmpty()) {
                val next = block.location
                next.pitch = origin.pitch
                next.yaw = origin.yaw
                return next
            }
        }
        return null
    }

}