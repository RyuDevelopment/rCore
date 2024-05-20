package dev.ryu.core.bukkit.system.webhook.impl

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.system.webhook.WebHookManager
import dev.ryu.core.bukkit.system.webhook.data.IWebHook
import dev.t4yrn.jupiter.orbit.Orbit
import java.awt.Color

class ServersLogWebhook(
    data: JsonObject
): IWebHook {

    private val serverName: String = data["serverName"].asString
    private val serverGroup: String = data["serverGroup"].asString
    private val startedAt: String = data["startedAt"].asString
    private val serverStatus: String = data["serverStatus"].asString

    override fun title(): String {
        return "$serverName is now $serverStatus!"
    }

    override fun description(): MutableList<String> {
        return mutableListOf(
            "**$serverName** server has been started",
            "",
            "Server Information:",
            "[!] Name: **$serverName**",
            "[!] Group: **$serverGroup**",
            "[!] Started At: **$startedAt**",
        )
    }

    override fun image(): String {
        return "https://i.imgur.com/qgyFDXD.gif"
    }

    override fun thumbnail(): String {
        return "http://cravatar.eu/head/CONSOLE/128.png"
    }

    override fun color(): Color {
        return Color.GREEN
    }

    override fun url(): String {
        return Core.get().config.getString("discord_webhook.url.servers")
    }

    fun onServerStart(data: JsonObject) {
        val serverName = data["serverName"].asString
        val serverGroup = data["serverGroup"].asString
        val startedAt = data["startedAt"].asString
        val serverStatus = data["serverStatus"].asString

        val hookInfo = object : IWebHook {
            override fun title(): String {
                return "$serverName is now $serverStatus!"
            }

            override fun description(): MutableList<String> {
                return mutableListOf(
                    "**$serverName** server has been started",
                    "",
                    "Server Information:",
                    "[!] Name: **$serverName**",
                    "[!] Group: **$serverGroup**",
                    "[!] Started At: **$startedAt**",
                )
            }

            override fun image(): String {
                return "https://i.imgur.com/qgyFDXD.gif"
            }

            override fun thumbnail(): String {
                return "http://cravatar.eu/head/CONSOLE/128.png"
            }

            override fun color(): Color {
                return Color.GREEN
            }

            override fun url(): String {
                return Core.get().config.getString("discord_webhook.url.servers")
            }
        }

        val hook = WebHookManager.createWebhook(hookInfo)
        WebHookManager.send(hook, hookInfo)
    }

    fun onServerStop(data: JsonObject) {
        val serverName = data["serverName"].asString
        val serverGroup = data["serverGroup"].asString
        val stoppedAt = data["stoppedAt"].asString
        val serverStatus = data["serverStatus"].asString

        val hookInfo = object : IWebHook {
            override fun title(): String {
                return "$serverName is now $serverStatus!"
            }

            override fun description(): MutableList<String> {
                return mutableListOf(
                    "**$serverName** server has been stopped",
                    "",
                    "Server Information:",
                    "[!] Name: **$serverName**",
                    "[!] Group: **$serverGroup**",
                    "[!] Stopped At: **$stoppedAt**",
                )
            }

            override fun image(): String {
                return "https://i.imgur.com/m3060Rn.gif"
            }

            override fun thumbnail(): String {
                return "http://cravatar.eu/head/CONSOLE/128.png"
            }

            override fun color(): Color {
                return Color.RED
            }

            override fun url(): String {
                return Core.get().config.getString("discord_webhook.url.servers")
            }
        }

        val hook = WebHookManager.createWebhook(hookInfo)
        WebHookManager.send(hook, hookInfo)
    }

}