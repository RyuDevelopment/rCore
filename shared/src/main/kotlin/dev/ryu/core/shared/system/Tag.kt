package dev.ryu.core.shared.system

import com.mongodb.client.model.Filters
import com.mongodb.client.model.ReplaceOptions
import dev.ryu.core.shared.CoreAPI
import org.bson.Document
import java.util.concurrent.CompletableFuture

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 25/April - 6:32 PM
*/

class Tag(
    var name: String,
    var display: String? = null,
    var permission: String? = null,
    var priority: Int = 1,
    var type: String? = null
) {

    init {
        load()
    }

    fun save(async: Boolean) {
        val document = Document()

        document["display"] = this.display
        document["permission"] = this.permission
        document["priority"] = this.priority
        document["type"] = this.type?.toUpperCase()

        if (async) {
            CompletableFuture.runAsync {
                dev.ryu.core.shared.CoreAPI.backendManager.getCollection("tags").replaceOne(
                    Filters.eq("_id", this.name),
                    document,
                    ReplaceOptions().upsert(true)
                )
            }
        } else {
            dev.ryu.core.shared.CoreAPI.backendManager.getCollection("tags").replaceOne(
                Filters.eq("_id", this.name),
                document,
                ReplaceOptions().upsert(true)
            )
        }
    }

    fun load() {
        val document = dev.ryu.core.shared.CoreAPI.backendManager.getCollection("tags").find(Filters.eq("_id", this.name)).first() ?: return

        this.display = document.getString("display")
        this.permission = document.getString("permission")
        this.priority = document.getInteger("priority")
        this.type = document.getString("type")
    }

    fun delete() {
        dev.ryu.core.shared.CoreAPI.tagManager.tags.remove(this.name)
        dev.ryu.core.shared.CoreAPI.backendManager.getCollection("tags").deleteOne(Filters.eq("_id", this.name))
    }

}