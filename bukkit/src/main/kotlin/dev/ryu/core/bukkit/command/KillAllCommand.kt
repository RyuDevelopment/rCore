package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.World
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType

object KillAllCommand {

    @Command(names = ["killall", "clearmobs", "clearentities", "killentities"], permission = "core.admin")
    @JvmStatic
    fun killallCommand(sender: CommandSender) {
        for (world in Bukkit.getWorlds()) {
            sender.sendMessage(ChatColor.GOLD.toString() + "Cleared " + ChatColor.WHITE + clearEntities(world) + ChatColor.GOLD + " entities from " + ChatColor.WHITE + world.name)
        }
    }

    fun clearEntities(world: World): Int {
        var removed = 0
        for (entity in world.entities) {
            if (entity.type == EntityType.PLAYER) {
                continue
            }
            ++removed
            entity.remove()
        }
        return removed
    }

}