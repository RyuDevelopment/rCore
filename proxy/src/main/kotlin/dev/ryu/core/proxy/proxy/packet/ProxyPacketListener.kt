package dev.ryu.core.proxy.proxy.packet

import com.google.gson.JsonObject
import dev.ryu.core.proxy.Core
import dev.ryu.core.proxy.proxy.ProxyHandler
import dev.ryu.core.shared.system.Proxy
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.TextComponent
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class ProxyPacketListener(private val instance: Core) : OrbitListener {

    @Orbit(ProxyHandler.SEND_PACKET)
    fun onSend(data: JsonObject) {

        if (this.instance.proxy.name == data["proxy"].asString) {
            return
        }

        val server = this.instance.proxy.getServerInfo(data["server"].asString) ?: return

        val players = dev.ryu.core.shared.Shared.getGson().fromJson<List<String>>(data["players"].asJsonPrimitive.asString,List::class.java).map{UUID.fromString(it)}.toList()

        players.map{this.instance.proxy.getPlayer(it)}.filter{it != null && it.server != null && it.server.info != server}.forEach{
            it.connect(server)
            it.sendMessage(TextComponent("${ChatColor.GOLD}You have been sent to ${ChatColor.WHITE}${server.name}${ChatColor.GOLD} by ${data["displayName"].asString}${ChatColor.GOLD}."))
        }

    }

    @Orbit(ProxyHandler.ALERT_PACKET)
    fun onAlert(data: JsonObject) {

        if (!data.has("message")) {
            return
        }

        this.instance.proxy.broadcast(*TextComponent.fromLegacyText(data["message"].asString))
    }

    @Orbit(Proxy.UPDATE_ID)
    fun onUpdate(data: JsonObject) {

        val proxy = this.instance.proxyHandler.proxy

        if (proxy.id != data["_id"].asString) {
            return
        }

        proxy.motd = data["motd"].asJsonArray.map{it.asString}.toTypedArray()
        proxy.group = data["group"].asString
    }
}
