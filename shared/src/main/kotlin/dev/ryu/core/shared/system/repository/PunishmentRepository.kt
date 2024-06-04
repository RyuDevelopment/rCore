package dev.ryu.core.shared.system.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.extra.IRepository
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

class PunishmentRepository : IRepository<UUID, Punishment> {

    /**
    * TODO: VERY IMPORTANT
    * fix gson serializing "long" values as "strings" so we can sort mongo with "created" timestamp
    * instead of manually sorting them -> CoreAPI.backendManager.getCollection("grants").find(filters).sort(Filters.eq("created",-1)).limit(1)
    **/

    @Deprecated(message = "empty()",level = DeprecationLevel.HIDDEN)
    override fun pull(): Map<UUID, Punishment> {
        return HashMap()
    }

    override fun update(value: Punishment): Boolean {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").updateOne(Filters.eq("_id",value.id.toString()),Document("\$set",Document.parse(
            dev.ryu.core.shared.Shared.getGson().toJson(value))), UpdateOptions().upsert(true)).wasAcknowledged()
    }

    override fun delete(value: Punishment): Boolean {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").deleteOne(Filters.eq("_id",value.id.toString())).wasAcknowledged()
    }

    override fun findById(id: UUID): Punishment? {

        val document = dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").find(Filters.eq("_id",id.toString())).first() ?: return null

        return dev.ryu.core.shared.Shared.getGson().fromJson(document.toJson(), Punishment::class.java)
    }

    fun findByVictim(victim: UUID,type: Punishment.Type):MutableSet<Punishment> {
        return this.findByVictim(victim,arrayListOf(type))
    }

    fun findByVictim(victim: UUID,types: MutableList<Punishment.Type>):MutableSet<Punishment> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").find(Filters.eq("victim",victim.toString())).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Punishment::class.java)}.filter{types.contains(it.type)}.toMutableSet()
    }

    fun findByVictimOrIdentifier(victim: UUID,addresses: MutableList<String>):MutableSet<Punishment> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").find(Filters.or(Filters.eq("victim",victim.toString()),Filters.or(addresses.map{Filters.eq("addresses",it)}))).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Punishment::class.java)}.toMutableSet()
    }

    fun findBySender(uuid: UUID,type: Punishment.Type):MutableSet<Punishment> {
        return this.findBySender(uuid,arrayListOf(type))
    }

    fun findBySender(uuid: UUID,types: MutableList<Punishment.Type>):MutableSet<Punishment> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").find(Filters.eq("sender",uuid.toString())).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Punishment::class.java)}.filter{types.contains(it.type)}.toMutableSet()
    }

    fun findBySenderOrPardoner(uuid: UUID):MutableSet<Punishment> {
        return dev.ryu.core.shared.Shared.backendManager.getCollection("punishments").find(Filters.or(arrayListOf(Filters.eq("sender",uuid.toString()),Filters.eq("pardoner",uuid.toString())))).map{ dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Punishment::class.java)}.toMutableSet()
    }

    fun findMostRecentPunishment(punishments: MutableSet<Punishment>): Punishment? {
        return this.findMostRecentPunishment(punishments,punishments.first().type)
    }

    fun findMostRecentPunishment(punishments: MutableSet<Punishment>, type: Punishment.Type): Punishment? {
        return this.findMostRecentPunishment(punishments,arrayListOf(type))
    }

    fun findMostRecentPunishment(punishments: MutableSet<Punishment>, types: MutableList<Punishment.Type>): Punishment? {
        return punishments
                .filter{types.contains(it.type)}
                .filter{!it.isVoided() && !it.isPardoned()}
                .sortedBy{it.type.ordinal}
                .reversed()
                .sortedBy{it.created}
                .reversed()
                .firstOrNull()
    }

}