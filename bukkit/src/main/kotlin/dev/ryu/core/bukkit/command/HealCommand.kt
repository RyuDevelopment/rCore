package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.flag.Flag
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object HealCommand {

    private val NEGATIVE_EFFECTS: Set<PotionEffectType> = setOf(
        PotionEffectType.SLOW,
        PotionEffectType.SLOW_DIGGING,
        PotionEffectType.HARM,
        PotionEffectType.CONFUSION,
        PotionEffectType.BLINDNESS,
        PotionEffectType.HUNGER,
        PotionEffectType.WEAKNESS,
        PotionEffectType.POISON,
        PotionEffectType.WITHER
    )

    @Command(names = ["heal"], permission = "core.admin", description = "Heal a player.")
    @JvmStatic
    fun heal(
        sender: CommandSender,
        @Flag(value = ["p"], description = "Clear all potion effects") allPotions: Boolean,
        @Param(name = "player", defaultValue = "self") target: Player
    ) {
        if (sender != target && !sender.hasPermission("core.staff")) {
            sender.sendMessage(ChatColor.RED.toString() + "No permission to heal other players.")
            return
        }
        target.foodLevel = 20
        target.saturation = 10.0f
        target.health = target.maxHealth
        target.activePotionEffects.stream().filter { effect: PotionEffect ->
            allPotions || NEGATIVE_EFFECTS.contains(
                effect.type
            )
        }.forEach { effect: PotionEffect ->
            target.removePotionEffect(
                effect.type
            )
        }
        target.fireTicks = 0
        if (sender != target) {
            sender.sendMessage(target.displayName + ChatColor.GOLD + " has been healed.")
        }
        target.sendMessage(ChatColor.GOLD.toString() + "You have been healed.")
    }

}