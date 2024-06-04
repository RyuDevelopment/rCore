package dev.ryu.core.shared.system.repository

import com.google.gson.GsonBuilder
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.extra.IRepository
import com.starlight.nexus.util.LongDeserializer
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 23/2/2024 - 00:33
*/

class CodeRepository : IRepository<UUID, Code> {

    /**
    * TODO: VERY IMPORTANT
    * fix gson serializing "long" values as "strings" so we can sort mongo with "created" timestamp
    * instead of manually sorting them -> CoreAPI.backendManager.getCollection("grants").find(filters).sort(Filters.eq("created",-1)).limit(1)
    **/

    @Deprecated(message = "empty()",level = DeprecationLevel.HIDDEN)
    override fun pull(): Map<UUID, Code> {
        return HashMap()
    }

    override fun update(value: Code): Boolean {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("codes").updateOne(Filters.eq("_id", value.id.toString()),Document("\$set",Document.parse(
            dev.ryu.core.shared.Shared.getGson().toJson(value))), UpdateOptions().upsert(true)).wasAcknowledged()
    }

    override fun delete(value: Code): Boolean {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("codes").deleteOne(Filters.eq("_id", value.id.toString())).wasAcknowledged()
    }

    override fun findById(id: UUID): Code? {

        val document = dev.ryu.core.shared.Shared.backendManager.getCollection("codes").find(Filters.eq("_id",id.toString())).first() ?: return null

        return dev.ryu.core.shared.Shared.getGson().fromJson(document.toJson(), Code::class.java)
    }

    fun findByCode(id: String): Code? {

        val document = dev.ryu.core.shared.Shared.backendManager.getCollection("codes").find(Filters.eq("code",id)).first() ?: return null

        return dev.ryu.core.shared.Shared.getGson().fromJson(document.toJson(), Code::class.java)
    }

    fun findAllCodes(): MutableSet<Code> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        val cursor = dev.ryu.core.shared.Shared.backendManager.getCollection("codes").find()

        val codeSet = mutableSetOf<Code>()
        cursor.forEach {
            val code = gson.fromJson(it.toJson(), Code::class.java)
            codeSet.add(code)
        }

        return codeSet
    }

    fun findAllByRank(rank: Rank): MutableSet<Code> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        return dev.ryu.core.shared.Shared.backendManager.getCollection("codes").find(Filters.eq("rank", rank.id)).map { gson.fromJson(it.toJson(), Code::class.java) }.toMutableSet()
    }

    fun findAllBySender(uuid: UUID):MutableSet<Code> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()
        return dev.ryu.core.shared.Shared.backendManager.getCollection("codes").find(Filters.eq("createdBy",uuid.toString())).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Code::class.java)}.toMutableSet()
    }

}