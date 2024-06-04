package dev.ryu.core.shared.system.repository

import com.google.gson.GsonBuilder
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.extra.IRepository
import dev.ryu.core.shared.system.module.RankModule
import com.starlight.nexus.util.LongDeserializer
import org.bson.Document

/*
    * Author: T4yrn
    * Project: core
    * Date: 24/2/2024 - 02:49
*/

class RankRepository : IRepository<String, Rank> {

    /**
    * TODO: VERY IMPORTANT
    * fix gson serializing "long" values as "strings" so we can sort mongo with "created" timestamp
    * instead of manually sorting them -> CoreAPI.backendManager.getCollection("grants").find(filters).sort(Filters.eq("created",-1)).limit(1)
    **/

    override fun pull(): Map<String, Rank> {
        return Shared.backendManager.getCollection("ranks").find().map { Shared.getGson().fromJson(it.toJson(), Rank::class.java) }.associateBy { it.id }.toMutableMap()
    }

    override fun update(value: Rank): Boolean {
        val result = Shared.backendManager.getCollection("ranks").updateOne(Filters.eq("_id", value.id), Document("\$set", Document.parse(
            Shared.getGson().toJson(value))), UpdateOptions().upsert(true))

        return if (result.wasAcknowledged()) {
            RankModule.cache[value.id] = value
            true
        } else {
            false
        }
    }

    override fun delete(value: Rank): Boolean {
        return Shared.backendManager.getCollection("ranks").deleteOne(Filters.eq("_id", value.id)).wasAcknowledged()
    }

    override fun findById(id: String): Rank? {

        val document = Shared.backendManager.getCollection("ranks").find(Filters.eq("_id", id)).first() ?: return null

        return Shared.getGson().fromJson(document.toJson(), Rank::class.java)
    }

    fun findAllRanks() : MutableSet<Rank> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        val cursor = Shared.backendManager.getCollection("ranks").find()

        val codeSet = mutableSetOf<Rank>()
        cursor.forEach {
            val code = gson.fromJson(it.toJson(), Rank::class.java)
            codeSet.add(code)
        }

        return codeSet
    }

}