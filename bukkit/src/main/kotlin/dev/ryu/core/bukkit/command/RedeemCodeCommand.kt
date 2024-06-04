package dev.ryu.core.bukkit.command

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.system.webhook.impl.CodesLogWebhook
import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.CodeModule
import dev.ryu.core.shared.system.module.GrantModule
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.time.Duration
import dev.t4yrn.jupiter.Jupiter
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.text.SimpleDateFormat
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 15:17
*/

object RedeemCodeCommand {

    @Command(names = ["redeem", "canjear"], description = "", permission = "")
    @JvmStatic
    fun redeem(
        sender: CommandSender,
        @Param(name = "code") code: Code
    ) {

        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}You must be a player to perform that command.")
            return
        }

        if (code.redeemed) {
            sender.sendMessage("${ChatColor.RED}That code has already been redeemed.")
            return
        }

        if (GrantModule.repository.findAllByPlayer(sender.uniqueId).any { it.getRank() == code.getRank() && !it.isRemoved() }) {
            sender.sendMessage("${ChatColor.RED}Grant cannot be given as the player already possesses it.")
            return
        }

        val rankToGive = dev.ryu.core.shared.Shared.rankManager.findById(code.rank)!!
        val rankDuration = Duration(code.rankDuration)

        code.redeemed = true
        code.redeemBy = sender.uniqueId
        code.redeemAt = System.currentTimeMillis()

        CodeModule.repository.update(code)

        GrantModule.grant(rankToGive, sender.uniqueId, UUID.fromString(Profile.CONSOLE_UUID),"Redeemed by code (${code.code})", rankDuration.get())

        val data = JsonObject()

        data.addProperty("code", code.code)
        data.addProperty("rank", code.rank)
        data.addProperty("redeemer", sender.name)

        val date = Date(System.currentTimeMillis())
        val format = SimpleDateFormat("EEEE MMMM d h:mm a yyyy", Locale.getDefault())
        val finalDate = format.format(date)

        data.addProperty("redeemedAt", finalDate)
        data.addProperty("redeemerUUID", sender.uniqueId.toString())

        if (dev.ryu.core.bukkit.Core.get().webhookEnabled) {
            CodesLogWebhook.onCodeClaimed(data)
        }

        dev.ryu.core.shared.Shared.backendManager.getJupiter().sendPacket(Jupiter(Code.CODE_REDEEMED, data))

    }

    class asd {

    }

}