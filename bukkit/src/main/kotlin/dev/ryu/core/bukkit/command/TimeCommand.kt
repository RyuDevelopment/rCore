package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.util.Color
import org.bukkit.entity.Player

object TimeCommand {

    @Command(names = ["sun", "day"], permission = "core.admin", description = "Set time of the server to day")
    @JvmStatic
    fun sun(player: Player) {
        player.sendMessage(Color.color("&6Sun has begun!"))
        player.location.world.time = 1000
        player.location.world.setStorm(false)
        player.location.world.isThundering = false
    }

    @Command(names = ["night"], permission = "core.admin", description = "Set time of the server to night")
    @JvmStatic
    fun night(player: Player) {
        player.sendMessage(Color.color("&6Moon was turn up!"))
        player.location.world.time = 12000
    }

    @Command(names = ["weather clear"], permission = "core.admin", description = "Set weather to off")
    @JvmStatic
    fun weatherclear(player: Player) {
        player.sendMessage(Color.color("&6Weather was cleared!"))
        player.location.world.setStorm(false)
        player.location.world.isThundering = false
    }

    @Command(names = ["weather thunder"], permission = "core.admin", description = "Set weather thunders to off")
    @JvmStatic
    fun weatherthunder(player: Player) {
        if (!player.location.world.isThundering) {
            player.sendMessage(Color.color("&6Thunders are flashing!"))
            player.location.world.isThundering = true
        } else {
            player.sendMessage(Color.color("&6Thunders was cleared!"))
            player.location.world.isThundering = false
        }
    }

    @Command(names = ["weather storm"], permission = "core.weather.storm", description = "Set weather storm to off")
    @JvmStatic
    fun weatherstorm(player: Player) {
        if (!player.location.world.hasStorm()) {
            player.location.world.setStorm(true)
            player.sendMessage(Color.color("&6Storm is coming on!"))
        } else {
            player.sendMessage(Color.color("&6Storm weather was cleared!"))
            player.location.world.isThundering = false
        }
    }
    
}