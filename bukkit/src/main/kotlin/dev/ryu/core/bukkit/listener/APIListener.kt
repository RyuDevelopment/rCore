package dev.ryu.core.bukkit.listener

import com.google.gson.JsonObject
import com.mongodb.client.model.Filters
import com.starlight.nexus.manager.EventManager
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.CoreAPI
import dev.ryu.core.bukkit.event.profile.ProfileLoadEvent
import dev.ryu.core.bukkit.listener.orbit.PunishmentOrbitListener
import dev.ryu.core.bukkit.prompt.server.info.CustomServerInfo
import dev.ryu.core.bukkit.system.profile.ProfileLoadException
import dev.ryu.core.linker.manager.ModuleManager
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Punishment
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import dev.ryu.core.shared.system.module.PunishmentModule
import dev.ryu.core.shared.system.module.SessionModule
import dev.t4yrn.jupiter.Jupiter
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.*
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 10:05
*/

object APIListener {

    init {

        EventManager.subscribe(AsyncPlayerPreLoginEvent::class) {
            if (name == null) {
                return@subscribe
            }

            if (!Core.get().serverLoaded) {
                disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}The server is still being set up!")
                return@subscribe
            }

            if (name.length > 16 || name.length < 3) {
                disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "${ChatColor.RED}You can't join the server with that name length.")
                return@subscribe
            }

            val player = Core.get().server.getPlayer(uniqueId)

