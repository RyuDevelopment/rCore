package dev.ryu.core.proxy.proxy.command

import com.google.gson.JsonObject
import dev.ryu.core.proxy.Core
import dev.ryu.core.proxy.proxy.ProxyHandler
import dev.ryu.core.shared.CoreAPI
import dev.t4yrn.jupiter.Jupiter
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class AlertCommand(private val instance: Core) : Command("alert","bungeecord.command.alert") {

    override fun execute(sender: CommandSender,args: Array<String>) {

        if (args.isEmpty()) {
            sender.sendMessage(TextComponent("${ChatColor.RED}Usage: /alert <message>"))
            return
        }

        val builder = StringBuilder()

        if (args[0].startsWith("&h")) {
            args[0] = args[0].substring(2).replace(" ","")
        } else {
            builder.append("${ChatColor.DARK_GRAY}[${ChatColor.DARK_RED}Alert${ChatColor.DARK_GRAY}]${ChatColor.WHITE} ")
        }

        args.forEach{
            builder.append(ChatColor.translateAlternateColorCodes('&',it))
            builder.append(" ")
        }

        this.instance.proxy.scheduler.runAsync(this.instance) {

            val jsonObject = JsonObject()

            jsonObject.addProperty("message",builder.substring(0,builder.length-1))

            dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().sendPacket(Jupiter(ProxyHandler.ALERT_PACKET,jsonObject))
        }

    }


}