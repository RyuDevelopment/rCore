package dev.ryu.core.bukkit.menu.punishment.staffhistory

import dev.ryu.core.bukkit.menu.punishment.staffhistory.element.StaffPunishmentButton
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.impl.GlassButton
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player

class StaffPunishmentMenu(private val profile: Profile, private val punishments: Set<Punishment>) : Menu() {

    override fun size(player: Player): Int {
        return 6*9
    }

    override fun getTitle(player: Player): String {
        return "${ChatColor.GRAY}${this.profile.name!!}"
    }

    override fun getButtons(player: Player): MutableMap<Int,Button> {
        return hashMapOf<Int, Button>().also { toReturn ->

            for (i in 0..8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 5)] = GlassButton(7)
            }

            for (i in 1 .. 4) {
                toReturn[getSlot(0, i)] = GlassButton(7)
                toReturn[getSlot(8, i)] = GlassButton(7)
            }

            val types = Punishment.Type.entries.filter { player.hasPermission(it.permission(false)) }.sortedBy { it.ordinal }.reversed()

            types.forEach{ type ->
                when (type) {
                    Punishment.Type.KICK -> {
                        toReturn[24] = StaffPunishmentButton(type, this.punishments, this.profile)
                    }
                    Punishment.Type.WARN -> {
                        toReturn[22] = StaffPunishmentButton(type, this.punishments, this.profile)
                    }
                    Punishment.Type.MUTE -> {
                        toReturn[23] = StaffPunishmentButton(type, this.punishments, this.profile)
                    }
                    Punishment.Type.BAN -> {
                        toReturn[20] = StaffPunishmentButton(type, this.punishments, this.profile)
                    }
                    Punishment.Type.BLACKLIST -> {
                        toReturn[29] = StaffPunishmentButton(type, this.punishments, this.profile)
                    }
                }
            }

            val removeTypes = RemoveType.entries.sortedBy { it.ordinal }.reversed()

            val removeStart = 33 - (removeTypes.size - 1)

            removeTypes.filter { player.hasPermission(it.parent[0].permission(false)) }.forEachIndexed { index, type -> toReturn[removeStart + index] = StaffPunishmentButton(type, this.punishments, this.profile) }

        }
    }

    enum class RemoveType(val parent: Array<Punishment.Type>, val material: Material) {

        UN_MUTE(arrayOf(Punishment.Type.MUTE),Material.IRON_SWORD),
        UN_BAN(arrayOf(Punishment.Type.BAN),Material.GOLD_SWORD),
        UN_BLACKLIST(arrayOf(Punishment.Type.BLACKLIST),Material.DIAMOND_SWORD)

    }

}