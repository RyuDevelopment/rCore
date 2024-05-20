package dev.ryu.core.shared.system.module

import com.google.gson.Gson
import com.mongodb.client.MongoCollection
import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.backend.mongodb.MongoManager
import dev.ryu.core.shared.backend.redis.RedisManager
import dev.t4yrn.jupiter.JupiterAPI
import org.bson.Document

/*
    * Project: core
    * Date: 24/2/2024 - 01:38
*/

object BackendModule : IModule {

    private val collections: HashMap<String, MongoCollection<Document>> = HashMap()

    private lateinit var mongoManager: MongoManager
    private lateinit var redisManager : RedisManager

    override fun id(): Int {
        return 1
    }

    override fun onEnable() {
        registerManagers()
        registerCollections()
    }

    override fun onDisable() {
        redisManager.client.close()
        mongoManager.client.close()
    }

    override fun onReload() {}

    override fun moduleName(): String {
        return "Backend System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    private fun registerManagers() {
        redisManager = RedisManager
        mongoManager = MongoManager
    }

    fun getGson() : Gson {
        return redisManager.gson
    }

    fun getJupiter() : JupiterAPI {
        return redisManager.jupiter
    }

    fun getCollection(collection: String): MongoCollection<Document> {
        return collections[collection]!!
    }

    private fun registerCollections() {
        collections["groups"] = MongoManager.groups
        collections["proxies"] = MongoManager.proxies
        collections["servers"] = MongoManager.servers
        collections["ranks"] = MongoManager.ranks
        collections["codes"] = MongoManager.codes
        collections["grants"] = MongoManager.grants
        collections["punishments"] = MongoManager.punishments
        collections["profiles"] = MongoManager.profiles
        collections["sessions"] = MongoManager.sessions
        collections["tags"] = MongoManager.tags
        collections["reports"] = MongoManager.reports
    }

}