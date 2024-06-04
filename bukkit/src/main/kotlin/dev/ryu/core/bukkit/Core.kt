package dev.ryu.core.bukkit

import com.google.gson.JsonObject
import com.starlight.nexus.Nexus
import com.starlight.nexus.command.CommandHandler
import com.starlight.nexus.config.SubFileConfig
import com.starlight.nexus.util.time.Duration
import dev.ryu.core.bukkit.manager.LoaderManager
import dev.ryu.core.bukkit.manager.PermissionManager
import dev.ryu.core.bukkit.manager.ServerManager
import dev.ryu.core.bukkit.parameter.*
import dev.ryu.core.bukkit.system.grant.GrantBukkitAdapter
import dev.ryu.core.bukkit.system.staffmode.type.StaffModeType
import dev.ryu.core.bukkit.system.tips.TipManager
import dev.ryu.core.bukkit.system.webhook.WebHookManager
import dev.ryu.core.linker.main.MLinker
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.Tag
import dev.ryu.core.shared.system.extra.tag.TagType
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.NetworkModule
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CompletableFuture

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

class Core : JavaPlugin() {

    override fun onEnable() {
        saveDefaultConfig()

        config.getStringList("coinshop_menus").forEach {
            val configFile = SubFileConfig(this, "coinshop", "$it.yml")
            configFile.save()

            coinshopFiles[it] = configFile
        }

        nexus.instance = this;
        nexus.onEnable();

        val externalModulesPath = File(dataFolder, "modules")

        if (!externalModulesPath.exists()) {
            externalModulesPath.mkdirs();
        }

        MLinker.plugin = this
        Shared.moduleManager.externalModulesPath = externalModulesPath
        Shared.onEnable()

        LoaderManager.onEnable()
        PermissionManager.onEnable()
        ServerManager.onEnable()

        if (config.getBoolean("tips.settings.status")) {
            TipManager.onEnable()
        }

        NetworkModule.server = ServerManager.server

        if (config.getString("server-info.name") == "Practice") {
            staffModeType = StaffModeType.PRACTICE
        } else if (config.getString("server-info.name") == "Annihilation") {
            staffModeType = StaffModeType.ANNIHILATION
        }

        server.messenger.registerOutgoingPluginChannel(this, "BungeeCord");

        CommandHandler.registerParameterType(Code::class.java, CodeParameterType())
        CommandHandler.registerParameterType(ChatColor::class.java, ColorParameterType())
        CommandHandler.registerParameterType(Duration::class.java, DurationParameterType())
        CommandHandler.registerParameterType(Profile::class.java, ProfileParameterType())
        CommandHandler.registerParameterType(Rank::class.java, RankParameterType())
        CommandHandler.registerParameterType(Tag::class.java, TagParameterType())
        CommandHandler.registerParameterType(TagType::class.java, TagTypeParameterType())
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.admin")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.punishment")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.permission")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.server")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.grant")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.rank")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.staff")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.tag")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.friend")
        CommandHandler.registerPackage(this, "dev.ryu.core.bukkit.command.coinshop")

        GrantModule.setProvider(GrantBukkitAdapter())
        server.scheduler.runTaskTimerAsynchronously(this, GrantModule.expiryService, 40L, 40L)

        CompletableFuture.runAsync {
            try {
                Thread.sleep(3000)
                serverLoaded = true
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }

        if (webhookEnabled) {
            webhookManager = WebHookManager
        }

        if (webhookManager.enabled) {
            val data = JsonObject()

            data.addProperty("serverName", get().config.getString("server-info.name"))
            data.addProperty("serverGroup", get().config.getString("server-info.group"))

            val date = Date(System.currentTimeMillis())
            val format = SimpleDateFormat("EEEE MMMM d h:mm a yyyy", Locale.getDefault())
            val finalDate = format.format(date)

            data.addProperty("startedAt", finalDate)
            data.addProperty("serverStatus", "Online")

            //ServersLogWebhook.onServerStart(data)
        }

        startupUptime = System.currentTimeMillis()
        server.setWhitelist(false)
    }

    override fun onDisable() {

        if (webhookEnabled) {
            val data = JsonObject()

            data.addProperty("serverName", get().config.getString("server-info.name"))
            data.addProperty("serverGroup", get().config.getString("server-info.group"))

            val date = Date(System.currentTimeMillis())
            val format = SimpleDateFormat("EEEE MMMM d h:mm a yyyy", Locale.getDefault())
            val finalDate = format.format(date)

            data.addProperty("stoppedAt", finalDate)
            data.addProperty("serverStatus", "Offline")

            //ServersLogWebhook.onServerStop(data)
        }

        ServerManager.onDisable()
        PermissionManager.onDisable()
        LoaderManager.onDisable()
        Shared.onDisable()
        Nexus.onDisable()

    }

    var serverLoaded = false
    var staffModeType: StaffModeType = StaffModeType.GENERAL
    var webhookEnabled = config.getBoolean("discord_webhook.status")
    var nexus = Nexus
    var startupUptime: Long? = null
    lateinit var webhookManager: WebHookManager
    var coinshopFiles: MutableMap<String, SubFileConfig> = mutableMapOf()

    companion object {

        fun get() : Core {
            return getPlugin(Core::class.java)
        }

    }

    fun getCoinShopFileById(name: String): SubFileConfig {
        return coinshopFiles[name]!!
    }

}