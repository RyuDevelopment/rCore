package dev.ryu.core.bukkit.parameter

import dev.ryu.core.shared.system.extra.tag.TagType
import com.starlight.nexus.command.data.parameter.ParameterType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/*
    * Author: T4yrn © 2024
    * Project: tags
    * Date: 10/3/2024 - 13:43
*/

class TagTypeParameterType : ParameterType<TagType> {

    override fun tabComplete(player: Player, flags: Set<String>, source: String): List<String> {
        val suggestion = mutableListOf<String>()

        TagType.entries.forEach {
            suggestion.add(it.name)
        }

        return suggestion
    }

    override fun transform(sender: CommandSender, source: String): TagType? {
        try {
            val toReturn = TagType.valueOf(source.toUpperCase())
            return toReturn
        } catch (e: IllegalArgumentException) {
            sender.sendMessage("${ChatColor.RED}There's no existing tag type with that name.")
            return null
        }
    }

}