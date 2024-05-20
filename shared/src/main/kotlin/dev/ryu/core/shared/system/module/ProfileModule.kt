package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Profile.Companion.CONSOLE_UUID
import dev.ryu.core.shared.system.orbit.ProfileOrbitListener
import dev.ryu.core.shared.system.repository.ProfileRepository
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 09:47
*/

object ProfileModule : IModule {

    val cache = HashMap<UUID, Profile>()
    val repository = ProfileRepository()

    private val uuidToName = ConcurrentHashMap<UUID,String>()
    private val nameToUuid = ConcurrentHashMap<String,UUID>()

    override fun id(): Int {
        return 6
    }

    override fun onEnable() {
        cache.putAll(repository.pull())

        BackendModule.getCollection("profiles").find().iterator().forEachRemaining{
            val name = it.getString("name")
            val uuid = UUID.fromString(it.getString("_id"))

            uuidToName[uuid] = name
            nameToUuid[name] = uuid
        }

        BackendModule.getJupiter().addListener(ProfileOrbitListener())

        uuidToName[UUID.fromString(CONSOLE_UUID)] = "CONSOLE"
        nameToUuid["Console"] = UUID.fromString(CONSOLE_UUID)
    }

    override fun onDisable() {}

    override fun onReload() {}

    override fun moduleName(): String {
        return "Profile System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun findById(id: UUID):Profile? {
        return cache[id]
    }

    fun findByName(name: String): Profile? {
        return name.let { profile ->
            cache.values.firstOrNull { it.name == profile }
        }
    }

    fun updateId(id: UUID,name: String) {
        uuidToName[id] = name
        nameToUuid[name] = id
    }

    fun findId(name: String):UUID? {
        return nameToUuid[name]
    }

    fun findName(id: UUID):String {
        return uuidToName[id] ?: "null"
    }

    fun isCached(uuid: UUID):Boolean {
        return uuidToName.contains(uuid)
    }

    fun calculatePermissions(permissions: ArrayList<String>,defaultPermissions: Boolean):ConcurrentHashMap<String,Boolean> {

        val toReturn = ConcurrentHashMap<String,Boolean>()

        if (defaultPermissions) {
            toReturn.putAll(RankModule.defaultRank.permissions.associate{
                val value = !it.startsWith("-")
                return@associate (if (value) it else it.substring(1)).toLowerCase() to value
            })
        }

        toReturn.putAll(permissions.associate{
            val value = !it.startsWith("-")
            return@associate (if (value) it else it.substring(1)).toLowerCase() to value
        })

        return toReturn
    }

}