package dev.ryu.core.bukkit.parameter

import dev.ryu.core.shared.system.Tag
import dev.ryu.core.shared.system.module.TagModule
import com.starlight.nexus.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn
    * Project: tags
    * Date: 13/2/2024 - 19:01
*/

class TagParameterType : ParameterType<Tag> {

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        val suggestion = mutableListOf<String>()

        TagModule.getTags().forEach {
            suggestion.add(it.name)
        }

        return suggestion
    }

    override fun transform(sender: CommandSender, source: String): Tag? {
        val toReturn = TagModule.findByName(source)

        if (toReturn == null) {
            sender.sendMessage("${ChatColor.RED}There's not existing tag with that name.")
            return  null
        }

        return toReturn
    }

}