package dev.ryu.core.bukkit.system.webhook

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.DiscordWebhook
import dev.ryu.core.bukkit.system.webhook.data.IWebHook

object WebHookManager{

    val enabled = Core.get().webhookEnabled

    @JvmStatic
    fun createWebhook(webhook: IWebHook): DiscordWebhook {
        return DiscordWebhook(webhook.url())
    }

    @JvmStatic
    fun send(webhook: DiscordWebhook, info: IWebHook) {

        val embed = DiscordWebhook.EmbedObject()

        embed.setTitle(info.title())
        embed.setImage(info.image())
        embed.setThumbnail(info.thumbnail())
        embed.setDescription(webhook.buildDescription(info.description()))
        embed.setFooter("Developed by le4ndev", "https://i.imgur.com/CmQPepN.png")
        embed.color = info.color()

        webhook.addEmbed(embed)

        try {
            webhook.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}