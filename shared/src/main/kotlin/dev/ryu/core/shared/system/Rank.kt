package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.shared.Shared
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 24/2/2024 - 02:41
*/

class Rank(
    @SerializedName("_id") val id: String
) {

    var display: String = id
    var prefix: String? = null
    var color: String = "GRAY"
    var websiteColor: String? = null

    var weight: Int = 1
    var discordId: String? = null
    var price: Int = 0

    var staff: Boolean = false
    var media: Boolean = false
    var hidden: Boolean = false
    var default: Boolean = false
    var grantable: Boolean = true
    var donator: Boolean = false

    var createdAt: Long? = null

    var inherits: MutableList<String> = mutableListOf()
    var permissions: MutableList<String> = mutableListOf()

    fun save() {
        Shared.rankManager.repository.update(this)
    }

    fun isPurchasable() : Boolean {
        return this.price != 0
    }

    fun isStaff() : Boolean {
        return this.staff
    }

    fun isMedia() : Boolean {
        return this.media
    }

    fun isHidden() : Boolean {
        return this.hidden
    }

    fun isDefault() : Boolean {
        return this.default
    }

    fun isGrantable() : Boolean {
        return this.grantable
    }

    fun isDonator() : Boolean {
        return this.donator
    }

    fun getCreatedAt() : String {
        return TimeUtil.formatIntoCalendarString(Date(this.createdAt!!))
    }

    companion object {

        const val RANK_CREATED = "RANK_CREATED"
        const val RANK_DELETED = "RANK_DELETED"
        const val RANK_UPDATED = "RANK_UPDATED"

    }

}