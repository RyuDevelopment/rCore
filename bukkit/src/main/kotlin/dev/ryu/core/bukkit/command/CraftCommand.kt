package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import org.bukkit.entity.Player

object CraftCommand {

    @Command(names = ["craft", "workbench", "crafting"], permission = "command.craft")
    @JvmStatic
    fun craft(
        player: Player
    ) {
        player.openWorkbench(null, true)
    }

}