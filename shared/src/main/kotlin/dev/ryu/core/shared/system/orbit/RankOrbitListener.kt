package dev.ryu.core.shared.system.orbit

import com.google.gson.JsonObject
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Rank
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener

/*
    * Author: T4yrn
    * Project: core
    * Date: 24/2/2024 - 03:01
*/

class RankOrbitListener : OrbitListener {

    @Orbit(Rank.RANK_CREATED)
    fun onRankCreate(data: JsonObject) {
        val rank = dev.ryu.core.shared.CoreAPI.rankManager.findById(data["_id"].asString)

        if (rank == null) {
            dev.ryu.core.shared.CoreAPI.rankManager.cache[data["_id"].asString] = dev.ryu.core.shared.CoreAPI.getGson().fromJson(data, Rank::class.java)
            return
        }

        dev.ryu.core.shared.CoreAPI.rankManager.cache.replace(data["_id"].asString, dev.ryu.core.shared.CoreAPI.getGson().fromJson(data, Rank::class.java))
    }

    @Orbit(Rank.RANK_DELETED)
    fun onRankDelete(data: JsonObject) {
        dev.ryu.core.shared.CoreAPI.rankManager.cache.remove(data["_id"].asString)
    }

    @Orbit(Rank.RANK_UPDATED)
    fun onRankUpdate(data: JsonObject) {
        val rank = dev.ryu.core.shared.CoreAPI.rankManager.findById(data["_id"].asString)

        if (rank == null) {
            dev.ryu.core.shared.CoreAPI.rankManager.cache[data["_id"].asString] = dev.ryu.core.shared.CoreAPI.getGson().fromJson(data, Rank::class.java)
            return
        }

        dev.ryu.core.shared.CoreAPI.rankManager.cache.replace(data["_id"].asString, dev.ryu.core.shared.CoreAPI.getGson().fromJson(data, Rank::class.java))
    }

}