package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.module.PunishmentModule
import dev.ryu.core.shared.system.module.ReportModule
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 09:43
*/

class Profile(
    @SerializedName("_id")val id: UUID
){

    var name: String? = null
    var customNick: String? = null
    var chatColor: String = "WHITE"

    var currentServer: String? = null
    var lastServer: String? = null

    var address: String? = null
    var addresses: MutableList<String> = mutableListOf()

    var email: String? = null
    var coins: Int = 0
    var tokens: Int = 0
    var discordId: String? =null

    var firstJoined: Long = System.currentTimeMillis()
    var lastJoined: Long? = null
    var reportCooldown: Long = 0L

    var isSuperUser: Boolean = false
    var online: Boolean = false

    var tag: String? = null

    var friends: MutableList<UUID> = mutableListOf()
    var requests: MutableList<UUID> = mutableListOf()
    var permissions: MutableList<String> = mutableListOf()

    fun save() {
        Shared.profileManager.repository.update(this)
    }

    fun setSuperUser(value: Boolean) : Boolean {
        return value.also { this.isSuperUser = it }
    }

    fun getFirstJoined() : String {
        return TimeUtil.formatIntoCalendarString(Date(this.firstJoined))
    }

    fun getLastJoined() : String {
        return TimeUtil.formatIntoCalendarString(Date(this.lastJoined!!))
    }

    fun isMuted(uuid: UUID): Boolean {
        return PunishmentModule.mutes[uuid]?.any{it.type == Punishment.Type.MUTE && it.getRemaining() > 0} ?: false
    }

    fun isBlacklisted(): Boolean {
        val punishments = PunishmentModule.repository.findByVictimOrIdentifier(this.id,this.addresses)

        if (punishments.isNotEmpty()) {

            val punishment = PunishmentModule.repository.findMostRecentPunishment(punishments,Punishment.Type.BLACKLIST)

            if (punishment != null) {
                return true
            }

        }

        return false
    }

    fun isPunished(): Boolean {
        val punishments = PunishmentModule.repository.findByVictimOrIdentifier(this.id,this.addresses)

        if (punishments.isNotEmpty()) {

            val punishment = PunishmentModule.repository.findMostRecentPunishment(punishments,Punishment.Type.BAN)

            if (punishment != null) {
                return true
            }

        }

        return false
    }

    fun getLastPunishment(): Punishment? {
        val punishments = PunishmentModule.repository.findByVictimOrIdentifier(this.id,this.addresses)
        if (punishments.isNotEmpty()) {

            val punishment = PunishmentModule.repository.findMostRecentPunishment(punishments,arrayListOf(Punishment.Type.BLACKLIST,Punishment.Type.BAN))

            if (punishment != null) {
                return punishment
            }

        }
        return null
    }

    fun getTotalReports(): Int {
        val reports = ReportModule.repository.findAllReportsById(this.id)
        return reports.size
    }

    companion object {

        const val CONSOLE_UUID = "00000000-0000-0000-0000-000000000000"
        const val PROFILE_NAME_UPDATE = "PROFILE_NAME_UPDATE"
        const val PERMISSION_UPDATE_PACKET = "PERMISSION_UPDATE"

    }

}