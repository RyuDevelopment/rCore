package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object SpeedCommand {

    @Command(names = ["speed", "spd"], permission = "core.admin", description = "Change your walk or fly speed")
    @JvmStatic
    fun speed(sender: Player, @Param(name = "speed") speed: Int) {
        if (speed < 0 || speed > 10) {
            sender.sendMessage(ChatColor.RED.toString() + "Speed must be between 0 and 10.")
            return
        }
        val fly = sender.isFlying
        if (fly) {
            sender.flySpeed = getSpeed(speed, true)
        } else {
            sender.walkSpeed = getSpeed(speed, false)
        }
        sender.sendMessage(ChatColor.GOLD.toString() + (if (fly) "Fly" else "Walk") + " set to " + ChatColor.WHITE + speed + ChatColor.GOLD + ".")
    }

    private fun getSpeed(speed: Int, isFly: Boolean): Float {
        val defaultSpeed = if (isFly) 0.1f else 0.2f
        val maxSpeed = 1.0f
        if (speed.toFloat() < 1.0f) {
            return defaultSpeed * speed.toFloat()
        }
        val ratio = (speed.toFloat() - 1.0f) / 9.0f * (1.0f - defaultSpeed)
        return ratio + defaultSpeed
    }

}