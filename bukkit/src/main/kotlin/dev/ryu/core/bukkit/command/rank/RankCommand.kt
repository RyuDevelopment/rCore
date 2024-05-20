package dev.ryu.core.bukkit.command.rank

import dev.ryu.core.bukkit.menu.rank.RankCommand
import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.shared.system.Rank
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.UnicodeUtil
import dev.t4yrn.jupiter.Jupiter
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 13:00
*/

object RankCommand {

    @Command(names = ["ranks"], description = "Open ranks menu", permission = "core.rank")
    @JvmStatic
    fun rank(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            return
        }

        RankCommand().openMenu(sender)

        sender.sendMessage("")
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "To get the complete list of commands use '" + ChatColor.GOLD + "/rank" + ChatColor.GRAY + "'.")
        sender.sendMessage("")

        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

    @Command(names = ["rank create"], description = "Create's a new rank", permission = "core.rank")
    @JvmStatic
    fun create(
        sender: CommandSender,
        @Param("rank") name: String
    ) {
        val rank = dev.ryu.core.shared.CoreAPI.rankManager.findById(name)

        if (rank != null) {
            sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_ALREADY_EXISTS_ERROR.value.replace("{rank}", name))
            return
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CREATION_COMMAND.value.replace("{rank}", name))

        val newRank = Rank(name)
        newRank.createdAt = System.currentTimeMillis()
        newRank.display = "&7${newRank.id}"

        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(newRank)
        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().sendPacket(Jupiter(Rank.RANK_CREATED, newRank))

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank delete"], description = "Delete's an existing rank", permission = "core.rank")
    @JvmStatic
    fun delete(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_DELETE_COMMAND.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}"))

        dev.ryu.core.shared.CoreAPI.rankManager.repository.delete(rank)
        dev.ryu.core.shared.CoreAPI.rankManager.cache.remove(rank.id)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank information"], description = "Get a rank's information", permission = "core.rank")
    @JvmStatic
    fun information(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank
    ) {
        val information = listOf(
            "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 35)}",
            "&6&lRank Information &7${UnicodeUtil.VERTICAL_LINE} ${rank.display}",
            "",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eName&7: &f${rank.id}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eDisplay&7: &f${rank.display}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eColor&7: &f${ChatColor.valueOf(rank.color)}${Color.convert(rank.color)}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &ePrefix&7: &f${if (Objects.equals(rank.prefix, null)) "&cNot Set" else rank.prefix}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eWeight&7: &f${rank.weight}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eStaff&7: &f${if (rank.staff) "&aTrue" else "&cFalse"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eHidden&7: &f${if (rank.hidden) "&aTrue" else "&cFalse"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eDiscordID&7: &f${if (Objects.equals(rank.prefix, null)) "&cNot set" else "&f${rank.discordId}"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eInherits&7: &f${if (rank.inherits.isEmpty()) "&cNone" else "&7(${rank.inherits.size}) &f[${rank.inherits.joinToString(", ")}]"}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &ePermissions&7: &f${if (rank.permissions.isEmpty()) "&cNone" else "&7(${rank.permissions.size}) &f[${rank.permissions.joinToString(", ")}]"}",
            "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 35)}"
        )

        information.forEach {
            sender.sendMessage(Color.color(it))
        }
    }

    @Command(names = ["rank display"], description = "Set's a rank's display name", permission = "core.rank")
    @JvmStatic
    fun display(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "display") display: String
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_DISPLAY.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{display}", Color.color(display)))

        rank.display = display
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank prefix"], description = "Set's a rank's prefix", permission = "core.rank")
    @JvmStatic
    fun prefix(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "prefix") prefix: String
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_PREFIX.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{prefix}", Color.color(prefix)))

        rank.prefix = Color.color(prefix)
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank color"], description = "Set's a rank's color", permission = "core.rank")
    @JvmStatic
    fun color(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "color") color: ChatColor
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_COLOR.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{color}", "${ChatColor.valueOf(color.name)}${Color.convert(color.name)}"))

        rank.color = color.name
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank weight"], description = "Set's a rank's weight", permission = "core.rank")
    @JvmStatic
    fun weight(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "weight") weight: Int
    ) {
        if (weight < 0) {
            sender.sendMessage("${ChatColor.RED}Weight cannot be negative")
            return
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_WEIGHT.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{weight}", weight.toString()))

        rank.weight = weight
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank discordId"], description = "Set's a rank's discord id", permission = "core.rank")
    @JvmStatic
    fun discordId(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "discordId") discordId: String
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_DISCORD_ID.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{discordId}", discordId))

        rank.discordId = discordId;
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank staff"], description = "Set's a rank's staff", permission = "core.rank")
    @JvmStatic
    fun staff(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "status") status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_STAFF_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{status}", statusDisplay))

        rank.staff = status
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank media"], description = "Set's a rank's media", permission = "core.rank")
    @JvmStatic
    fun media(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "status") status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_MEDIA_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{status}", statusDisplay))

        rank.media = status
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank hidden"], description = "Set's a rank's hidden", permission = "core.rank")
    @JvmStatic
    fun hidden(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "status") status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_HIDDEN_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{status}", statusDisplay))

        rank.hidden = status
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank default"], description = "Set's a rank's default", permission = "core.rank")
    @JvmStatic
    fun default(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "status") status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_DEFAULT_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{status}", statusDisplay))

        rank.default = status
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank grantable"], description = "Set's a rank's grantable", permission = "core.rank")
    @JvmStatic
    fun grantable(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "status") status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_GRANTABLE_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{status}", statusDisplay))

        rank.grantable = status
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank donator"], description = "Set's a rank's donator", permission = "core.rank")
    @JvmStatic
    fun donator(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "status") status: Boolean
    ) {
        val statusDisplay: String = if (status) {
            "${ChatColor.GREEN}true"
        } else {
            "${ChatColor.RED}false"
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_CHANGED_DONATOR_STATUS.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{status}", statusDisplay))

        rank.donator = status
        dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank inherit"], description = "Set's a rank's inherit", permission = "core.rank")
    @JvmStatic
    fun inherit(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "inherit") inherit: Rank
    ) {
        if (!rank.inherits.contains(inherit.id)) {
            sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_INHERIT_ADDED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{inherit}", "${ChatColor.valueOf(inherit.color)}${inherit.id}"))

            rank.inherits.add(inherit.id)
            dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)
        } else {
            sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_INHERIT_REMOVED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{inherit}", "${ChatColor.valueOf(inherit.color)}${inherit.id}"))

            rank.inherits.remove(inherit.id)
            dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)
        }

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["rank permission"], description = "Set's a rank's permission", permission = "core.rank")
    @JvmStatic
    fun permission(
        sender: CommandSender,
        @Param(name = "rank") rank: Rank,
        @Param(name = "permission") permission: String
    ) {
        if (!rank.permissions.contains(permission)) {
            sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_PERMISSION_ADDED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{permission}", permission))

            rank.permissions.add(permission)
            dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)
        } else {
            sender.sendMessage(Lang.CORE_PREFIX.value + Lang.RANK_PERMISSION_REMOVED.value.replace("{rank}", "${ChatColor.valueOf(rank.color)}${rank.id}").replace("{permission}", permission))

            rank.permissions.remove(permission)
            dev.ryu.core.shared.CoreAPI.rankManager.repository.update(rank)
        }

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

}