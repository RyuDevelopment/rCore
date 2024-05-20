package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.extra.grant.GrantExpiryService
import dev.ryu.core.shared.system.orbit.GrantOrbitListener
import dev.ryu.core.shared.system.repository.GrantRepository
import dev.t4yrn.jupiter.Jupiter
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 19/2/2024 - 23:16
*/

object GrantModule : IModule {

    val grant = HashMap<UUID, Rank>()
    val active = HashMap<UUID,ArrayList<Grant>>()

    val repository = GrantRepository()
    val expiryService = GrantExpiryService()

    private var adapter: Optional<GrantAdapter> = Optional.empty()

    override fun id(): Int {
        return 4
    }

    override fun onEnable() {
        BackendModule.getJupiter().addListener(GrantOrbitListener())
    }

    override fun onDisable() {}

    override fun onReload() {}

    override fun moduleName(): String {
        return "Grant System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun grant(rank: Rank, target: UUID, sender: UUID, reason: String):Boolean {
        return grant(rank,target,sender,reason,0L)
    }

    fun grant(rank: Rank, target: UUID, sender: UUID, reason: String, duration: Long):Boolean {

        val grant = Grant(UUID.randomUUID(), rank.id,target,sender)

        grant.reason = reason
        grant.duration = duration

        repository.update(grant)

        BackendModule.getJupiter().sendPacket(Jupiter(Grant.GRANT_EXECUTE,grant))

        return true
    }

    fun remove(grant: Grant, remover: UUID, reason: String):Boolean {

        grant.remover = remover
        grant.removed = System.currentTimeMillis()
        grant.removedReason = reason

        val toReturn = repository.update(grant)

        if (toReturn) {
            BackendModule.getJupiter().sendPacket(Jupiter(Grant.GRANT_REMOVE,grant))
        }

        return toReturn
    }

    fun findDisplayName(profile: Profile):String {
        return findDisplayName(profile.id, profile.name!!)
    }

    fun findDisplayName(id: UUID):String {
        return findDisplayName(id, ProfileModule.findById(id)!!.name!!)
    }

    fun findDisplayName(id: UUID,grants: MutableSet<Grant>):String {
        return findDisplayName(ProfileModule.findById(id)!!.name!!,grants)
    }

    fun findDisplayName(id: UUID,name: String):String {

        if (id == UUID.fromString(Profile.CONSOLE_UUID)) {
            return "§4§lConsole"
        }

        if (grant.containsKey(id)) {

            var toReturn = ""

            toReturn += "${findBestRank(id).color.replace("&","§",false)}${name}"

            return toReturn
        }

        return findDisplayName(name, repository.findAllByPlayer(id))
    }

    fun findDisplayName(name: String,grants: MutableSet<Grant>):String {

        val grantedRank = grants.filter{!it.isVoided() && !it.isRemoved()}.mapNotNull{it.getRank()}.sortedBy{it.weight}.reversed().firstOrNull() ?: RankModule.defaultRank

        return "${grantedRank.color.replace("&","§",false)}${name}"
    }

    fun findGrantedRank(uuid: UUID): Rank {
        return grant[uuid] ?: RankModule.defaultRank
    }

    fun findBestRank(uuid: UUID): Rank {

        val grantedRank = findGrantedRank(uuid)

        return grantedRank
    }

    fun findBestRank(grants: MutableSet<Grant>): Rank {

        val grantedRank = grants.filter{!it.isVoided() && !it.isRemoved()}.mapNotNull{it.getRank()}.sortedBy{it.weight}.reversed().firstOrNull() ?: RankModule.defaultRank

        return grantedRank
    }

    fun findGrantedGrant(uuid: UUID,grants: MutableSet<Grant>): Grant? {
        return grants.filter{!it.isVoided() && !it.isRemoved()}.sortedBy{it.getPriority()}.reversed().firstOrNull()
    }

    fun setGrant(uuid: UUID, grants: Collection<Grant>) {
        grant[uuid] = (grants.filter{!it.isVoided() && !it.isRemoved()}.mapNotNull {it.getRank()}.sortedBy{it.weight}.reversed().firstOrNull() ?: RankModule.defaultRank)
    }

    fun findProvider():Optional<GrantAdapter> {
        return adapter
    }

    fun setProvider(adapter: GrantAdapter?) {
        GrantModule.adapter = Optional.ofNullable(adapter)
    }

    interface GrantAdapter {

        fun onGrantApply(uuid: UUID,grant: Grant)
        fun onGrantChange(uuid: UUID,grant: Grant)
        fun onGrantExpire(uuid: UUID,grant: Grant)
        fun onGrantRemove(uuid: UUID,grant: Grant)

    }

}