package dev.ryu.core.shared.backend.mongodb

import com.mongodb.MongoClientURI
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import org.bson.Document

/*
    * Author: T4yrn
    * Project: core
    * Date: 24/2/2024 - 01:39
*/

object MongoManager {

    //var client: MongoClient
    var client: com.mongodb.MongoClient

    var groups: MongoCollection<Document>
    var proxies: MongoCollection<Document>
    var servers: MongoCollection<Document>
    var ranks: MongoCollection<Document>
    var codes: MongoCollection<Document>
    var grants: MongoCollection<Document>
    var punishments: MongoCollection<Document>
    var profiles: MongoCollection<Document>
    var sessions: MongoCollection<Document>
    var tags: MongoCollection<Document>
    var reports: MongoCollection<Document>

    init {
        //val connectionString = "mongodb://admin:seNw&D{h9{e[gn}ps.{pZ)mVT7,i63sV@localhost:27017/?authSource=admin" // with password
        //val settings = MongoClientSettings.builder().applyConnectionString(ConnectionString(connectionString)).build()
        //this.client = MongoClients.create(settings)

        this.client = com.mongodb.MongoClient(MongoClientURI("mongodb://localhost:27017"))

        val database: MongoDatabase = this.client.getDatabase("core")

        loadOrCreate(database, "groups")
        loadOrCreate(database, "proxies")
        loadOrCreate(database, "servers")
        loadOrCreate(database, "profiles")
        loadOrCreate(database, "punishments")
        loadOrCreate(database, "ranks")
        loadOrCreate(database, "grants")
        loadOrCreate(database, "codes")
        loadOrCreate(database, "sessions")
        loadOrCreate(database, "tags")
        loadOrCreate(database, "reports")

        this.groups = database.getCollection("groups")
        this.proxies = database.getCollection("proxies")
        this.servers = database.getCollection("servers")
        this.profiles = database.getCollection("profiles")
        this.punishments = database.getCollection("punishments")
        this.ranks = database.getCollection("ranks")
        this.grants = database.getCollection("grants")
        this.codes = database.getCollection("codes")
        this.sessions = database.getCollection("sessions")
        this.tags = database.getCollection("tags")
        this.reports = database.getCollection("reports")
    }

    private fun loadOrCreate(database: MongoDatabase, collection: String) {
        if (!database.listCollectionNames().into(ArrayList<String>()).contains(collection)) {
            database.createCollection(collection)
        }
    }

}