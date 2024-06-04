package dev.ryu.core.bukkit.system.reboot.task

import com.starlight.nexus.util.TextSplitter
import com.starlight.nexus.util.time.TimeUtil
import dev.ryu.core.bukkit.Core
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable

class ServerRebootTask(time: Long, private val reason: String) : BukkitRunnable() {

    var secondsRemaining: Int = (time / 1000).toInt()
    private val wasWhitelisted: Boolean = Core.get().server.hasWhitelist()

    override fun run() {
        when (secondsRemaining) {
            300 -> Core.get().server.setWhitelist(true)
            0 -> {
                Core.get().server.setWhitelist(wasWhitelisted)
                Core.get().server.shutdown()
            }
        }

        val messageList = mutableListOf<String?>().apply {
            when (secondsRemaining) {
                in 1..5 -> add("${ChatColor.DARK_RED}[Reboot] ${ChatColor.RESET}${ChatColor.RED}Server will restart in ${ChatColor.BOLD}$secondsRemaining${ChatColor.RESET}${ChatColor.RED} seconds.")
                in arrayOf(5, 10, 15, 30, 60, 120, 180, 240, 300, 600) -> {
                    add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}------------------------------------")
                    add("    ${ChatColor.RED}⚠ ${ChatColor.BOLD}${ChatColor.DARK_RED}Server Reboot Notification ${ChatColor.RED}⚠")
                    add("")
                    add("${ChatColor.WHITE}This server will reboot in: ${ChatColor.YELLOW}${TimeUtil.formatIntoDetailedString(secondsRemaining.toLong() * 1000)}")
                    add("${ChatColor.WHITE}Reason: ${ChatColor.YELLOW}$reason")
                    add("")
                    addAll(TextSplitter.split(text = "${ChatColor.GRAY}${ChatColor.ITALIC}If you experience any issues, please report them to a staff member."))
                    add("${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}------------------------------------")
                }
            }
        }

        messageList.forEach { message ->
            message?.let { Bukkit.broadcastMessage(it) }
        }

        secondsRemaining--
    }


    override fun cancel() {
        super.cancel()
        Core.get().server.setWhitelist(wasWhitelisted)
    }
}
