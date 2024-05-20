package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

object PingCommand {

    @Command(names = ["ping", "latency"], permission = "")
    @JvmStatic
    fun ping(sender: Player, @Param(name = "target", defaultValue = "self") target: Player) {
        val ping: Int = getPing(target)
        sender.sendMessage(target.displayName + ChatColor.YELLOW + "'s Ping: " + ChatColor.RED + ping)
    }

    fun getPing(player: Player): Int {
        return (player as CraftPlayer).handle.ping
    }

}