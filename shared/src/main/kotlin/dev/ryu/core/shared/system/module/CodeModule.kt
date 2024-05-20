package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.repository.CodeRepository
import java.util.*

object CodeModule : IModule {

    val repository = CodeRepository()

    override fun id(): Int {
        return 3
    }

    override fun onEnable() {}

    override fun onDisable() {}

    override fun onReload() {}

    override fun moduleName(): String {
        return "Code Reward System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun code(code: String, rank: String, createdBy: UUID, createdAt: Long) : Boolean {
        return code(UUID.randomUUID(), code, rank, 0L, createdBy, createdAt)
    }

    fun code(id: UUID, code: String, rank: String, rankDuration: Long, createdBy: UUID, createdAt: Long) : Boolean {
        return repository.update(Code(id, code, rank, rankDuration, createdBy, createdAt))
    }

    fun remove(code: Code) : Boolean {
        return repository.delete(code)
    }

}