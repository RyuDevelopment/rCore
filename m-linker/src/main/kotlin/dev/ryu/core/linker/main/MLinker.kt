package dev.ryu.core.linker.main

import org.bukkit.plugin.java.JavaPlugin

object MLinker {

    lateinit var plugin: JavaPlugin

    fun get(): JavaPlugin{
        return plugin
    }

}