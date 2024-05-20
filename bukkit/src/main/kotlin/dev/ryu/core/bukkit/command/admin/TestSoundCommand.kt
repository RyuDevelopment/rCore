package dev.ryu.core.bukkit.command.admin

import dev.ryu.core.bukkit.menu.sound.SoundTestMenu
import com.starlight.nexus.command.Command
import org.bukkit.entity.Player

object TestSoundCommand {

    @Command(names = ["test sound"], permission = "op")
    @JvmStatic
    fun execute(player: Player) {
        SoundTestMenu().openMenu(player)
    }

}