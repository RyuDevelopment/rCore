package dev.ryu.core.bukkit.menu.session.element

import dev.ryu.core.shared.system.Session
import dev.ryu.core.shared.system.module.SessionModule
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.button.Button
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import java.text.SimpleDateFormat
import java.util.*

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 5:03 PM
*/

class SessionElement(
    val session: Session
) : Button() {

    override fun getName(p0: Player): String {
        return "${ChatColor.GOLD}${session.id}"
    }

    override fun getDescription(p0: Player): MutableList<String> {
        return arrayListOf<String>().also { toReturn ->
            toReturn.add("")

            val formattedJoinedAt = formatDate(session.joinedAt)

            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Joined${ChatColor.GRAY}: ${ChatColor.WHITE}${formattedJoinedAt}")

            if (session.leftAt != null) {
                val formattedLeftAt = formatDate(session.leftAt!!)

                toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Left${ChatColor.GRAY}: ${ChatColor.WHITE}${formattedLeftAt}")
            }
            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current Playtime${ChatColor.GRAY}: ${ChatColor.WHITE}${currentPlaytime()}")
            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Total Playtime${ChatColor.GRAY}: ${ChatColor.WHITE}${totalPlaytime()}")
            toReturn.add("")
        }
    }

    override fun getMaterial(p0: Player): Material {
        return Material.EMPTY_MAP
    }

    private fun currentPlaytime(): String {
        val elapsedTimeMillis = System.currentTimeMillis() - session.joinedAt
        val hours = elapsedTimeMillis / (1000 * 60 * 60)
        val minutes = (elapsedTimeMillis % (1000 * 60 * 60)) / (1000 * 60)
        val seconds = ((elapsedTimeMillis % (1000 * 60 * 60)) % (1000 * 60)) / 1000

        val formattedTime = buildString {
            if (hours > 0) {
                append("$hours ${if (hours.toInt() == 1) "hour" else "hours"}, ")
            }
            if (minutes > 0 || hours > 0) {
                append("$minutes ${if (minutes.toInt() == 1) "minute" else "minutes"}, ")
            }
            append("$seconds ${if (seconds.toInt() == 1) "second" else "seconds"}")
        }

        return formattedTime
    }

    private fun totalPlaytime(): String {
        val totalPlaytimeMillis = SessionModule.repository.findTotalPlaytimeById(session.uuid)

        val hoursTotal = totalPlaytimeMillis / (1000 * 60 * 60)
        val minutesTotal = (totalPlaytimeMillis % (1000 * 60 * 60)) / (1000 * 60)
        val secondsTotal = ((totalPlaytimeMillis % (1000 * 60 * 60)) % (1000 * 60)) / 1000

        val formattedTimeTotal = buildString {
            if (hoursTotal > 0) {
                append("$hoursTotal ${if (hoursTotal.toInt() == 1) "hour" else "hours"}, ")
            }
            if (minutesTotal > 0 || hoursTotal > 0) {
                append("$minutesTotal ${if (minutesTotal.toInt() == 1) "minute" else "minutes"}, ")
            }
            append("$secondsTotal ${if (secondsTotal.toInt() == 1) "second" else "seconds"}")
        }

        return formattedTimeTotal
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

}