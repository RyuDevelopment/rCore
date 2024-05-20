package dev.ryu.core.shared.system

import com.google.gson.annotations.SerializedName
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 20:39
*/

class Clan(
    @SerializedName("_id") var id: String,
    var leaderId: UUID
){

    var abbreviation: String? = null
    var coLeader: String? = null
    var members: MutableList<UUID> = mutableListOf()
    var coins: Int = 0
    var maxMembers: Int = 40

    var createdAt = System.currentTimeMillis()

    var armorPurchased: Boolean = false
    var helmetColor: String? = null
    var chestPlateColor: String? = null
    var leggingsColor: String? = null
    var bootsColor: String? = null

    //TODO:Annihilation stats

}