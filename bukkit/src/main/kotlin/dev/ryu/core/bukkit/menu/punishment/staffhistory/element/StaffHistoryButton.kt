package dev.ryu.core.bukkit.menu.punishment.staffhistory.element

import com.starlight.nexus.menu.Menu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.prompt.punishment.PunishmentRemoveReasonPrompt
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.ProfileModule
import org.bukkit.ChatColor
import org.bukkit.DyeColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.util.*

class StaffHistoryButton(private val punishment: Punishment, private val profile: Profile, private val parent: Menu) : Button() {

    private val voided = this.punishment.isVoided()
    private val pardoned = this.punishment.isPardoned()

    private var canPardon = false

    override fun getName(player: Player): String {
        this.canPardon = !this.pardoned && !this.voided && this.punishment.type != Punishment.Type.KICK && this.punishment.type != Punishment.Type.WARN && player.hasPermission(this.punishment.type.permission(true))

        return "${ChatColor.GOLD}${TimeUtil.formatIntoCalendarString(Date(this.punishment.created))} ${if (this.punishment.isIP()) "${ChatColor.RED}[IP]" else ""}"
    }

    override fun getMaterial(player: Player): Material {
        return if (this.punishment.type == Punishment.Type.KICK || this.punishment.type == Punishment.Type.WARN) Material.PAPER else Material.WOOL
    }

    override fun getDescription(player: Player): MutableList<String> {

        val lore = ArrayList<String>()

        lore.add("")

        var name = ProfileModule.findById(this.punishment.victim)!!.name

        if (name == "null") {
            name = this.punishment.victim.toString()
        }

        lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Victim${ChatColor.GRAY}: ${ChatColor.WHITE}${name}")
        lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Server${ChatColor.GRAY}: ${ChatColor.WHITE}${this.punishment.server}")
        lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reason${ChatColor.GRAY}: ${ChatColor.WHITE}${this.punishment.reason}")

        if (!this.punishment.isPermanent()) {
            lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Duration${ChatColor.GRAY}: ${ChatColor.WHITE}${TimeUtil.formatIntoDetailedString(this.punishment.duration)}")
        }

        when {
            this.pardoned -> {
                lore.add("")
                lore.add("${ChatColor.RED}${ChatColor.BOLD}Pardoned")
                lore.add(" ")

                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}By${ChatColor.GRAY}: ${ChatColor.WHITE}${ if (this.punishment.pardoner == UUID.fromString(Profile.CONSOLE_UUID)) "${ChatColor.DARK_RED}${ChatColor.BOLD}Console" else ProfileModule.findById(this.punishment.pardoner!!)!!.name}")
                lore.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reason${ChatColor.GRAY}: ${ChatColor.WHITE}${this.punishment.pardonReason}")
                lore.add(" ")
                lore.add("${ChatColor.GOLD}${TimeUtil.formatIntoCalendarString(Date(this.punishment.pardoned!!))}")
            }
            this.voided -> {
                lore.add("")
                lore.add("${ChatColor.RED}${ChatColor.BOLD}Expired")
                lore.add(" ")
                lore.add("${ChatColor.GOLD}${TimeUtil.formatIntoCalendarString(Date(this.punishment.getExpiredAt()))}")
            }
        }

        if (this.canPardon) {
            lore.add("")
            lore.add("${ChatColor.GRAY}-> ${ChatColor.RED}Click to pardon.")
        }

        lore.add("")

        return lore
    }

    override fun getDamageValue(player: Player): Byte {

        var dyeColor = DyeColor.LIME

        if (this.punishment.type == Punishment.Type.WARN || this.punishment.type == Punishment.Type.KICK) {
            return 0
        }

        when {
            this.voided -> dyeColor = DyeColor.ORANGE
            this.pardoned -> dyeColor = DyeColor.RED
        }

        return dyeColor.woolData
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        if (!this.canPardon) {
            return
        }

        player.closeInventory()
        player.beginConversation(ConversationFactory(Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
            PunishmentRemoveReasonPrompt(this.punishment,this.profile)
        ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))

    }

}