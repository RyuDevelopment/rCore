package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.ItemBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import java.util.*

object SpawnerCommand {

    @Command(names = ["spawner", "changespawner"], permission = "command.spawner")
    @JvmStatic
    fun spawner(player: Player, @Param(name = "entityType") entityType: String) {
        val type: EntityType?
        type = try {
            EntityType.valueOf(entityType.uppercase(Locale.getDefault()))
        } catch (ex: Exception) {
            null
        }
        if (type == null) {
            player.sendMessage(ChatColor.RED.toString() + "Not an entity named '" + entityType + "'.")
            return
        }
        player.inventory.addItem(
            ItemBuilder(Material.MOB_SPAWNER).name(ChatColor.GREEN.toString() + type.getName() + " Spawner").build()
        )
    }

}