package dev.ryu.core.shared.system.orbit

import com.google.gson.JsonObject
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.module.GrantModule
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 19/2/2024 - 23:16
*/

class GrantOrbitListener : OrbitListener {

    @Orbit(Grant.GRANT_EXECUTE)
    fun onGrantExecute(data: JsonObject) {

        val uuid = UUID.fromString(data["target"].asString)

        if (!GrantModule.active.containsKey(uuid)) {
            return
        }

        val grant = dev.ryu.core.shared.CoreAPI.getGson().fromJson(data, Grant::class.java)

        GrantModule.active[uuid]!!.add(grant)

        val rank = dev.ryu.core.shared.CoreAPI.rankManager.findById(grant.rank) ?: return

        GrantModule.findProvider().ifPresent{it.onGrantApply(uuid,grant)}

        if (rank.weight < GrantModule.findGrantedRank(uuid).weight) {
            return
        }

        val grants = GrantModule.active[uuid] ?: ArrayList()

        GrantModule.setGrant(uuid,grants)

        if (!grant.isPermanent()) {
            GrantModule.active.putIfAbsent(uuid,ArrayList())
            GrantModule.active[uuid]!!.add(grant)
        }

        GrantModule.findProvider().ifPresent{it.onGrantChange(uuid,grant)}
    }

    @Orbit(Grant.GRANT_REMOVE)
    fun onGrantRemove(data: JsonObject) {

        val uuid = UUID.fromString(data["target"].asString)

        if (!GrantModule.active.containsKey(uuid)) {
            return
        }

        val grant = dev.ryu.core.shared.CoreAPI.getGson().fromJson(data, Grant::class.java)

        GrantModule.active[uuid]!!.removeIf{it.rank == grant.rank}
        GrantModule.active[uuid]!!.add(grant)

        val grants = GrantModule.active[uuid]!!

        GrantModule.setGrant(uuid,grants)

        GrantModule.findProvider().ifPresent{it.onGrantRemove(uuid,grant)}
    }


}