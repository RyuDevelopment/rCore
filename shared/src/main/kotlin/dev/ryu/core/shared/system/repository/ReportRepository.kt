package dev.ryu.core.shared.system.repository

import com.google.gson.GsonBuilder
import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import com.starlight.nexus.util.LongDeserializer
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Report
import dev.ryu.core.shared.system.extra.IRepository
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 23/2/2024 - 00:33
*/

class ReportRepository : IRepository<UUID, Report> {

    /**
    * TODO: VERY IMPORTANT
    * fix gson serializing "long" values as "strings" so we can sort mongo with "created" timestamp
    * instead of manually sorting them -> CoreAPI.backendManager.getCollection("grants").find(filters).sort(Filters.eq("created",-1)).limit(1)
    **/

    @Deprecated(message = "empty()",level = DeprecationLevel.HIDDEN)
    override fun pull(): Map<UUID, Report> {
        return HashMap()
    }

    override fun update(value: Report): Boolean {
        val document = Document.parse(Shared.getGson().toJson(value))
        val idFilter = Filters.eq("_id", value.id.toString())

        return Shared.backendManager.getCollection("reports").replaceOne(idFilter, document, ReplaceOptions().upsert(true)).wasAcknowledged()
    }

    override fun delete(value: Report): Boolean {
        return Shared.backendManager.getCollection("reports").deleteOne(Filters.eq("_id", value.id.toString())).wasAcknowledged()
    }

    override fun findById(id: UUID): Report? {

        val document = Shared.backendManager.getCollection("reports").find(Filters.eq("_id",id.toString())).first() ?: return null

        return Shared.getGson().fromJson(document.toJson(),Report::class.java)
    }

    fun findMostRecentById(id: UUID): Report? {
        val document = Shared.backendManager.getCollection("reports").find(Filters.eq("targetId",id.toString())).sort(Filters.eq("reportedAt",-1)).limit(1).first() ?: return null

        return Shared.getGson().fromJson(document.toJson(), Report::class.java)
    }

    fun findAllReportsById(id: UUID): MutableSet<Report> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        val filter = Filters.eq("targetId", id.toString())
        val cursor = Shared.backendManager.getCollection("reports").find(filter)

        val reportSet = mutableSetOf<Report>()
        cursor.forEach {
            val session = gson.fromJson(it.toJson(), Report::class.java)
            reportSet.add(session)
        }

        return reportSet
    }


    fun findAllReports(): MutableSet<Report> {
        val gson = GsonBuilder().registerTypeAdapter(Long::class.java, LongDeserializer).create()

        val cursor = Shared.backendManager.getCollection("reports").find()

        val reportSet = mutableSetOf<Report>()
        cursor.forEach {
            val report = gson.fromJson(it.toJson(), Report::class.java)
            reportSet.add(report)
        }

        return reportSet
    }

}