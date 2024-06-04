package dev.ryu.core.bukkit.menu.report

import com.starlight.nexus.menu.ConfirmMenu
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.menu.button.impl.MenuButton
import com.starlight.nexus.util.Callback
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.UnicodeUtil
import dev.ryu.core.bukkit.util.protocol
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ReportModule
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.text.SimpleDateFormat
import java.util.*

class ReportMenu(
    val target: Profile
): PaginatedMenu() {

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}${target.name}'s"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->
            val reports = ReportModule.repository.findAllReportsById(target.id).sortedByDescending { it.reportedAt }

            var index = 0
            reports.forEach {report ->
                toReturn[index] = MenuButton()
                    .name("${ChatColor.GOLD}${Date(report.reportedAt)}")
                    .icon(Material.PAPER)
                    .lore(
                        arrayListOf<String>().also { toReturn ->
                            val formattedReportedAt = formatDate(report.reportedAt)

                            toReturn.add("")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reported By${ChatColor.GRAY}: ${ChatColor.WHITE}${report.sender}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reported${ChatColor.GRAY}: ${ChatColor.WHITE}${report.target}")
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reason${ChatColor.GRAY}: ${ChatColor.WHITE}${report.reason}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Server${ChatColor.GRAY}: ${ChatColor.AQUA}${report.server}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Reported At${ChatColor.GRAY}: ${ChatColor.WHITE}$formattedReportedAt")
                            toReturn.add("")
                            toReturn.add(Button.styleAction(ChatColor.GREEN, "CLICK", "to remove report from database."))
                        }
                    )
                    .action(ClickType.LEFT) {
                        ConfirmMenu("¿Sure?",
                            object : Callback<Boolean> {
                                override fun callback(callback: Boolean) {
                                    if (callback) {
                                        player.closeInventory()
                                        Shared.reportManager.remove(report)
                                        ReportMenu(target).openMenu(player)
                                    } else {
                                        player.closeInventory()
                                        ReportMenu(target).openMenu(player)
                                    }
                                }
                            },
                        ).openMenu(player)
                    }
                    .action(ClickType.RIGHT) {
                        ConfirmMenu("¿Sure?",
                            object : Callback<Boolean> {
                                override fun callback(callback: Boolean) {
                                    if (callback) {
                                        player.closeInventory()
                                        Shared.reportManager.remove(report)
                                        ReportMenu(target).openMenu(player)
                                    } else {
                                        player.closeInventory()
                                        ReportMenu(target).openMenu(player)
                                    }
                                }
                            },
                        ).openMenu(player)
                    }
                index++
            }

        }
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
        return mutableMapOf<Int, Button>().also { toReturn ->

            for (i in 0 .. 8) {
                toReturn[getSlot(i, 0)] = GlassButton(7)
                toReturn[getSlot(i, 4)] = GlassButton(7)
            }

            val currentGrant = Shared.grantManager.findBestRank(GrantModule.repository.findAllByPlayer(target.id))

            if (player.protocol <= 20) {
                toReturn[4] = MenuButton()
                    .name("${ChatColor.AQUA}${target.name}'s Profile")
                    .icon(Material.PAPER)
                    .amount(target.getTotalReports())
                    .lore(
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Id${ChatColor.GRAY}: ${ChatColor.WHITE}${this.target.id.toString().replace("-", "")}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Rank${ChatColor.GRAY}: ${Color.color(currentGrant.display)}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Connected${ChatColor.GRAY}: ${if (target.online) "${ChatColor.GREEN}Online" else "${ChatColor.RED}Offline"}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Total Reports:${ChatColor.GRAY}: ${ChatColor.AQUA}${target.getTotalReports()}")
                        }
                    )
            } else {
                toReturn[4] = MenuButton()
                    .name("${ChatColor.AQUA}${target.name}'s Profile")
                    .playerTexture(target.name)
                    .amount(target.getTotalReports())
                    .lore(
                        arrayListOf<String>().also { toReturn ->
                            toReturn.add("")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Id${ChatColor.GRAY}: ${ChatColor.WHITE}${this.target.id.toString().replace("-", "")}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Rank${ChatColor.GRAY}: ${Color.color(currentGrant.display)}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Connected${ChatColor.GRAY}: ${if (target.online) "${ChatColor.GREEN}Online" else "${ChatColor.RED}Offline"}")
                            toReturn.add(" ${ChatColor.WHITE}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Total Reports${ChatColor.GRAY}: ${ChatColor.AQUA}${target.getTotalReports()}")
                        }
                    )
            }
        }
    }

    override fun size(player: Player?): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player?): Int {
        return 27
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

}