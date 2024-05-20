package dev.ryu.core.proxy.proxy

import dev.ryu.core.proxy.Core
import dev.ryu.core.proxy.proxy.command.AlertCommand
import dev.ryu.core.proxy.proxy.command.ListCommand
import dev.ryu.core.proxy.proxy.command.SendCommand
import dev.ryu.core.proxy.proxy.listener.HubListener
import dev.ryu.core.proxy.proxy.packet.ProxyPacketListener
import dev.ryu.core.proxy.staff.StaffServerListener
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Proxy
import dev.ryu.core.shared.system.extra.server.Status
import dev.ryu.core.shared.system.module.NetworkModule
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class ProxyHandler(private val instance: Core) {

    lateinit var proxy: Proxy
    lateinit var proxyID: String
    lateinit var proxyGroup: String

    private lateinit var initialized: AtomicBoolean
    private lateinit var updateTaskId: AtomicInteger

    init {
        this.setup()
    }

    private fun setup() {
        this.initialized = AtomicBoolean(false)

        try {
            this.proxyID = this.instance.proxy::class.java.getMethod("getID").invoke(this.instance.proxy) as String
        } catch (ex: NoSuchMethodException) {
            this.proxyID = this.instance.config.getString("id","NA")
        }

        try {
            this.proxyGroup = this.instance.proxy::class.java.getMethod("getGroup").invoke(this.instance.proxy) as String
        } catch (ex: NoSuchMethodException) {
            this.proxyGroup = this.instance.config.getString("group","unknown")
        }

        dev.ryu.core.shared.CoreAPI.backendManager.getJupiter().addListener(ProxyPacketListener(this.instance))

        // All these commands are so everything syncs across all proxies.
        this.instance.proxy.pluginManager.registerCommand(this.instance, SendCommand(this.instance))
        this.instance.proxy.pluginManager.registerCommand(this.instance, ListCommand(this.instance))
        this.instance.proxy.pluginManager.registerCommand(this.instance, AlertCommand(this.instance))

        this.instance.proxy.pluginManager.registerListener(this.instance, HubListener(this.instance))
        this.instance.proxy.pluginManager.registerListener(this.instance, StaffServerListener(this.instance))

        this.proxy = Proxy(this.proxyID,this.instance.proxy.config.listeners.first().host.port,this.proxyGroup)
        this.updateTaskId = AtomicInteger(this.instance.proxy.scheduler.schedule(this.instance,{this.update()},0L,2L,TimeUnit.SECONDS).id)

        this.initialized.set(true)
    }

    fun dispose() {

        if (!this.initialized.get()) {
            return
        }

        this.instance.proxy.scheduler.cancel(this.updateTaskId.get())

        this.proxy.update = System.currentTimeMillis()
        this.proxy.status = Status.OFFLINE
        this.proxy.onlinePlayers.clear()

        NetworkModule.update(this.proxy)
    }

    private fun update() {

        this.proxy.update = System.currentTimeMillis()
        this.proxy.status = Status.ONLINE
        this.proxy.onlinePlayers = this.instance.proxy.players.map{it.uniqueId}.toHashSet()
        this.proxy.bungeeServers = this.instance.proxy.servers.keys.toHashSet()

        NetworkModule.update(this.proxy)
    }

    companion object {

        const val SEND_PACKET = "SEND_PACKET"
        const val ALERT_PACKET = "ALERT_PACKET"

    }

}