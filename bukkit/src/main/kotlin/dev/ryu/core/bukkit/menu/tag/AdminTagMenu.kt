package dev.ryu.core.bukkit.menu.tag

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import dev.ryu.core.bukkit.Core
import dev.ryu.core.bukkit.menu.tag.editor.AdminTagEditorMenu
import dev.ryu.core.bukkit.prompt.tag.TagCreatePrompt
import dev.ryu.core.shared.CoreAPI
import dev.ryu.core.shared.system.Tag
import dev.ryu.core.shared.system.extra.tag.TagType
import com.starlight.nexus.menu.PaginatedMenu
import com.starlight.nexus.menu.button.Button
import com.starlight.nexus.menu.button.impl.GlassButton
import com.starlight.nexus.util.UnicodeUtil
import org.apache.commons.lang3.StringUtils
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.conversations.ConversationFactory
import org.bukkit.conversations.NullConversationPrefix
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType

/*
    * Author: T4yrn © 2024
    * Project: tags
    * Date: 10/3/2024 - 14:54
*/

class AdminTagMenu  : PaginatedMenu() {

    var tagType: TagType = TagType.SYMBOLS
    var nextType: String = if (tagType == TagType.SYMBOLS) "Countries" else ""
    private val types: MutableList<TagType> = arrayListOf()
    private var currentTypeIndex: Int = 0

    init {
        isAutoUpdate = true
        isUpdateAfterClick = true

        types.add(TagType.SYMBOLS)
        types.add(TagType.COUNTRIES)
        types.add(TagType.PARTNERS)

        setInitialFilterState()
    }

    override fun getPrePaginatedTitle(player: Player): String {
        return "${ChatColor.GRAY}Tags - ${StringUtils.capitalize(tagType.name.toLowerCase())}"
    }

