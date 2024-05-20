package dev.ryu.core.bukkit.command.admin

import dev.ryu.core.bukkit.menu.tag.AdminTagMenu
import dev.ryu.core.bukkit.system.lang.Lang
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Tag
import dev.ryu.core.shared.system.extra.tag.TagType
import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.parameter.Param
import com.starlight.nexus.util.UnicodeUtil
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object AdminTagCommand {

    @Command(names = ["admintag editor"], description = "AdminTag command.", permission = "core.admin")
    @JvmStatic
    fun execute(
        sender: CommandSender
    ) {
        if (sender !is Player) {
            return
        }

        AdminTagMenu().openMenu(sender)

        sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
    }
    
    @Command(names = ["admintag create"], description = "Create's a new tag.", permission = "core.admin")
    @JvmStatic
    fun create(
        sender: CommandSender,
        @Param(name = "tag") tag: String,
        @Param(name = "type") type: TagType
    ) {
        if (dev.ryu.core.shared.CoreAPI.tagManager.findByName(tag) != null) {
            sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_ALREADY_EXISTS_ERROR.value.replace("{tag}", tag))
            return
        }

        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_CREATION_COMMAND.value.replace("{tag}", tag))

        val tagToCreate = Tag(tag)
        tagToCreate.name = tag
        tagToCreate.type = type.name.toUpperCase()
        tagToCreate.display = tag
        tagToCreate.permission = "tags.$tag"
        tagToCreate.priority = 0

        tagToCreate.save(true)

        dev.ryu.core.shared.CoreAPI.tagManager.tags[tagToCreate.name] = tagToCreate

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag delete"], description = "Delete's an existing tag.", permission = "core.admin")
    @JvmStatic
    fun delete(
        sender: CommandSender,
        @Param(name = "tag") tag: Tag
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_DELETE_COMMAND.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)))

        tag.delete()

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag type"], description = "Set's a new type for a tag.", permission = "core.admin")
    @JvmStatic
    fun type(
        sender: CommandSender,
        @Param(name = "tag") tag: Tag,
        @Param(name = "type") type: TagType
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_CHANGED_TYPE.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)).replace("{type}", type.name.toUpperCase()))

        tag.type = type.name.toUpperCase()
        tag.save(true)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag info", "admintag information"], description = "Get a tag's information.", permission = "core.admin")
    @JvmStatic
    fun information(
        sender: CommandSender,
        @Param(name = "tag") tag: Tag
    ) {
        val information = listOf(
            "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 35)}",
            "&6&lTag Information &7${UnicodeUtil.VERTICAL_LINE} ${tag.display}",
            "",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eName&7: &f${tag.name}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &eDisplay&7: &f${tag.display}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &ePriority&7: &f${tag.priority}",
            "&f  ${UnicodeUtil.SMALL_ARROW} &ePermission&7: &f${if (tag.permission == "") "&cNot set" else "&f${tag.permission}"}",
            "${ChatColor.GRAY}${ChatColor.STRIKETHROUGH}${StringUtils.repeat('-', 35)}"
        )

        information.forEach {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', it))
        }
    }

    @Command(names = ["admintag rename"], description = "Rename's an existing tag.", permission = "core.admin")
    @JvmStatic
    fun rename(
        sender: CommandSender,
        @Param(name = "tag") tag: Tag,
        @Param(name = "newName") newName: String
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_CHANGED_NAME.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)).replace("{newName}", newName))

        tag.name = newName
        tag.save(true)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag display"], description = "Set's a tag's display name.", permission = "core.admin")
    @JvmStatic
    fun display(
        sender: CommandSender,
        @Param(name = "tag") tag: Tag,
        @Param(name = "display") display: String
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_CHANGED_DISPLAY.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)).replace("{display}", ChatColor.translateAlternateColorCodes('&', display)))

        tag.display = display
        tag.save(true)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag priority"], description = "Set's a tag's priority.", permission = "core.admin")
    @JvmStatic
    fun priority(
         sender: CommandSender,
         @Param(name = "tag") tag: Tag,
         @Param(name = "priority") priority: Int
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_CHANGED_PRIORITY.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)).replace("{priority}", "$priority"))

        tag.priority = priority
        tag.save(true)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag permission"], description = "Set's a tag's permission.", permission = "core.admin")
    @JvmStatic
    fun permission(
        sender: CommandSender,
        @Param(name = "tag") tag: Tag,
        @Param(name = "permission") permission: String
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_CHANGED_PERMISSION.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)).replace("{permission}", permission))

        tag.permission = permission
        tag.save(true)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

    @Command(names = ["admintag playertag"], description = "Set's a tag's to player.", permission = "core.admin")
    @JvmStatic
    fun playertag(
        sender: CommandSender,
        @Param(name = "profile") profile: Profile,
        @Param(name = "tag") tag: Tag
    ) {
        sender.sendMessage(Lang.CORE_PREFIX.value + Lang.TAG_SET_PLAYER.value.replace("{tag}", ChatColor.translateAlternateColorCodes('&', tag.display)).replace("{player}", "${profile.name}"))

        profile.tag = tag.name
        dev.ryu.core.shared.CoreAPI.profileManager.repository.update(profile)

        if (sender is Player) {
            sender.playSound(sender.location, Sound.NOTE_PLING, 0.2F, 1.5F)
        }
    }

}