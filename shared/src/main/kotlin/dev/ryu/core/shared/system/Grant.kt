package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 19/2/2024 - 23:16
*/

class Grant(
    @SerializedName("_id")val id: UUID,
    val rank: String,
    val target: UUID,
    val sender: UUID
) {

    var duration: Long = 0L

    var reason: String = ""
    var removedReason: String? = null

    var created: Long = System.currentTimeMillis()
    var removed: Long? = null

    var remover: UUID? = null

    fun isActive(): Boolean {
        return !this.isVoided() && !this.isRemoved()
    }

    fun isVoided(): Boolean {
        return this.duration != 0L && (this.created + this.duration) - System.currentTimeMillis() <= 0L
    }

    fun isRemoved():Boolean {
        return this.removed != null && this.removedReason != null && this.remover != null
    }

    fun getTimeAgo():Long {
        return System.currentTimeMillis() - this.created
    }

    fun getVoidedAt():Long {
        return this.created + this.duration
    }

    fun getRemaining(): Long {
        return (this.created + this.duration) - System.currentTimeMillis()
    }

    fun isPermanent():Boolean {
        return this.duration == 0L
    }

    fun getRank(): Rank? {
        return dev.ryu.core.shared.Shared.rankManager.findById(this.rank)
    }

    fun getPriority():Int {
        return dev.ryu.core.shared.Shared.rankManager.findById(this.rank)?.weight ?: 0
    }

    companion object {

        const val GRANT_REMOVE = "GRANT_REMOVE"
        const val GRANT_EXECUTE = "GRANT_EXECUTE"

    }

}