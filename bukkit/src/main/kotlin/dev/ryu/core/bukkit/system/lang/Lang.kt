package dev.ryu.core.bukkit.system.lang

import com.starlight.nexus.util.UnicodeUtil
import org.bukkit.ChatColor

/*
    * Author: T4yrn
    * Project: core
    * Date: 20/2/2024 - 13:20
*/

enum class Lang(val value: String) {

    CORE_PREFIX("${ChatColor.GOLD}[rCore] "),
    FRIEND_PREFIX("${ChatColor.GOLD}${ChatColor.BOLD}Friends ${ChatColor.GRAY}${UnicodeUtil.DOUBLE_ARROW_RIGHT} "),

    //Rank
    RANK_CREATION_COMMAND("${ChatColor.YELLOW}You've ${ChatColor.GREEN}created ${ChatColor.YELLOW}the rank ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_DELETE_COMMAND("${ChatColor.YELLOW}You've ${ChatColor.RED}deleted ${ChatColor.YELLOW}the rank ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_ALREADY_EXISTS_ERROR("${ChatColor.RED}There's an existing rank with that name."),
    RANK_PERMISSION_ADDED("${ChatColor.YELLOW}You've ${ChatColor.GREEN}added ${ChatColor.WHITE}'${ChatColor.WHITE}{permission}${ChatColor.WHITE}' ${ChatColor.YELLOW}permission to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_PERMISSION_REMOVED("${ChatColor.YELLOW}You've ${ChatColor.RED}removed ${ChatColor.WHITE}'${ChatColor.WHITE}{permission}${ChatColor.WHITE}' ${ChatColor.YELLOW}permission from ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_INHERIT_ADDED("${ChatColor.YELLOW}You've ${ChatColor.GREEN}added ${ChatColor.WHITE}'${ChatColor.WHITE}{inherit}${ChatColor.WHITE}' ${ChatColor.YELLOW}inherit to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_INHERIT_REMOVED("${ChatColor.YELLOW}You've ${ChatColor.RED}removed ${ChatColor.WHITE}'${ChatColor.WHITE}{inherit}${ChatColor.WHITE}' ${ChatColor.YELLOW}inherit from ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_NAME("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}'s name to ${ChatColor.WHITE}{newName}${ChatColor.YELLOW}."),
    RANK_CHANGED_DISPLAY("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}'s display to ${ChatColor.WHITE}{display}${ChatColor.YELLOW}."),
    RANK_CHANGED_PREFIX("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{prefix}${ChatColor.YELLOW}'s prefix to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_COLOR("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{color}${ChatColor.YELLOW}'s color to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_WEIGHT("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{weight}${ChatColor.YELLOW}'s weight to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_DISCORD_ID("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}'s discord id to ${ChatColor.WHITE}{discordId}${ChatColor.YELLOW}."),
    RANK_CHANGED_STAFF_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} staff boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_HIDDEN_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} hidden boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_MEDIA_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} media boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_DEFAULT_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} default boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_GRANTABLE_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} grantable boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),
    RANK_CHANGED_DONATOR_STATUS("${ChatColor.YELLOW}You've set ${ChatColor.WHITE}{status}${ChatColor.YELLOW} donator boolean to ${ChatColor.WHITE}{rank}${ChatColor.YELLOW}."),

    TAG_CREATION_COMMAND("${ChatColor.YELLOW}You've ${ChatColor.GREEN}created ${ChatColor.YELLOW}the tag ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}."),
    TAG_DELETE_COMMAND("${ChatColor.YELLOW}You've ${ChatColor.RED}deleted ${ChatColor.YELLOW}the tag ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}."),
    TAG_ALREADY_EXISTS_ERROR("${ChatColor.RED}There's an existing tag with that name."),
    TAG_CHANGED_NAME("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}'s name to ${ChatColor.WHITE}{newName}${ChatColor.YELLOW}."),
    TAG_CHANGED_TYPE("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}'s type to ${ChatColor.WHITE}{type}${ChatColor.YELLOW}."),
    TAG_CHANGED_DISPLAY("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}'s display to ${ChatColor.WHITE}{display}${ChatColor.YELLOW}."),
    TAG_CHANGED_PRIORITY("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}'s priority to ${ChatColor.WHITE}{priority}${ChatColor.YELLOW}."),
    TAG_CHANGED_PERMISSION("${ChatColor.YELLOW}You've ${ChatColor.GREEN}update ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}'s permission to ${ChatColor.WHITE}{permission}${ChatColor.YELLOW}."),
    TAG_SET_PLAYER("${ChatColor.YELLOW}You've ${ChatColor.GREEN}set ${ChatColor.WHITE}{tag}${ChatColor.YELLOW}'s to player ${ChatColor.WHITE}{player}${ChatColor.YELLOW}."),

}