package dev.ryu.core.bukkit.menu.rank.editor.element

import com.google.gson.JsonObject
import dev.ryu.core.bukkit.menu.rank.editor.RankCodes
import dev.ryu.core.bukkit.system.webhook.impl.CodesLogWebhook
import dev.ryu.core.shared.system.Code
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.CodeModule
import dev.ryu.core.shared.system.module.GrantModule
import dev.ryu.core.shared.system.module.ProfileModule
import com.starlight.nexus.menu.ConfirmMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.Callback
import com.starlight.nexus.util.Color
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.util.time.TimeUtil
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import java.text.SimpleDateFormat
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 23/2/2024 - 01:08
*/

class CodeButton(
    private val code: Code,
    private val rank: Rank
) : Button() {

    override fun getName(player: Player): String {
        return "${ChatColor.AQUA}${code.code}"
    }

    override fun getDescription(player: Player): List<String> {
        return arrayListOf<String>().also { toReturn ->

            var creator: Profile? = null
            var creatorGrant: Rank? = null

            if (code.createdBy != UUID.fromString(Profile.CONSOLE_UUID)) {
                creator = ProfileModule.findById(code.createdBy)!!
                creatorGrant = GrantModule.findBestRank(creator.id)
            }

            toReturn.add("")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Rank${ChatColor.GRAY}: ${ChatColor.WHITE}${Color.color(rank.display)}")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Duration${ChatColor.GRAY}: ${ChatColor.WHITE}${if (code.rankDuration == 0L) { "${ChatColor.WHITE}Permanent" } else { TimeUtil.formatIntoDetailedString(code.rankDuration) }}")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Created By${ChatColor.GRAY}: ${ChatColor.WHITE}${if (code.createdBy == UUID.fromString(Profile.CONSOLE_UUID)) "${ChatColor.DARK_RED}${ChatColor.BOLD}Console" else "${ChatColor.valueOf(creatorGrant!!.color)}${creator!!.name}"}")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Created At${ChatColor.GRAY}: ${ChatColor.WHITE}${code.getCreatedAt()}")
            if (code.isRedeemed()) {
                val redeemer = ProfileModule.findById(code.redeemBy!!)!!
                val redeemerGrant = GrantModule.findBestRank(redeemer.id)

                toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Redeemed By${ChatColor.GRAY}: ${ChatColor.WHITE}${ChatColor.valueOf(redeemerGrant.color)}${redeemer.name}")
                toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Redeemed At${ChatColor.GRAY}: ${ChatColor.WHITE}${code.getRedeemedAt()}")
                toReturn.add("")
            } else {
                toReturn.add("")
                toReturn.add(styleAction(ChatColor.GREEN, "LEFT-CLICK", "to receive code"))
            }
            toReturn.add(styleAction(ChatColor.RED, "RIGHT-CLICK", "to delete code"))
        }
    }

    override fun getMaterial(player: Player): Material {
        if (code.isRedeemed()) return Material.BEDROCK

        return Material.PAPER
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        player.closeInventory()
        if (clickType == ClickType.LEFT && !code.isRedeemed()) {
            player.sendMessage("${ChatColor.GREEN}Here's the code: ${ChatColor.WHITE}${code.code}")
        } else if (clickType == ClickType.RIGHT) {
            ConfirmMenu("¿Sure?",
                object : Callback<Boolean> {
                    override fun callback(callback: Boolean) {
                        if (callback) {
                            player.closeInventory()
                            CodeModule.remove(code)
                            RankCodes(rank).openMenu(player)

                            if (dev.ryu.core.bukkit.Core.get().webhookEnabled) {
                                val data = JsonObject()
                                data.addProperty("rank", rank.id)
                                data.addProperty("deletedBy", player.name)

                                val date = Date(System.currentTimeMillis())
                                val format = SimpleDateFormat("EEEE MMMM d h:mm a yyyy", Locale.getDefault())
                                val finalDate = format.format(date)

                                data.addProperty("code", code.code)
                                data.addProperty("deletedAt", finalDate)
                                data.addProperty("senderUUID", player.uniqueId.toString())

                                CodesLogWebhook.onCodeDeleted(data)
                            }
                        } else {
                            player.closeInventory()
                            RankCodes(rank).openMenu(player)
                        }
                    }
                },
            ).openMenu(player)
        }
    }

}