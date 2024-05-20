package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object FlyCommand {

    @Command(names = ["fly", "flight"], permission = "core.staff", description = "Toggle a player's fly mode")
    @JvmStatic
    private fun fly(
        sender: Player,
        @Param(name = "player", defaultValue = "self") target: Player
    ) {
        if (sender != target && !sender.hasPermission("core.staff")) {
            sender.sendMessage(ChatColor.RED.toString() + "No permission to set other player's fly mode.")
            return
        }
        target.allowFlight = !target.allowFlight
        if (sender != target) {
            sender.sendMessage(Color.color(target.displayName + ChatColor.GOLD + "'s fly mode was set to " + ChatColor.WHITE + target.allowFlight + ChatColor.GOLD + "."))
        }
        target.sendMessage(ChatColor.GOLD.toString() + "Your fly mode was set to " + ChatColor.WHITE + target.allowFlight + ChatColor.GOLD + ".")
    }

}