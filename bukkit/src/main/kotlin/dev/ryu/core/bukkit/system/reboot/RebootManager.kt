package dev.ryu.core.bukkit.system.reboot

import com.starlight.nexus.util.time.Duration
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.system.reboot.task.ServerRebootTask

object RebootManager {

    private var serverRebootTask: ServerRebootTask? = null

    @JvmStatic
    fun reboot(time: Long, reason: String) {
        if (serverRebootTask != null) {
            throw IllegalStateException("A reboot is already in progress.")
        }
        serverRebootTask = ServerRebootTask(time, reason)
        serverRebootTask!!.runTaskTimer(Core.get(), 20L, 20L)
    }


    @JvmStatic
    fun isRebooting(): Boolean {
        return serverRebootTask != null
    }

    @JvmStatic
    fun getRebootSecondsRemaining(): Int {
        return serverRebootTask?.secondsRemaining ?: -1
    }

    @JvmStatic
    fun cancelReboot() {
        if (serverRebootTask == null) {
            return
        }
        serverRebootTask!!.cancel()
        serverRebootTask = null
    }

}
