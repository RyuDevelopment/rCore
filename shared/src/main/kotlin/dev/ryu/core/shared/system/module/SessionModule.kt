package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Session
import dev.ryu.core.shared.system.repository.SessionRepository
import java.util.*

object SessionModule : IModule {

    val repository = SessionRepository()

    override fun id(): Int {
        return 7
    }

    override fun onEnable() {}

    override fun onDisable() {}

    override fun onReload() {}

    override fun moduleName(): String {
        return "Session System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun session(id: UUID, name: String, joinedAt: Long) : Boolean {
        return repository.update(Session(Date().toString(), id, name, joinedAt))
    }

    fun remove(session: Session) : Boolean {
        return repository.delete(session)
    }

}