    override fun getAllPagesButtons(player: Player): MutableMap<Int, Button> {
        val toReturn : MutableMap<Int, Button> = Maps.newHashMap()

        val tags = getAllTags()

        var symbolsIndex = 0
        var countriesIndex = 0
        var partnersIndex = 0

        for (i in tags.indices) {
            val currentTag = tags[i]

            when (tagType) {
                TagType.SYMBOLS -> {
                    when(currentTag.type) {
                        "SYMBOLS" -> {
                            toReturn[symbolsIndex] = object : Button() {
                                override fun getName(player: Player): String {
                                    return ChatColor.translateAlternateColorCodes('&', currentTag.display)
                                }

                                override fun getDescription(player: Player): MutableList<String> {
                                    return mutableListOf<String>().also { toReturn ->
                                        toReturn.add("")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Priority${ChatColor.GRAY}: ${ChatColor.AQUA}${currentTag.priority}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Display${ChatColor.GRAY}: ${ChatColor.WHITE}${currentTag.display}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Type${ChatColor.GRAY}: ${ChatColor.WHITE}${StringUtils.capitalize(currentTag.type!!.toLowerCase())}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Permission${ChatColor.GRAY}: ${ChatColor.WHITE}${currentTag.permission}")
                                        toReturn.add("")
                                        toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to manage tag.")
                                        toReturn.add("${ChatColor.RED}${ChatColor.BOLD}RIGHT-CLICK ${ChatColor.RED}to delete tag.")
                                    }
                                }

                                override fun getMaterial(player: Player): Material {
                                    return Material.NAME_TAG
                                }

                                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                                    if (clickType == ClickType.LEFT) {
                                        player.closeInventory()
                                        AdminTagEditorMenu(currentTag).openMenu(player)
                                    } else if (clickType == ClickType.RIGHT) {
                                        /*
                                        ConfirmMenu("¿Sure?",
                                        ) { callback ->
                                            if (callback) {
                                                player.sendMessage("${ChatColor.GREEN}Tag '${currentTag.name}' is now successfully deleted.")
                                                currentTag.delete()
                                                player.closeInventory()
                                                AdminTagMenu().openMenu(player)
                                            } else {
                                                player.closeInventory()
                                                AdminTagMenu().openMenu(player)
                                            }
                                        }.openMenu(player)
                                        */
                                    }
                                }
                            }
                            symbolsIndex++
                        }
                    }
                }
                TagType.COUNTRIES -> {
                    when(currentTag.type) {
                        "COUNTRIES" -> {
                            toReturn[countriesIndex] = object : Button() {
                                override fun getName(player: Player): String {
                                    return ChatColor.translateAlternateColorCodes('&', currentTag.display)
                                }

                                override fun getDescription(player: Player): MutableList<String> {
                                    return mutableListOf<String>().also { toReturn ->
                                        toReturn.add("")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Priority${ChatColor.GRAY}: ${ChatColor.AQUA}${currentTag.priority}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Display${ChatColor.GRAY}: ${ChatColor.WHITE}${currentTag.display}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Type${ChatColor.GRAY}: ${ChatColor.WHITE}${StringUtils.capitalize(currentTag.type!!.toLowerCase())}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Permission${ChatColor.GRAY}: ${ChatColor.WHITE}${currentTag.permission}")
                                        toReturn.add("")
                                        toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to manage tag.")
                                        toReturn.add("${ChatColor.RED}${ChatColor.BOLD}RIGHT-CLICK ${ChatColor.RED}to delete tag.")
                                    }
                                }

                                override fun getMaterial(player: Player): Material {
                                    return Material.NAME_TAG
                                }

                                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                                    if (clickType == ClickType.LEFT) {
                                        player.closeInventory()
                                        AdminTagEditorMenu(currentTag).openMenu(player)
                                    } else if (clickType == ClickType.RIGHT) {
/*
                                        ConfirmMenu("¿Sure?",
                                        ) { callback ->
                                            if (callback) {
                                                player.sendMessage("${ChatColor.GREEN}Tag '${currentTag.name}' is now successfully deleted.")
                                                currentTag.delete()
                                                player.closeInventory()
                                                AdminTagMenu().openMenu(player)
                                            } else {
                                                player.closeInventory()
                                                AdminTagMenu().openMenu(player)
                                            }
                                        }.openMenu(player)
 */
                                    }
                                }
                            }
                            countriesIndex++
                        }
                    }
                }
                TagType.PARTNERS -> {
                    when(currentTag.type) {
                        "PARTNERS" -> {
                            toReturn[partnersIndex] = object : Button() {
                                override fun getName(player: Player): String {
                                    return ChatColor.translateAlternateColorCodes('&', currentTag.display)
                                }

                                override fun getDescription(player: Player): MutableList<String> {
                                    return mutableListOf<String>().also { toReturn ->
                                        toReturn.add("")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Priority${ChatColor.GRAY}: ${ChatColor.AQUA}${currentTag.priority}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Display${ChatColor.GRAY}: ${ChatColor.WHITE}${currentTag.display}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Type${ChatColor.GRAY}: ${ChatColor.WHITE}${StringUtils.capitalize(currentTag.type!!.toLowerCase())}")
                                        toReturn.add(" ${ChatColor.GRAY}${UnicodeUtil.VERTICAL_LINE} ${ChatColor.YELLOW}Permission${ChatColor.GRAY}: ${ChatColor.WHITE}${currentTag.permission}")
                                        toReturn.add("")
                                        toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}LEFT-CLICK ${ChatColor.GREEN}to manage tag.")
                                        toReturn.add("${ChatColor.RED}${ChatColor.BOLD}RIGHT-CLICK ${ChatColor.RED}to delete tag.")
                                    }
                                }

                                override fun getMaterial(player: Player): Material {
                                    return Material.NAME_TAG
                                }

                                override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                                    if (clickType == ClickType.LEFT) {
                                        player.closeInventory()
                                        AdminTagEditorMenu(currentTag).openMenu(player)
                                    } else if (clickType == ClickType.RIGHT) {
/*
                                        ConfirmMenu("¿Sure?",
                                        ) { callback ->
                                            if (callback) {
                                                player.sendMessage("${ChatColor.GREEN}Tag '${currentTag.name}' is now successfully deleted.")
                                                currentTag.delete()
                                                player.closeInventory()
                                                AdminTagMenu().openMenu(player)
                                            } else {
                                                player.closeInventory()
                                                AdminTagMenu().openMenu(player)
                                            }
                                        }.openMenu(player)
 */
                                    }
                                }
                            }
                            partnersIndex++
                        }
                    }
                }
            }
        }

        return toReturn
    }

    override fun getGlobalButtons(player: Player): MutableMap<Int, Button> {
        val toReturn : MutableMap<Int, Button> = Maps.newHashMap()

        for (i in 0 until 9) {
            toReturn[getSlot(i, 0)] = GlassButton(7)
            toReturn[getSlot(i, 4)] = GlassButton(7)
        }

        toReturn[4] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.LIGHT_PURPLE}Create new Tag!"
            }

            override fun getDescription(player: Player): MutableList<String> {
                return mutableListOf<String>().also { toReturn ->
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}CLICK ${ChatColor.GREEN}to create new tag.")
                }
            }

            override fun getMaterial(player: Player): Material {
                return Material.EMERALD
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                player.closeInventory()
                player.beginConversation(ConversationFactory(dev.ryu.core.bukkit.Core.get()).withModality(true).withPrefix(NullConversationPrefix()).withFirstPrompt(
                    TagCreatePrompt(player)
                ).withEscapeSequence("/no").withLocalEcho(false).withTimeout(25).thatExcludesNonPlayersWithMessage("Go away evil console!").buildConversation(player))
            }
        }

        toReturn[40] = object : Button() {
            override fun getName(player: Player): String {
                return "${ChatColor.GOLD}Filtered by${ChatColor.GRAY}: ${ChatColor.WHITE}${StringUtils.capitalize(tagType.name.toLowerCase())}"
            }

            override fun getDescription(player: Player): MutableList<String> {
                return mutableListOf<String>().also { toReturn ->
                    toReturn.add("${ChatColor.GRAY}Sort the tags from Type");
                    toReturn.add("");
                    toReturn.add("${ChatColor.GOLD}Current${ChatColor.GRAY}: ${ChatColor.WHITE}${StringUtils.capitalize(tagType.name.toLowerCase())}")
                    toReturn.add("${ChatColor.GOLD}Next${ChatColor.GRAY}: ${ChatColor.WHITE}$nextType");
                    toReturn.add("")
                    toReturn.add("${ChatColor.GREEN}${ChatColor.BOLD}CLICK ${ChatColor.GREEN}to switch filter.")
                }
            }

            override fun getMaterial(player: Player): Material {
                return Material.HOPPER
            }

            override fun clicked(player: Player, slot: Int, clickType: ClickType) {
                if (types.isNotEmpty()) {
                    val currentIndex = types.indexOf(tagType)
                    currentTypeIndex = (currentIndex + 1) % types.size
                    tagType = types[currentTypeIndex]

                    val nextIndex = (currentTypeIndex + 1) % types.size
                    nextType = StringUtils.capitalize(types[nextIndex].name.toLowerCase())
                }
            }
        }

        return toReturn
    }

    override fun size(player: Player): Int {
        return 45
    }

    override fun getMaxItemsPerPage(player: Player): Int {
        return 27
    }


    private fun getAllTags(): List<Tag> {
        val allTags = ArrayList(dev.ryu.core.shared.CoreAPI.tagManager.tags.values)
        val tags: MutableList<Tag> = Lists.newArrayList()
        tags.addAll(allTags)
        tags.sortWith { o1, o2 -> o2.priority - o1.priority }
        return tags
    }

    private fun setInitialFilterState() {
        tagType = TagType.SYMBOLS
        currentTypeIndex = types.indexOf(TagType.SYMBOLS)
        nextType = if (tagType == TagType.SYMBOLS) "Countries" else StringUtils.capitalize(types[currentTypeIndex].name.toLowerCase())
    }

}