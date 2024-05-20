package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class Punishment(@SerializedName("_id")val id: UUID, val type: Type, val victim: UUID, val sender: UUID) {

    var created = System.currentTimeMillis()

    var server: String = "Unknown"
    var duration: Long = 0L

    var reason: String = ""
    var pardonReason: String? = null

    var silent: Boolean = true
    var pardonedSilent: Boolean = true

    var pardoned: Long? = null
    var pardoner: UUID? = null

    var identifiers: MutableList<String> = mutableListOf()

    fun isIP():Boolean {
        return this.identifiers.isNotEmpty()
    }

    fun isVoided(): Boolean {
        return this.duration != 0L && (this.created + this.duration) - System.currentTimeMillis() <= 0L
    }

    fun isPardoned():Boolean {
        return this.pardoned != null && this.pardonReason != null && this.pardoner != null
    }

    fun isPermanent():Boolean {
        return this.duration == 0L
    }

    fun getExpiredAt(): Long {
        return this.created + this.duration
    }

    fun getRemaining(): Long {
        return (this.created + this.duration) - System.currentTimeMillis()
    }

    enum class Type(val kickOnExecute: Boolean,val context: String,val color: String,val woolData: Int) {

        KICK(true,"kicked","§a",5),
        WARN(false,"warned","§e",4),
        MUTE(false,"muted","§6",1),
        BAN(true,"banned","§c",14),
        BLACKLIST(true,"blacklisted","§4",0);

        fun permission(pardon: Boolean,ip: Boolean = false): String {
            return "core.${if (pardon) "pardon" else "punishment"}.${if (ip) "ip" else ""}${this.name.toLowerCase()}"
        }

    }

    companion object {

        const val PACKET_ID = "PUNISHMENT"

    }

}