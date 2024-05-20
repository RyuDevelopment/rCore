package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Report
import dev.ryu.core.shared.system.repository.ReportRepository
import java.util.*

object ReportModule: IModule {

    val repository = ReportRepository()

    override fun id(): Int {
        return 9
    }

    override fun onEnable() {
    }

    override fun onDisable() {
    }

    override fun onReload() {
    }

    override fun moduleName(): String {
        return "Reports System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun report(targetId: UUID, target: String, senderId: UUID, sender: String, server: String, reason: String) : Boolean {
        return repository.update(Report(UUID.randomUUID(), targetId, target, senderId, sender, server, System.currentTimeMillis(), reason))
    }

    fun remove(report: Report) : Boolean {
        return repository.delete(report)
    }

}