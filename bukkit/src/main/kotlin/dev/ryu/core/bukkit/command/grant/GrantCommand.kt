package dev.ryu.core.bukkit.command.grant

import dev.ryu.core.bukkit.menu.grant.apply.GrantMenu
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import com.starlight.nexus.util.time.Duration
import com.starlight.nexus.util.time.TimeUtil
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.Color
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 21/2/2024 - 23:28
*/

object GrantCommand {

    @Command(names = ["grant"], description = "View all granted ranks of player", permission = "core.grant")
    @JvmStatic
    fun grants(
        sender: CommandSender,
        @Param(name = "profile") profile: Profile
    ) {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        GrantMenu(profile).openMenu(sender)
        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }

    @Command(names = ["ogrant"], description = "Set grant to player", permission = "core.grant")
    @JvmStatic
    fun oGrant(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "rank") rank: Rank,
        @Param(name = "duration") duration: Duration,
        @Param(name = "reason") reason: String
    ){

        if (sender !is Player) {
            GrantModule.grant(rank,target.id, UUID.fromString(Profile.CONSOLE_UUID),reason,duration.get())
        } else {
            val profile = ProfileModule.findById(sender.uniqueId)!!

            if (GrantModule.repository.findAllByPlayer(target.id).any { it.getRank() == rank && !it.isRemoved() }) {
                sender.sendMessage("${ChatColor.RED}Grant cannot be given as the player already possesses it.")
                return
            }

            GrantModule.grant(rank,target.id,profile.id,reason,duration.get())
        }

        sender.sendMessage("${ChatColor.GREEN}Granted ${ChatColor.WHITE}${target.name} ${Color.color(rank.display)}${ChatColor.GREEN} rank${if (duration.get() == 0L) "" else " for ${TimeUtil.formatIntoDetailedString(duration.get())}"}.")

    }

}