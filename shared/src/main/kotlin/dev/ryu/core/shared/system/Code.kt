package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import com.starlight.nexus.util.time.TimeUtil
import java.sql.Date
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 23/2/2024 - 00:37
*/

class Code(
    @SerializedName("_id") var id: UUID,
    var code: String,
    var rank: String,
    var rankDuration: Long,
    var createdBy: UUID,
    var createdAt: Long,
) {

    var redeemBy: UUID? = null
    var redeemAt: Long? = null
    var redeemed: Boolean = false

    fun getRank(): Rank? {
        return dev.ryu.core.shared.Shared.rankManager.findById(this.rank)
    }

    fun getRankDuration() : String {
        return TimeUtil.formatIntoDetailedString(this.rankDuration)
    }

    fun getCreatedAt() : String {
        return TimeUtil.formatIntoCalendarString(Date(this.createdAt))
    }

    fun getRedeemedAt() : String {
        return TimeUtil.formatIntoCalendarString(Date(this.redeemAt!!))
    }

    fun isRedeemed(): Boolean {
        return this.redeemed
    }

    companion object {

        const val CODE_CREATED = "CODE_CREATED"
        const val CODE_REMOVED = "CODE_REMOVED"
        const val CODE_REDEEMED = "CODE_REDEEMED"

    }

}