package dev.ryu.core.bukkit.command.coinshop

import com.starlight.nexus.command.Command
import dev.ryu.core.bukkit.menu.coinshop.CoinShopMenu
import org.bukkit.Sound
import org.bukkit.entity.Player

object CoinShopCommand {

    @Command(names = ["testshop"], permission = "", description = "Open the coin shop menu", hidden = true)
    @JvmStatic
    fun execute(
        sender: Player
    ) {
        sender.playSound(sender.location, Sound.ORB_PICKUP, 50.0f, 1.0f)
        CoinShopMenu().openMenu(sender)
    }

}