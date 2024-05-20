package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import java.util.*

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 3:31 PM
*/

class Report(
    @SerializedName("_id") var id: UUID,
    var targetId: UUID,
    var target: String,
    var senderId: UUID,
    var sender: String,
    var server: String,
    var reportedAt: Long,
    var reason: String
)