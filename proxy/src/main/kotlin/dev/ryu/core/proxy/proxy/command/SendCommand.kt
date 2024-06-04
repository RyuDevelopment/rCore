package dev.ryu.core.proxy.proxy.command

import com.google.common.collect.ImmutableSet
import com.google.gson.JsonObject
import dev.ryu.core.proxy.Core
import dev.ryu.core.proxy.proxy.ProxyHandler
import dev.ryu.core.shared.system.module.NetworkModule
import dev.ryu.core.shared.system.module.ProfileModule
import dev.t4yrn.jupiter.Jupiter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command
import net.md_5.bungee.api.plugin.TabExecutor
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class SendCommand(private val instance: Core) : Command("send","bungeecord.command.send"),TabExecutor {

    override fun execute(sender: CommandSender,args: Array<out String>) {

        if (args.size < 2) {
            sender.sendMessage(TextComponent("${ChatColor.RED}Usage: /send <server|group|player|all> <server>"))
            return
        }

        val server = this.instance.proxy.getServerInfo(args[1])

        if (server == null) {
            sender.sendMessage(TextComponent("${ChatColor.RED}Server ${ChatColor.YELLOW}${args[1]}${ChatColor.RED} not found."))
            return
        }

        var players: List<UUID>? = this.findTargets(sender,args[0]) ?: return

        players = players as ArrayList<UUID>

        val sent = AtomicInteger()

        val displayName = sender.name

        players.removeIf{

            val player = this.instance.proxy.getPlayer(it)

            if (player != null && player.server != null && player.server.info != server) {

                player.connect(server)
                player.sendMessage(TextComponent("${ChatColor.GOLD}You have been sent to ${ChatColor.WHITE}${server.name}${ChatColor.GOLD} by $displayName${ChatColor.GOLD}."))

                sent.incrementAndGet()
            }

            return@removeIf player != null
        }

        if (players.isNotEmpty()) {

            this.instance.proxy.scheduler.runAsync(this.instance) {

                val jsonObject = JsonObject()

                jsonObject.addProperty("proxy",this.instance.proxy.name)
                jsonObject.addProperty("server",server.name)
                jsonObject.addProperty("players", dev.ryu.core.shared.Shared.getGson().toJson(players))
                jsonObject.addProperty("displayName",displayName)

                dev.ryu.core.shared.Shared.backendManager.getJupiter().sendPacket(Jupiter(ProxyHandler.SEND_PACKET,jsonObject))
            }

        }

        sender.sendMessage(TextComponent("${ChatColor.GOLD}Sent ${ChatColor.WHITE}${sent.get()} ${ChatColor.GOLD}player${if (players.size == 1) "" else "s"} ${if (players.isNotEmpty()) "on current proxy " else ""}to ${ChatColor.WHITE}${server.name}${ChatColor.GOLD}${if (players.isEmpty()) "." else ", attempting to send ${ChatColor.WHITE}${players.size}${ChatColor.GOLD} player${if (players.size == 1) "" else "s"} on different proxies."}"))
    }

    private fun findTargets(sender: CommandSender,argument: String):List<UUID>? {

        if (argument.equals("all",true)) {

            val toReturn = NetworkModule.servers.flatMap{it.onlinePlayers}.toList()

            if (toReturn.isEmpty()) {
                sender.sendMessage(TextComponent("${ChatColor.RED}There are no players on the network."))
                return null
            }

            return toReturn
        }

        val server = NetworkModule.findServerById(argument)

        if (server != null) {

            if (server.onlinePlayers.isEmpty()) {
                sender.sendMessage(TextComponent("${ChatColor.RED}There are no players on ${ChatColor.WHITE}${server.id}${ChatColor.RED}."))
                return null
            }

            return server.onlinePlayers.toList()
        }

        val group =  NetworkModule.findGroupById(argument)

        if (group != null) {
            return group.findServers().flatMap {it.onlinePlayers }
        }

        val uuid = ProfileModule.findId(argument)

        if (uuid == null || !NetworkModule.isOnline(uuid)) {
            sender.sendMessage(TextComponent("${ChatColor.RED}${argument} is currently not on the network."))
            return null
        }

        return arrayListOf(uuid)
    }

    override fun onTabComplete(sender: CommandSender,args: Array<out String>): MutableIterable<String> {

        if (args.size > 2 || args.isEmpty()) {
            return ImmutableSet.of()
        }

        val toReturn = HashSet<String>()

        if (args.size == 1) {

            val search = args[0].toLowerCase(Locale.ROOT)

            this.instance.proxy.players.filter{it.name.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT))}.forEach{toReturn.add(it.name)}

            if (search.startsWith("all",true)) {
                toReturn.add("all")
            }
            if (search.startsWith("current",true)) {
                toReturn.add("current")
            }

            return toReturn
        }

        return this.instance.proxy.servers.keys.filter{it.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT))}.toMutableList()
    }

}