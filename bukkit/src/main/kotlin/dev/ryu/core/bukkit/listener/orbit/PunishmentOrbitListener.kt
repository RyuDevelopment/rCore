package dev.ryu.core.bukkit.listener.orbit

import com.google.gson.JsonObject
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.ProfileModule
import dev.ryu.core.shared.system.module.PunishmentModule
import com.starlight.nexus.util.time.TimeUtil
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import mkremins.fanciful.FancyMessage
import org.bukkit.ChatColor
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class PunishmentOrbitListener() : OrbitListener {

    @Orbit(Punishment.PACKET_ID)
    fun onPunishment(data: JsonObject) {

        val punishment = dev.ryu.core.shared.Shared.getGson().fromJson(data, Punishment::class.java)

        val pardoned = punishment.isPardoned()

        val silent = if (pardoned) punishment.pardonedSilent else punishment.silent

        val fancyMessage = FancyMessage("${data["victimDisplay"].asString}${ChatColor.GREEN} has been${ChatColor.YELLOW}${if (silent) " silently" else ""}${ChatColor.GREEN} ${if (pardoned) "un" else ""}${punishment.type.context} by ${data["senderDisplay"].asString}${ChatColor.GREEN}.")

        fancyMessage.tooltip(this.tooltip(punishment))

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()) {

            dev.ryu.core.bukkit.Core.get().server.onlinePlayers.filter{

                if (silent) {
                    return@filter it.hasPermission(punishment.type.permission(pardoned))
                }

                return@filter true
            }.forEach{if (it.hasPermission(punishment.type.permission(pardoned))) fancyMessage.send(it) else it.sendMessage(fancyMessage.toOldMessageFormat())}

        }

        fancyMessage.send(dev.ryu.core.bukkit.Core.get().server.consoleSender)

        if (!punishment.type.kickOnExecute) {

            val victim = dev.ryu.core.bukkit.Core.get().server.getPlayer(punishment.victim)

            if (victim != null) {
                val message = if (punishment.isPardoned() && punishment.type == Punishment.Type.MUTE) "${ChatColor.RED}You are no longer silenced." else getPunishmentMessage(punishment)

                if (message != null) {
                    victim.sendMessage(message)
                }

                PunishmentModule.mutes[punishment.victim]!!.removeIf{it.id == punishment.id}
                PunishmentModule.mutes[punishment.victim]!!.add(punishment)
            } else {
                return
            }

            return
        }

        if (punishment.isPardoned()) {
            return
        }

        if (!punishment.isIP()) {

            val victim = dev.ryu.core.bukkit.Core.get().server.getPlayer(punishment.victim)

            if (victim != null) {
                dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()) {victim.kickPlayer(getPunishmentKickMessage(victim.uniqueId,punishment,true))}
            } else {
                return
            }

            return
        }

        val victims = dev.ryu.core.bukkit.Core.get().server.onlinePlayers.filter{it.uniqueId == punishment.victim || punishment.identifiers.contains(it.address.address.hostAddress)}

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()){victims.forEach{it.kickPlayer(getPunishmentKickMessage(it.uniqueId,punishment,true))}}
    }

    private fun tooltip(punishment: Punishment):ArrayList<String> {

        val server = "${ChatColor.YELLOW}Server: ${ChatColor.RED}${punishment.server}"
        val reason = "${ChatColor.YELLOW}Reason: ${ChatColor.RED}${if (punishment.isPardoned()) punishment.pardonReason else punishment.reason}"

        if (punishment.type == Punishment.Type.KICK || punishment.type == Punishment.Type.WARN || punishment.isPardoned()) {
            return arrayListOf(server,reason)
        }

        val duration = "${ChatColor.YELLOW}Duration: ${ChatColor.RED}${if (punishment.isPermanent()) "Forever" else TimeUtil.formatIntoDetailedString(punishment.duration)}"

        return arrayListOf(server,reason,duration)
    }

    companion object {

        fun getPunishmentMessage(punishment: Punishment):String? {

            if (punishment.type == Punishment.Type.WARN) {
                return "${ChatColor.RED}You have been warned: ${ChatColor.YELLOW}${punishment.reason}"
            }

            if (punishment.type == Punishment.Type.MUTE) {
                return "${ChatColor.RED}${if (punishment.isPermanent()) "You have been permanently silenced." else "You have been silenced for ${ChatColor.YELLOW}${TimeUtil.formatIntoDetailedString(punishment.duration)}${ChatColor.RED}."}"
            }

            return null
        }

        fun getPunishmentKickMessage(victim: UUID, punishment: Punishment, online: Boolean):String {

            if (punishment.type == Punishment.Type.KICK){
                return "${ChatColor.RED}You have been kicked: ${ChatColor.YELLOW}${punishment.reason}"
            }

            var toReturn = if (victim == punishment.victim) "Your account has been ${if (punishment.type == Punishment.Type.BLACKLIST) "blacklisted" else "suspended"} from ${dev.ryu.core.bukkit.Core.get().config.getString("server-info.name")}" else "Your account has been ${if (punishment.type == Punishment.Type.BLACKLIST) "blacklisted" else "suspended"} due to a punishment related to ${ProfileModule.findById(punishment.victim)!!.name}"

            if (!punishment.isPermanent()) {
                toReturn += "\n\n Expires: ${ChatColor.YELLOW}${TimeUtil.formatIntoDetailedString(if (online) punishment.duration else punishment.getRemaining())}"
            }

            return "${ChatColor.RED}$toReturn"
        }
        
    }

}