package dev.ryu.core.bukkit.menu.punishment.history

import dev.ryu.core.bukkit.menu.punishment.history.element.PunishmentElement
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.impl.GlassButton
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import org.bukkit.ChatColor
import org.bukkit.entity.Player

class PunishmentMenu(private val profile: Profile, private val punishments: Set<Punishment>) : Menu() {

    override fun size(player: Player): Int {
        return 5*9
    }

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}${this.profile.name}'s History${ChatColor.GRAY}"
    }

    override fun getButtons(player: Player): MutableMap<Int, Button> {
        return hashMapOf<Int, Button>().also { toReturn ->
            val types = Punishment.Type.entries.filter{player.hasPermission(it.permission(false))}.sortedBy{it.ordinal}.reversed()

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            types.forEach{ type ->
                when (type) {
                    Punishment.Type.KICK -> {
                        toReturn[23] = PunishmentElement(type, this.punishments, this.profile)
                    }
                    Punishment.Type.WARN -> {
                        toReturn[21] = PunishmentElement(type, this.punishments, this.profile)
                    }
                    Punishment.Type.MUTE -> {
                        toReturn[22] = PunishmentElement(type, this.punishments, this.profile)
                    }
                    Punishment.Type.BAN -> {
                        toReturn[19] = PunishmentElement(type, this.punishments, this.profile)
                    }
                    Punishment.Type.BLACKLIST -> {
                        toReturn[25] = PunishmentElement(type, this.punishments, this.profile)
                    }
                }
            }

        }
    }

}