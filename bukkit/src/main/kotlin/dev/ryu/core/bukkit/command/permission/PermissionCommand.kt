package dev.ryu.core.bukkit.command.permission

import dev.ryu.core.bukkit.manager.PermissionManager
import dev.ryu.core.shared.system.Profile
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 01:38
*/

object PermissionCommand {

    @Command(names = ["permission add"], description = "Add a permission to a user's account!", permission = "core.admin")
    @JvmStatic
    fun add(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "permission") permission: String
    ) {
        if (target.permissions.any{it.equals(permission,true)}) {
            sender.sendMessage("${target.name} ${ChatColor.RED}already has the permission ${ChatColor.WHITE}$permission${ChatColor.RED}.")
            return
        }

        target.permissions.add(permission.toLowerCase())

        if (!PermissionManager.update(target,permission.toLowerCase(),false)) {
            sender.sendMessage("${ChatColor.RED}There was an issue updating ${target.name}${ChatColor.RED}'s permissions.")
            return
        }

        sender.sendMessage("${ChatColor.GREEN}Granted ${target.name} ${ChatColor.GREEN}permission ${ChatColor.WHITE}$permission${ChatColor.GREEN}.")
    }

    @Command(names = ["permission remove"], description = "Remove a permission to a user's account!", permission = "core.admin")
    @JvmStatic
    fun remove(
        sender: CommandSender,
        @Param(name = "profile") target: Profile,
        @Param(name = "permission") permission: String
    ) {
        if (target.permissions.none{it.equals(permission,true)}) {
            sender.sendMessage("${target.name} ${ChatColor.RED}does not have the permission ${ChatColor.WHITE}$permission${ChatColor.RED}.")
            return
        }

        target.permissions.remove(permission.toLowerCase())

        if (!PermissionManager.update(target,permission.toLowerCase(),true)) {
            sender.sendMessage("${ChatColor.RED}There was an issue updating ${target.name}${ChatColor.RED}'s permissions.")
            return
        }

        sender.sendMessage("${ChatColor.RED}Removed permission ${ChatColor.WHITE}$permission${ChatColor.RED} for ${target.name}${ChatColor.RED}.")
    }

}