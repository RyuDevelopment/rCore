package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

object ChunksCommand {

    @Command(names = ["chunks"], permission = "core.admin")
    @JvmStatic
    fun chunks(sender: CommandSender) {
        sender.sendMessage("${ChatColor.GREEN}Loaded chunks per world:")
        for (world in Bukkit.getWorlds()) {
            sender.sendMessage("${ChatColor.YELLOW}${world.name}: ${ChatColor.RED}${world.loadedChunks.size}")
        }
    }

}