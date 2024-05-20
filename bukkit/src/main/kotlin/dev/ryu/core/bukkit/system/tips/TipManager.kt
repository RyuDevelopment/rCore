package dev.ryu.core.bukkit.system.tips

import dev.ryu.core.bukkit.Core
import dev.ryu.core.shared.system.extra.IManager
import org.bukkit.Bukkit
import org.bukkit.Sound

object TipManager : IManager {

    private var tips: List<String> = emptyList()
    private var interval = Core.get().config.getInt("tips.settings.interval")
    private var lastTipIndex = -1

    override fun onEnable() {
        val tipsSection = Core.get().config.getConfigurationSection("tips")
        if (tipsSection != null) {
            tips = tipsSection.getKeys(false).flatMap { key ->
                tipsSection.getStringList(key)
            }
        }

        Bukkit.getScheduler().runTaskTimer(Core.get(), {
            if (Core.get().config.getBoolean("tips.settings.random")) {
                sendRandomTip()
            } else {
                sendTipInOrder()
            }
        }, 0L, (interval * 60L) * 20)
    }

    override fun onDisable() {}

    private fun sendRandomTip() {
        if (tips.isEmpty()) {
            return
        }

        val randomTip = tips.random()

        Bukkit.getOnlinePlayers().forEach {
            it.playSound(Sound.NOTE_PLING, 10.0f, 0.15f)
        }

        Bukkit.broadcastMessage(randomTip)
    }

    private fun sendTipInOrder() {
        if (tips.isEmpty()) {
            return
        }

        lastTipIndex = (lastTipIndex + 1) % tips.size
        val nextTip = tips[lastTipIndex]

        Bukkit.getOnlinePlayers().forEach {
            it.playSound(Sound.NOTE_PLING, 10.0f, 0.15f)
        }

        Bukkit.broadcastMessage(nextTip)
    }
}
