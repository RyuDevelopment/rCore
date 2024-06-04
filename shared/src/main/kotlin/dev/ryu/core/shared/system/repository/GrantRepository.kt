package dev.ryu.core.shared.system.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.extra.IRepository
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 19/2/2024 - 23:16
*/

class GrantRepository : IRepository<UUID, Grant> {

    /**
    * TODO: VERY IMPORTANT
    * fix gson serializing "long" values as "strings" so we can sort mongo with "created" timestamp
    * instead of manually sorting them -> CoreAPI.backendManager.getCollection("grants").find(filters).sort(Filters.eq("created",-1)).limit(1)
    **/

    @Deprecated(message = "empty()",level = DeprecationLevel.HIDDEN)
    override fun pull(): Map<UUID, Grant> {
        return HashMap()
    }

    override fun update(value: Grant): Boolean {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("grants").updateOne(Filters.eq("_id",value.id.toString()),Document("\$set",Document.parse(
            dev.ryu.core.shared.Shared.getGson().toJson(value))), UpdateOptions().upsert(true)).wasAcknowledged()
    }

    override fun delete(value: Grant): Boolean {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("grants").deleteOne(Filters.eq("_id",value.id.toString())).wasAcknowledged()
    }

    override fun findById(id: UUID): Grant? {

        val document = dev.ryu.core.shared.Shared.backendManager.getCollection("grants").find(Filters.eq("_id",id.toString())).first() ?: return null

        return dev.ryu.core.shared.Shared.getGson().fromJson(document.toJson(), Grant::class.java)
    }

    fun findAllBySender(uuid: UUID):MutableSet<Grant> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("grants").find(Filters.eq("sender",uuid.toString())).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Grant::class.java)}.toMutableSet()
    }

    fun findAllByPlayer(target: UUID):MutableSet<Grant> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("grants").find(Filters.eq("target",target.toString())).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Grant::class.java)}.toMutableSet()
    }

    fun findAllBySenderOrRemover(uuid: UUID):MutableSet<Grant> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("grants").find(Filters.or(arrayListOf(Filters.eq("sender",uuid.toString()),Filters.eq("remover",uuid.toString())))).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Grant::class.java)}.toMutableSet()
    }

}