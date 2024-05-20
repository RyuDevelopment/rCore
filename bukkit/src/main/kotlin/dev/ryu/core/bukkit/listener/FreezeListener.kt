package dev.ryu.core.bukkit.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object FreezeListener: Listener {

    @EventHandler
    fun onPlayerFrozenMove(event: PlayerMoveEvent) {
        if (event.player.hasMetadata("frozen")) {
            event.isCancelled = true
        }
    }

}