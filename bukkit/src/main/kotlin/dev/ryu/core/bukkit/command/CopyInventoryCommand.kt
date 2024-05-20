package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import java.util.function.Consumer

object CopyInventoryCommand {

    @Command(names = ["cpinv", "cpfrom", "copyinv"], permission = "core.admin")
    @JvmStatic
    fun cpinv(sender: Player, @Param(name = "target") player: Player) {
        sender.inventory.contents = player.inventory.contents
        sender.inventory.armorContents = player.inventory.armorContents
        sender.health = player.health
        sender.foodLevel = player.foodLevel
        player.activePotionEffects.forEach(Consumer { potionEffect: PotionEffect? ->
            sender.addPotionEffect(
                potionEffect
            )
        })
        sender.sendMessage(player.displayName + ChatColor.GOLD + "'s inventory has been applied to you.")
    }

    @Command(names = ["cpto", "invto", "copyinvto", "cpinvto"], permission = "core.admin")
    @JvmStatic
    fun cpto(sender: Player, @Param(name = "target") player: Player) {
        player.inventory.contents = sender.inventory.contents
        player.inventory.armorContents = sender.inventory.armorContents
        player.health = sender.health
        player.foodLevel = sender.foodLevel
        sender.activePotionEffects.forEach(Consumer { potionEffect: PotionEffect? ->
            player.addPotionEffect(
                potionEffect
            )
        })
        sender.sendMessage(Color.color(ChatColor.GOLD.toString() + "Your inventory has been applied to " + ChatColor.WHITE + player.displayName + ChatColor.GOLD + "."))
    }

}