package dev.ryu.core.bukkit.listener.orbit

import com.google.gson.JsonObject
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Code
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import org.bukkit.Bukkit
import org.bukkit.ChatColor

/*
    * Author: T4yrn
    * Project: core
    * Date: 22/2/2024 - 16:00
*/

class CodeOrbitListener : OrbitListener {

    @Orbit(Code.CODE_REDEEMED)
    fun onRedeem(data: JsonObject) {

        val code = data["code"].asString
        val rank = dev.ryu.core.shared.CoreAPI.rankManager.findById(data["rank"].asString)!!
        val redeemer = data["redeemer"].asString

        Bukkit.getOnlinePlayers().forEach { toReturn ->
            toReturn.sendMessage("${ChatColor.YELLOW}$redeemer ${ChatColor.GREEN}has just redeemed code ${ChatColor.WHITE}'${code}' ${ChatColor.GREEN}for '${ChatColor.valueOf(rank.color)}${rank.id}${ChatColor.GREEN}', enjoy!")
        }
    }

}