package dev.ryu.core.shared.system.module

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.result.UpdateResult
import dev.ryu.core.shared.system.Group
import dev.ryu.core.shared.system.Proxy
import dev.ryu.core.shared.system.Server
import dev.ryu.core.shared.system.orbit.NetworkOrbitListener
import dev.t4yrn.jupiter.Jupiter
import org.bson.Document
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

object NetworkModule {

    val groups = HashSet<Group>()
    val proxies = HashSet<Proxy>()
    val servers = HashSet<Server>()

    var server: Server? = null

    private val groupsCollection: MongoCollection<Document> = BackendModule.getCollection("groups")
    private val proxiesCollection: MongoCollection<Document> = BackendModule.getCollection("proxies")
    private val serversCollection: MongoCollection<Document> = BackendModule.getCollection("servers")

    init {
        groupsCollection.find().forEach{ groups.add(dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Group::class.java))}
        proxiesCollection.find().forEach{ proxies.add(dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Proxy::class.java))}
        serversCollection.find().forEach{ servers.add(dev.ryu.core.shared.Shared.getGson().fromJson(it.toJson(), Server::class.java))}

        BackendModule.getJupiter().addListener(NetworkOrbitListener())
    }

    fun findGroupById(id: String): Group? {
        return groups.firstOrNull{it.id.equals(id,true)}
    }

    fun findProxyById(id: String): Proxy? {
        return proxies.firstOrNull{it.id.equals(id,true)}
    }

    fun findServerById(id: String): Server? {
        return servers.firstOrNull{it.id.equals(id,true)}
    }

    fun update(group: Group):UpdateResult {

        val toReturn = groupsCollection.updateOne(Document("_id",group.id),Document("\$set",Document.parse(dev.ryu.core.shared.Shared.getGson().toJson(group))), UpdateOptions().upsert(true))

        if (toReturn.wasAcknowledged()) {
            BackendModule.getJupiter().sendPacket(Jupiter(Group.UPDATE_ID,group))
        }

        return toReturn
    }

    fun update(proxy: Proxy):UpdateResult {

        val toReturn = proxiesCollection.updateOne(Document("_id",proxy.id),Document("\$set",Document.parse(dev.ryu.core.shared.Shared.getGson().toJson(proxy))), UpdateOptions().upsert(true))

        if (toReturn.wasAcknowledged()) {
            BackendModule.getJupiter().sendPacket(Jupiter(Proxy.UPDATE_ID,proxy))
        }

        return toReturn
    }

    fun update(server: Server):UpdateResult {

        val toReturn = serversCollection.updateOne(Document("_id",server.id),Document("\$set",Document.parse(dev.ryu.core.shared.Shared.getGson().toJson(server))), UpdateOptions().upsert(true))

        if (toReturn.wasAcknowledged()) {
            BackendModule.getJupiter().sendPacket(Jupiter(Server.UPDATE_ID,server))
        }

        return toReturn
    }

    fun isOnline(uuid: UUID):Boolean {
        return servers.any{it.onlinePlayers.contains(uuid)}
    }

    fun findServer(uuid: UUID): Server? {
        return servers.firstOrNull{it.onlinePlayers.contains(uuid)}
    }

    const val NETWORK_JOIN_PACKET = "NETWORK_JOIN_PACKET"
    const val NETWORK_LEAVE_PACKET = "NETWORK_LEAVE_PACKET"
    const val NETWORK_SWITCH_PACKET = "NETWORK_SWITCH_PACKET"

}