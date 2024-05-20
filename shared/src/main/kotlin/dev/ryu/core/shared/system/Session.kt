package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import java.util.*

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 3:31 PM
*/

class Session(
    @SerializedName("_id")val id: String,
    @SerializedName("uuid")val uuid: UUID,
    val name: String,
    val joinedAt: Long,
) {
    var leftAt: Long? = null
    var totalPlaytime: Long? = null
}