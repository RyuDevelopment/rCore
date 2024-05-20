package dev.ryu.core.bukkit.system.grant

import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.event.grant.GrantApplyEvent
import dev.ryu.core.bukkit.event.grant.GrantExpireEvent
import dev.ryu.core.bukkit.event.grant.GrantRemoveEvent
import dev.ryu.core.bukkit.manager.PermissionManager
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.module.GrantModule
import org.bukkit.ChatColor
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 17:42
*/

class GrantBukkitAdapter : GrantModule.GrantAdapter {

    override fun onGrantApply(uuid: UUID, grant: Grant) {

        val player = dev.ryu.core.bukkit.Core.get().server.getPlayer(uuid) ?: return

        PermissionManager.update(player,true)

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()){ dev.ryu.core.bukkit.Core.get().server.pluginManager.callEvent(GrantApplyEvent(player,grant))}
    }

    override fun onGrantChange(uuid: UUID,grant: Grant) {
        // We do nothing here, #onGrantApply already gets called which already does everything.
    }

    override fun onGrantExpire(uuid: UUID,grant: Grant) {

        val player = dev.ryu.core.bukkit.Core.get().server.getPlayer(uuid) ?: return

        val rank = grant.getRank()

        if (rank != null && rank.isHidden()) {
            player.sendMessage("${ChatColor.LIGHT_PURPLE}Your ${rank.display}${ChatColor.LIGHT_PURPLE} rank has expired.")
        }

        PermissionManager.update(player,true)

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()){ dev.ryu.core.bukkit.Core.get().server.pluginManager.callEvent(GrantExpireEvent(player,grant))}
    }

    override fun onGrantRemove(uuid: UUID,grant: Grant) {

        val player = dev.ryu.core.bukkit.Core.get().server.getPlayer(uuid) ?: return

        PermissionManager.update(player,true)

        dev.ryu.core.bukkit.Core.get().server.scheduler.runTask(dev.ryu.core.bukkit.Core.get()){ dev.ryu.core.bukkit.Core.get().server.pluginManager.callEvent(GrantRemoveEvent(player,grant))}
    }

}