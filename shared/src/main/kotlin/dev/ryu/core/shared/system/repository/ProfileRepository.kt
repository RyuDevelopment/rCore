package dev.ryu.core.shared.system.repository

import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.extra.IRepository
import dev.ryu.core.shared.system.module.ProfileModule
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 09:48
*/

class ProfileRepository : IRepository<UUID, Profile> {

    override fun pull(): Map<UUID,Profile> {
        return CoreAPI.backendManager.getCollection("profiles").find().map { CoreAPI.getGson().fromJson(it.toJson(), Profile::class.java) }.associateBy { it.id }.toMutableMap()
    }

    override fun update(value: Profile): Boolean {
        val gson = CoreAPI.getGson()
        val filter = Filters.eq("_id", value.id.toString())
        val updateDocument = Document("\$set", Document.parse(gson.toJson(value)))
        val updateOptions = UpdateOptions().upsert(true)

        val result = CoreAPI.backendManager.getCollection("profiles")
            .updateOne(filter, updateDocument, updateOptions)

        if (result.wasAcknowledged()) {
            ProfileModule.cache[value.id] = value
        }

        return result.wasAcknowledged()
    }

    override fun delete(value: Profile): Boolean {
        return CoreAPI.backendManager.getCollection("profiles").deleteOne(Filters.eq("_id",value.id.toString())).wasAcknowledged()
    }

    override fun findById(id: UUID): Profile? {

        val document = CoreAPI.backendManager.getCollection("profiles").find(Filters.eq("_id",id.toString())).first() ?: return null

        return CoreAPI.getGson().fromJson(document.toJson(),Profile::class.java)
    }

    fun findByAddress(address: String):Profile? {

        val document = CoreAPI.backendManager.getCollection("profiles").find(Filters.eq("addresses",address)).limit(1).first() ?: return null

        return CoreAPI.getGson().fromJson(document.toJson(),Profile::class.java)
    }

}