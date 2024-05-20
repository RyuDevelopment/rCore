package dev.ryu.core.bukkit.command

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

object HeadCommand {

    @Command(
        names = ["head", "skull", "skullitem", "giveskull"],
        permission = "core.staff",
        description = "Spawn yourself a player's head"
    )
    @JvmStatic
    fun head(
        sender: Player,
        @Param(name = "name", defaultValue = "self") name: String
    ) {
        var name = name
        if (name == "self") {
            name = sender.name
        }
        val item = ItemStack(Material.SKULL_ITEM, 1, 3.toShort())
        val meta = item.itemMeta as SkullMeta
        meta.setOwner(name)
        item.setItemMeta(meta)
        sender.inventory.addItem(item)
        sender.sendMessage(ChatColor.GOLD.toString() + "You were given " + ChatColor.WHITE + name + ChatColor.GOLD + "'s head.")
    }

}