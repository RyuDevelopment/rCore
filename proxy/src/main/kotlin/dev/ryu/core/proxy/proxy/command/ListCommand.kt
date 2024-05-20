package dev.ryu.core.proxy.proxy.command

import dev.ryu.core.proxy.Core
import dev.ryu.core.shared.system.module.NetworkModule
import dev.ryu.core.shared.system.module.ProfileModule
import net.md_5.bungee.Util
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class ListCommand(private val instance: Core) : Command("glist","bungeecord.command.list") {

    override fun execute(sender: CommandSender,args: Array<out String>) {

        var players = 0

        for (server in NetworkModule.servers.sortedBy{it.group}.sortedBy{it.id}) {

            players += server.onlinePlayers.size

            sender.sendMessage(TextComponent("${ChatColor.GREEN}[${server.id}] ${ChatColor.YELLOW}(${server.onlinePlayers.size}): ${ChatColor.WHITE}${Util.format(server.onlinePlayers.map{ ProfileModule.findName(it)}.toList(),"${ChatColor.WHITE}, ")}"))
        }

        sender.sendMessage(TextComponent("${ChatColor.WHITE}Total players online: $players"))
    }

}