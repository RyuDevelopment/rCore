package dev.ryu.core.bukkit.system.webhook.impl

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.DiscordWebhook
import java.awt.Color

object CodesLogWebhook {

    fun onCodeCreated(data: JsonObject) {
        val rank = data["rank"].asString
        val duration = data["duration"].asString
        val reason = data["reason"].asString
        val generatedBy = data["generatedBy"].asString
        val generatedAt = data["generatedAt"].asString
        val uuidSender = data["senderUUID"].asString
        val code = data["code"].asString

        val codesHook =
            DiscordWebhook(Core.get().config.getString("discord_webhook.url.codes"))

        val embed = DiscordWebhook.EmbedObject()
        embed.setTitle("A new redeemable code has been created!")
        embed.setImage("https://i.imgur.com/33Ta72R.gif")//GENERATED

        val headURLImage = "http://cravatar.eu/head/$generatedBy/128.png"

        embed.setThumbnail(headURLImage)
        embed.color = Color.CYAN

        val desc = listOf(
            "**$generatedBy** has generated a new redeemable code!",
            "",
            "Code Information:",
            "[!] Code: **$code**",
            "[!] Rank: **$rank**",
            "[!] Duration: **$duration**",
            "[!] Reason: **$reason**",
            "[!] Generated At: **$generatedAt**",
            "",
            "Sender Information:",
            "[!] Name: **$generatedBy**",
            "[!] UUID: `$uuidSender`",
        )

        embed.setDescription(codesHook.buildDescription(desc))
        embed.setFooter("Developed by le4ndev", "https://i.imgur.com/CmQPepN.png")

        codesHook.addEmbed(embed)

        try {
            codesHook.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onCodeDeleted(data: JsonObject) {
        val rank = data["rank"].asString
        val deletedBy = data["deletedBy"].asString
        val deletedAt = data["deletedAt"].asString
        val uuidSender = data["senderUUID"].asString
        val code = data["code"].asString

        val codesHook = DiscordWebhook(Core.get().config.getString("discord_webhook.url.codes"))

        val embed = DiscordWebhook.EmbedObject()
        embed.setTitle("A redeemable code has been deleted!")
        embed.setImage("https://i.imgur.com/1D08WAO.gif")//DELETED

        val headURLImage = "http://cravatar.eu/head/$deletedBy/128.png"

        embed.setThumbnail(headURLImage)
        embed.color = Color.CYAN

        val desc = listOf(
            "**$deletedBy** has deleted a redeemable code!",
            "",
            "Code Information:",
            "[!] Code: **$code**",
            "[!] Rank: **$rank**",
            "[!] Deleted At: **$deletedAt**",
            "",
            "Sender Information:",
            "[!] Name: **$deletedBy**",
            "[!] UUID: `$uuidSender`",
        )

        embed.setDescription(codesHook.buildDescription(desc))
        embed.setFooter("Developed by le4ndev", "https://i.imgur.com/CmQPepN.png")

        codesHook.addEmbed(embed)

        try {
            codesHook.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onCodeClaimed(data: JsonObject) {
        val rank = data["rank"].asString
        val code = data["code"].asString
        val redeemer = data["redeemer"].asString
        val redeemedAt = data["redeemedAt"].asString
        val redeemerUUID = data["redeemerUUID"].asString

        val codesHook =
            DiscordWebhook(Core.get().config.getString("discord_webhook.url.codes"))

        val embed = DiscordWebhook.EmbedObject()
        embed.setTitle("A redeemable code has been claimed!")
        embed.setImage("https://i.imgur.com/oZQNrBF.gif")//CLAIMED

        val headURLImage = "http://cravatar.eu/head/$redeemer/128.png"

        embed.setThumbnail(headURLImage)
        embed.color = Color.CYAN

        val desc = listOf(
            "**$redeemer** has claimed a redeemable code!",
            "",
            "Code Information:",
            "[!] Code: **$code**",
            "[!] Rank: **$rank**",
            "[!] Deleted At: **$redeemedAt**",
            "",
            "Sender Information:",
            "[!] Name: **$redeemer**",
            "[!] UUID: `$redeemerUUID`",
        )

        embed.setDescription(codesHook.buildDescription(desc))
        embed.setFooter("Developed by le4ndev", "https://i.imgur.com/CmQPepN.png")

        codesHook.addEmbed(embed)

        try {
            codesHook.execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}