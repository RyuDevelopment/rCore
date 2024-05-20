package dev.ryu.core.linker.data

interface IModule {

    fun id() : Int

    fun onEnable()

    fun onDisable()

    fun onReload()

    fun moduleName(): String

    fun isDefault(): Boolean

}