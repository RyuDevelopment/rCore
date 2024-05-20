package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.orbit.RankOrbitListener
import dev.ryu.core.shared.system.repository.RankRepository

object RankModule : IModule {

    val cache = hashMapOf<String, Rank>()
    val repository = RankRepository()

    lateinit var defaultRank: Rank

    override fun id(): Int {
        return 2
    }

    override fun onEnable() {
        cache.putAll(repository.pull())
        defaultRank = loadDefaultRank()
        BackendModule.getJupiter().addListener(RankOrbitListener())
    }

    override fun onDisable() {}

    override fun onReload() {
        cache.clear()
        cache.putAll(repository.pull())
        defaultRank = loadDefaultRank()
    }

    override fun moduleName(): String {
        return "Rank System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun findById(id: String): Rank? {
        return cache[id]
    }

    private fun loadDefaultRank(): Rank {
        if (cache.containsKey("Default")) {
            return cache["Default"]!!
        }

        val toReturn = Rank("Default")

        toReturn.default = true
        toReturn.display = "&7Default"
        toReturn.prefix = "&7"
        toReturn.createdAt = System.currentTimeMillis()

        if (repository.update(toReturn)) {
            cache[toReturn.id] = toReturn
        }

        return toReturn
    }

}