package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.repository.PunishmentRepository
import dev.t4yrn.jupiter.Jupiter
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

object PunishmentModule : IModule {

    val mutes = HashMap<UUID,HashSet<Punishment>>()
    val repository = PunishmentRepository()

    override fun id(): Int {
        return 5
    }

    override fun onEnable() {}

    override fun onDisable() {}

    override fun onReload() {}

    override fun moduleName(): String {
        return "Punishment System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun isMuted(uuid: UUID):Boolean {
        return mutes[uuid]?.any{it.type == Punishment.Type.MUTE && it.getRemaining() > 0} ?: false
    }

    fun punish(type: Punishment.Type,victim: UUID,sender: UUID,reason: String,server: String,silent: Boolean,victimDisplay: String,senderDisplay: String):Boolean {
        return punish(type,victim,ArrayList(),sender,reason,server,0L,silent,victimDisplay,senderDisplay)
    }

    fun punish(type: Punishment.Type,victim: UUID,sender: UUID,reason: String,server: String,duration: Long,silent: Boolean,victimDisplay: String,senderDisplay: String):Boolean {
        return punish(type,victim,ArrayList(),sender,reason,server,duration,silent,victimDisplay,senderDisplay)
    }

    fun punish(type: Punishment.Type,victim: UUID,identifiers: MutableList<String>,sender: UUID,reason: String,server: String,silent: Boolean,victimDisplay: String,senderDisplay: String):Boolean {
        return punish(type,victim,identifiers,sender,reason,server,0L,silent,victimDisplay,senderDisplay)
    }

    fun punish(type: Punishment.Type,victim: UUID,identifiers: MutableList<String>,sender: UUID,reason: String,server: String,duration: Long,silent: Boolean,victimDisplay: String,senderDisplay: String):Boolean {
        val punishment = Punishment(UUID.randomUUID(),type,victim,sender)

        punishment.silent = silent
        punishment.reason = reason
        punishment.server = server
        punishment.duration = duration
        punishment.identifiers = identifiers

        val toReturn = repository.update(punishment)

        if (toReturn) {

            val jsonObject = dev.ryu.core.shared.Shared.getGson().toJsonTree(punishment).asJsonObject

            jsonObject.addProperty("victimDisplay",victimDisplay)
            jsonObject.addProperty("senderDisplay",senderDisplay)

            BackendModule.getJupiter().sendPacket(Jupiter(Punishment.PACKET_ID,jsonObject))
        }

        return toReturn
    }
    fun pardon(punishment: Punishment, pardoner: UUID, reason: String, silent: Boolean, victimDisplay: String, senderDisplay: String):Boolean {

        punishment.pardoner = pardoner
        punishment.pardoned = System.currentTimeMillis()
        punishment.pardonReason = reason
        punishment.pardonedSilent = silent

        val toReturn = repository.update(punishment)

        if (toReturn) {

            val jsonObject = dev.ryu.core.shared.Shared.getGson().toJsonTree(punishment).asJsonObject

            jsonObject.addProperty("victimDisplay",victimDisplay)
            jsonObject.addProperty("senderDisplay",senderDisplay)

            BackendModule.getJupiter().sendPacket(Jupiter(Punishment.PACKET_ID,jsonObject))
        }

        return toReturn
    }

}