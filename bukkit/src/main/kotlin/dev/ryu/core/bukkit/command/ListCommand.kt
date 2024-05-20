package dev.ryu.core.bukkit.command

import dev.ryu.core.bukkit.Core
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.RankModule
import com.starlight.nexus.command.Command
import com.starlight.nexus.util.Color
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 18:53
*/

object ListCommand {

    @Command(names = ["list"], description = "See a list of online players.", permission = "")
    @JvmStatic
    fun execute(
        sender: CommandSender
    ) {
        sender.sendMessage(Color.color(StringUtils.join(RankModule.cache.values.filter{this.canSee(sender,it)}.sortedBy{it.weight}.reversed().map{it.display},"${ChatColor.WHITE}, ")))

        val players = dev.ryu.core.bukkit.Core.get().server.onlinePlayers.filter{if (sender is Player) return@filter sender.canSee(it) else return@filter true}.sortedBy{

            val priority = GrantModule.findBestRank(it.uniqueId).weight

            return@sortedBy priority
        }.reversed().map{this.formatName(it, GrantModule.findBestRank(it.uniqueId))}

        sender.sendMessage("${ChatColor.WHITE}(${players.size}/${dev.ryu.core.bukkit.Core.get().server.maxPlayers}) [${StringUtils.join(players,"${ChatColor.WHITE}, ")}${ChatColor.WHITE}]")
    }

    private fun canSee(sender: CommandSender, rank: Rank):Boolean {

        if (sender.isOp || !rank.isHidden()) {
            return true
        }

        return sender.hasPermission("grant.rank.${rank.id}")
    }

    private fun formatName(player: Player, rank: Rank):String {
        return "${ChatColor.valueOf(rank.color)}${player.name}"
    }

}