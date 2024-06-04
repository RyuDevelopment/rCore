package dev.ryu.core.proxy

import dev.ryu.core.proxy.api.APIListener
import dev.ryu.core.proxy.grant.GrantProxyAdapter
import dev.ryu.core.proxy.permission.PermissionHandler
import dev.ryu.core.proxy.proxy.ProxyHandler
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.module.GrantModule
import net.md_5.bungee.api.plugin.Plugin
import net.md_5.bungee.config.Configuration
import net.md_5.bungee.config.ConfigurationProvider
import net.md_5.bungee.config.YamlConfiguration
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.util.concurrent.TimeUnit

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class Core : Plugin() {

    lateinit var config: Configuration

    lateinit var proxyHandler: ProxyHandler
    lateinit var permissionHandler: PermissionHandler

    override fun onEnable() {
        instance = this
        this.saveDefaultConfig()

        this.config = ConfigurationProvider.getProvider(YamlConfiguration::class.java).load(File(this.dataFolder,"config.yml"))

        Shared.onEnable()

        this.proxyHandler = ProxyHandler(this)
        this.permissionHandler = PermissionHandler(this)

        GrantModule.setProvider(GrantProxyAdapter(this))

        this.proxy.pluginManager.registerListener(this, APIListener(this))

        this.proxy.scheduler.schedule(this, GrantModule.expiryService,2L,2L,TimeUnit.SECONDS)
    }

    private fun saveDefaultConfig() {

        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdir()
        }

        val file = File(this.dataFolder,"config.yml")

        if (file.exists()) {
            return
        }

        try {
            this.getResourceAsStream("config.yml").use{Files.copy(it,file.toPath())}
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }

    override fun onDisable() {
        this.proxyHandler.dispose()
    }

    companion object {

        lateinit var instance: Core

    }

}