package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.flag.Flag
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.player.OfflinePlayerWrapper
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Player

object TeleportationCommand {

    @Command(names = ["teleport", "tp", "tpto", "goto"], permission = "core.staff", description = "Teleport yourself to a player")
    @JvmStatic
    fun teleport(sender: Player, @Param(name = "player") wrapper: OfflinePlayerWrapper) {
        wrapper.loadAsync { player ->
            if (player == null) {
                sender.sendMessage("${ChatColor.RED}No online or offline player with the name ${wrapper.name} found.")
                return@loadAsync
            }
            if (sender.world != Bukkit.getPlayer(player.uniqueId)?.world) {
                sender.teleport(Bukkit.getPlayer(player.uniqueId)?.world?.spawnLocation)
            }
            sender.teleport(player)
            sender.sendMessage(Color.color("${ChatColor.GOLD}Teleporting you to ${if (player.isOnline) "" else "offline player "}${ChatColor.WHITE}${player.displayName}${ChatColor.GOLD}."))
        }
    }

    @Command(names = ["tphere", "bring", "s"], permission = "core.staff", description = "Teleport a player to you")
    @JvmStatic
    fun tphere(sender: Player, @Flag(value = ["s", "silentMode"], description = "Silently teleport the player (staff members always get messaged)") silent: Boolean, @Param(name = "player") target: Player) {
        if (sender.world != target.world) {
            target.teleport(sender.world.spawnLocation)
        }
        target.teleport(sender.location)
        sender.sendMessage("${ChatColor.GOLD}Teleporting ${ChatColor.WHITE}${target.displayName}${ChatColor.GOLD} to you.")
        if (!silent || target.hasPermission("core.staff")) {
            target.sendMessage(Color.color("${ChatColor.GOLD}Teleporting you to ${ChatColor.WHITE}${sender.displayName}${ChatColor.GOLD}."))
        }
    }

    @Command(names = ["tpall", "teleportationall", "teleportall"], permission = "core.admin", description = "Teleport all players to your location")
    @JvmStatic
    fun teleportAll(sender: Player) {
        Bukkit.getOnlinePlayers().forEach { player ->
            if (sender == player) return@forEach
            player.teleport(sender.location)
            player.sendMessage("${ChatColor.GOLD}You and the rest of players were teleported to ${sender.displayName}${ChatColor.GOLD}'s location.")
        }
        sender.sendMessage("${ChatColor.GOLD}All players were teleported to your location.")
    }

    @Command(
        names = ["tppos", "teleportposition", "teleportpos", "tpos"],
        permission = "core.staff",
        description = "Teleport to coordinates"
    )
    @JvmStatic
    fun teleport(
        sender: Player,
        @Param(name = "x") x: Double,
        @Param(name = "y") y: Double,
        @Param(name = "z") z: Double,
        @Param(name = "player", defaultValue = "self") target: Player
    ) {
        var x = x
        var z = z
        if (sender != target && !sender.hasPermission("core.staff")) {
            sender.sendMessage(ChatColor.RED.toString() + "No permission to teleport other players.")
            return
        }
        if (isBlock(x)) {
            x += if (z >= 0.0) 0.5 else -0.5
        }
        if (isBlock(z)) {
            z += if (x >= 0.0) 0.5 else -0.5
        }
        target.teleport(Location(target.world, x, y, z))
        val location =
            ChatColor.translateAlternateColorCodes('&', String.format("&e[&f%s&e, &f%s&e, &f%s&e]&6", x, y, z))
        if (sender != target) {
            sender.sendMessage(Color.color(ChatColor.GOLD.toString() + "Teleporting " + ChatColor.WHITE + target.displayName + ChatColor.GOLD + " to " + location + "."))
        }
        target.sendMessage(ChatColor.GOLD.toString() + "Teleporting you to " + location + ".")
    }
    private fun isBlock(value: Double): Boolean {
        return value % 1.0 == 0.0
    }
}