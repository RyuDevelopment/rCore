package dev.ryu.core.shared.system.repository

import com.google.gson.GsonBuilder
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Session
import dev.ryu.core.shared.system.extra.IRepository
import com.starlight.nexus.util.LongDeserializer
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 23/2/2024 - 00:33
*/

class SessionRepository : IRepository<UUID, Session> {

    /**
    * TODO: VERY IMPORTANT
    * fix gson serializing "long" values as "strings" so we can sort mongo with "created" timestamp
    * instead of manually sorting them -> CoreAPI.backendManager.getCollection("grants").find(filters).sort(Filters.eq("created",-1)).limit(1)
    **/

    @Deprecated(message = "empty()",level = DeprecationLevel.HIDDEN)
    override fun pull(): Map<UUID, Session> {
        return HashMap()
    }

    override fun update(value: Session): Boolean {
        val document = Document.parse(CoreAPI.getGson().toJson(value))
        val idFilter = Filters.eq("_id", value.id)

        return CoreAPI.backendManager.getCollection("sessions").replaceOne(idFilter, document, ReplaceOptions().upsert(true)).wasAcknowledged()
    }

    override fun delete(value: Session): Boolean {
        return CoreAPI.backendManager.getCollection("sessions").deleteOne(Filters.eq("_id", value.id)).wasAcknowledged()
    }

    override fun findById(id: UUID): Session? {

        val document = CoreAPI.backendManager.getCollection("sessions").find(Filters.eq("_id",id.toString())).first() ?: return null

        return CoreAPI.getGson().fromJson(document.toJson(),Session::class.java)
    }

    fun findMostRecentById(id: UUID): Session? {
        val document = CoreAPI.backendManager.getCollection("sessions").find(Filters.eq("uuid",id.toString())).sort(Filters.eq("joinedAt",-1)).limit(1).first() ?: return null

        return CoreAPI.getGson().fromJson(document.toJson(), Session::class.java)
    }

    fun findTotalPlaytimeById(id: UUID): Long {
        val filter = Filters.eq("uuid", id.toString())
        val cursor = CoreAPI.backendManager.getCollection("sessions").find(filter)

        var totalPlaytime = 0L
        cursor.forEach {
            val session = CoreAPI.getGson().fromJson(it.toJson(), Session::class.java)
            totalPlaytime += if (session.leftAt != null) {
                (session.leftAt!! - session.joinedAt)
            } else {
                (System.currentTimeMillis() - session.joinedAt)
            }
        }

        return totalPlaytime
    }

    fun findAllSessionsById(id: UUID): MutableSet<Session> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        val filter = Filters.eq("uuid", id.toString())
        val cursor = CoreAPI.backendManager.getCollection("sessions").find(filter)

        val sessionsSet = mutableSetOf<Session>()
        cursor.forEach {
            val session = gson.fromJson(it.toJson(), Session::class.java)
            sessionsSet.add(session)
        }

        return sessionsSet
    }


    fun findAllSessions(): MutableSet<Session> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        val cursor = CoreAPI.backendManager.getCollection("sessions").find()

        val sessionsSet = mutableSetOf<Session>()
        cursor.forEach {
            val sessions = gson.fromJson(it.toJson(), Session::class.java)
            sessionsSet.add(sessions)
        }

        return sessionsSet
    }

}