            if (player != null) {
                disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER,"You logged in too fast! Please relog.")
                return@subscribe
            }

            val document = Shared.backendManager.getCollection("profiles").find(Filters.eq("_id",uniqueId.toString())).first()

            val profile: Profile = if (document == null) Profile(uniqueId) else {

                try {
                    Shared.getGson().fromJson(document.toJson(), Profile::class.java)
                } catch (ex: Exception) {
                    loginResult = AsyncPlayerPreLoginEvent.Result.KICK_OTHER
                    kickMessage = "${ChatColor.RED}There was an issue contacting the API, please try again later."
                    throw ProfileLoadException(uniqueId,ex.message!!)
                }

            }

            val identifier = address.hostAddress

            profile.online = true

            val updateName = profile.name != name || !Shared.profileManager.isCached(uniqueId)
            val updateAddress = !profile.addresses.contains(identifier)

            if (updateName || updateAddress) {

                if (updateAddress) {
                    profile.address = identifier
                    profile.addresses.add(identifier)
                }

                Bukkit.getServer().scheduler.runTaskAsynchronously(Core.get()) {

                    if (updateName) {

                        val payload = JsonObject()

                        payload.addProperty("_id",uniqueId.toString())
                        payload.addProperty("name",name)

                        Shared.profileManager.updateId(uniqueId,name)
                        Shared.backendManager.getJupiter().sendPacket(Jupiter(Profile.PROFILE_NAME_UPDATE,payload))
                    }

                    if (updateAddress) {
                        Shared.profileManager.repository.update(profile)
                    }

                }

            }

            profile.name = name

            val punishments = PunishmentModule.repository.findByVictimOrIdentifier(uniqueId,profile.addresses)

            if (punishments.isNotEmpty()) {

                val punishment = PunishmentModule.repository.findMostRecentPunishment(punishments,arrayListOf(Punishment.Type.BLACKLIST,Punishment.Type.BAN))

                if (punishment != null) {
                    loginResult = AsyncPlayerPreLoginEvent.Result.KICK_BANNED
                    kickMessage = PunishmentOrbitListener.getPunishmentKickMessage(uniqueId,punishment,false)
                    return@subscribe
                }

            }

            PunishmentModule.mutes[profile.id] = punishments.filter{it.type == Punishment.Type.MUTE}.toHashSet()

            Shared.profileManager.cache[profile.id] = profile

            val grants = GrantModule.repository.findAllByPlayer(uniqueId)

            if (grants.isEmpty()) {
                GrantModule.grant(Shared.rankManager.defaultRank, profile.id, UUID.fromString(Profile.CONSOLE_UUID), "Initial Rank", 0L)
            }

            GrantModule.active[profile.id] = ArrayList()
            GrantModule.active[profile.id]!!.addAll(grants.filter{it.isActive()})

            GrantModule.setGrant(uniqueId,grants)

            Core.get().server.pluginManager.callEvent(ProfileLoadEvent(profile))
        }

        EventManager.subscribe(PlayerLoginEvent::class, EventPriority.MONITOR) {
            if (!Core.get().serverLoaded) {
                result = PlayerLoginEvent.Result.KICK_OTHER
                kickMessage = "${ChatColor.RED}The server is still being set up!"
                return@subscribe
            }

            if (player.name.length > 16 || player.name.length < 3) {
                result = PlayerLoginEvent.Result.KICK_OTHER
                kickMessage = "${ChatColor.RED}You can't join the server with that name length."
                return@subscribe
            }

            if (ModuleManager.isModuleEnabled(ModuleManager.findById(7))) {
                SessionModule.session(player.uniqueId, player.name, System.currentTimeMillis())
            }

            val profile = ProfileModule.findById(player.uniqueId)

            if (profile != null) {
                profile.name = player.name
                profile.online = true
                profile.currentServer = Core.get().config.getString("server-info.name")
                profile.lastJoined = System.currentTimeMillis()

                ProfileModule.repository.update(profile)
            } else {
                result = PlayerLoginEvent.Result.KICK_OTHER
                kickMessage = "${ChatColor.RED}We couldn't load your profile, please join again!"
                return@subscribe
            }

            allow()
        }

        EventManager.subscribe(PlayerQuitEvent::class, EventPriority.MONITOR) {
            val profile = ProfileModule.findById(player.uniqueId)

            if (ModuleManager.isModuleEnabled(ModuleManager.findById(7))) {
                val session = SessionModule.repository.findMostRecentById(player.uniqueId)

                if (session != null) {
                    session.leftAt = System.currentTimeMillis()
                    session.totalPlaytime = (System.currentTimeMillis() - session.joinedAt) / 1000

                    SessionModule.repository.update(session)
                }
            }

            if (profile != null) {
                profile.lastServer = Core.get().config.getString("server-info.name")
                profile.online = false
                profile.currentServer = "N/A"
                profile.discordId = profile.discordId

                ProfileModule.repository.update(profile)
            }

            GrantModule.active.remove(player.uniqueId)
            PunishmentModule.mutes.remove(player.uniqueId)
        }

        EventManager.subscribe(AsyncPlayerChatEvent::class) {
            if (isCancelled) {
                return@subscribe
            }

            val player = player
            val profile = ProfileModule.findById(player.uniqueId)
            val color = try {
                ChatColor.valueOf(profile?.chatColor ?: "WHITE")
            } catch (ex: Exception) {
                ChatColor.WHITE
            }

            val punishment = PunishmentModule.repository.findMostRecentPunishment(PunishmentModule.mutes[player.uniqueId]!!, arrayListOf(Punishment.Type.MUTE))
            if (punishment != null) {
                val message = if (punishment.isPermanent()) {
                    "You are permanently silenced."
                } else {
                    "You are currently silenced, you may speak again in ${ChatColor.YELLOW}${TimeUtil.formatIntoDetailedString(punishment.getRemaining())}${ChatColor.RED}."
                }
                player.sendMessage("${ChatColor.RED}$message")
                isCancelled = true
                return@subscribe
            }

            if (!Core.get().config.getBoolean("chatFormat.enabled")) {
                return@subscribe
            }


            val bestGrant = GrantModule.findBestRank(GrantModule.repository.findAllByPlayer(player.uniqueId))
            var prefix = bestGrant.prefix

            if (prefix == null) {
                prefix = ""
            }

            val messageFormat = Core.get().config.getString("chatFormat.text")
                .replace("{rank}", Color.color(prefix))
                .replace("{tag}", CoreAPI.getActivePlayerTag(player) + "${ChatColor.RESET}")
                .replace("{name}",Color.color("${ChatColor.getLastColors(prefix) ?: ChatColor.WHITE.toString()}${player.name}") + "${ChatColor.RESET}: ")
                .replace("{message}", "$color${message}")

            isCancelled = true

            recipients.forEach { recipient -> recipient.sendMessage(messageFormat) }
        }

        EventManager.subscribe(PlayerMoveEvent::class) {
            if (player.hasMetadata("creatingCustomServer")) {
                CustomServerInfo.serverName = null
                CustomServerInfo.port = null
                CustomServerInfo.spigot = null
                CustomServerInfo.maxPlayers = 250
                CustomServerInfo.pluginsAvailables.clear()
                CustomServerInfo.spigotsAvailables.clear()

                CustomServerInfo.pluginsAvailables.putAll(
                    hashMapOf(
                        "Core" to true,
                        "WorldEdit" to true,
                        "ViaVersion" to true,
                        "ProtocolLib" to true,
                        "Spark" to true
                    )
                )
            }
        }

    }

}