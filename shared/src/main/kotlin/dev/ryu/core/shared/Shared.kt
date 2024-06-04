package dev.ryu.core.shared

import com.google.gson.Gson
import dev.ryu.core.linker.manager.ModuleManager
import dev.ryu.core.shared.system.extra.IManager
import dev.ryu.core.shared.system.module.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 11:50
*/

object Shared : IManager {

    val moduleManager: ModuleManager = ModuleManager
    var backendManager: BackendModule = BackendModule
    var rankManager: RankModule = RankModule
    var codeManager: CodeModule = CodeModule
    var grantManager: GrantModule = GrantModule
    var punishmentManager: PunishmentModule = PunishmentModule
    var profileManager: ProfileModule = ProfileModule
    var sessionManager: SessionModule = SessionModule
    var tagManager: TagModule = TagModule
    val reportManager: ReportModule = ReportModule

    override fun onEnable() {
        listOf(
            backendManager,
            rankManager,
            codeManager,
            grantManager,
            punishmentManager,
            profileManager,
            sessionManager,
            tagManager,
            reportManager
        ).forEach { module ->
            moduleManager.registerModule(module, module.id())
        }

        moduleManager.onEnable()
    }

    override fun onDisable() {
        moduleManager.onDisable()
    }

    fun get(): Shared {
        return this
    }

    fun getGson(): Gson {
        return backendManager.getGson()
    }

}