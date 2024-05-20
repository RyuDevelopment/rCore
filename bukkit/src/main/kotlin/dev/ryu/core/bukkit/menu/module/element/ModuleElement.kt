package dev.ryu.core.bukkit.menu.module.element

import dev.ryu.core.linker.manager.ModuleManager
import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.CoreAPI.backendManager
import dev.ryu.core.shared.CoreAPI.codeManager
import dev.ryu.core.shared.CoreAPI.grantManager
import dev.ryu.core.shared.CoreAPI.profileManager
import dev.ryu.core.shared.CoreAPI.punishmentManager
import dev.ryu.core.shared.CoreAPI.rankManager
import com.starlight.nexus.util.UnicodeUtil
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.util.TextSplitter
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn © 2024
    * Project: core
    * Date: 24/April - 2:53 PM
*/

class ModuleElement(
    val module: IModule,
): Button() {

    override fun getName(p0: Player): String {
        return "${ChatColor.AQUA}${module.moduleName()}"
    }

    override fun getDescription(p0: Player): MutableList<String> {
        arrayListOf<String>().also { toReturn ->
            toReturn.add("")
            toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Current${ChatColor.GRAY}: ${ if (ModuleManager.isModuleEnabled(module)) "${ChatColor.GREEN}Enabled" else "${ChatColor.RED}Disabled"}")
            toReturn.add("")

            val modulesToIgnore = listOf(
                backendManager,
                rankManager,
                codeManager,
                grantManager,
                punishmentManager,
                profileManager
            )

            if (modulesToIgnore.any { it == module }) {
                toReturn.addAll(TextSplitter.split(text = "This module is essential and cannot be disabled."))
            } else if (!ModuleManager.isModuleEnabled(module)) {
                toReturn.add(styleAction(ChatColor.GREEN, "CLICK", "to enable module."))
            } else {
                toReturn.add(styleAction(ChatColor.RED, "CLICK", "to disable module."))
            }
            return toReturn
        }
    }

    override fun getMaterial(p0: Player): Material {
        return Material.PAPER
    }

    override fun clicked(player: Player, slot: Int, clickType: ClickType) {
        val modulesToIgnore = listOf(
            backendManager,
            rankManager,
            codeManager,
            grantManager,
            punishmentManager,
            profileManager
        )

        if (modulesToIgnore.any { it == module }) {
            player.sendMessage("${ChatColor.RED}You can't disable a default module!")
            return
        }

        if (!ModuleManager.isModuleEnabled(module)) {
            ModuleManager.enableModule(module)
            player.sendMessage("${ChatColor.GREEN}Successfully enabled ${ChatColor.WHITE}'${module.moduleName()}'${ChatColor.GREEN} module!")
        } else {
            ModuleManager.disableModule(module)
            player.sendMessage("${ChatColor.RED}Successfully disabled ${ChatColor.WHITE}'${module.moduleName()}'${ChatColor.RED} module!")
        }
    }

}