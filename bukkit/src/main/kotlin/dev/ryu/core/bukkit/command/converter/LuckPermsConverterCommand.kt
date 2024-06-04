package dev.ryu.core.bukkit.command.converter

import com.starlight.nexus.command.Command
import com.starlight.nexus.command.data.flag.Flag
//import net.luckperms.api.LuckPermsProvider
//import net.luckperms.api.node.NodeType
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import java.util.*

object LuckPermsConverterCommand {

    private var asked = false
    private var completed = false

    //private val LUCK_PERMS = LuckPermsProvider.get()
    private val WEIGHT_REGEX = Regex("[0-9]{1,3}")

    @Command(names = ["convert luckperms"], permission = "op", description = "Import LuckPerms data into the core system.")
    @JvmStatic
    fun luckperms(
        sender: CommandSender,
        @Flag(value = ["grants"])grants: Boolean
    ) {
        sender.sendMessage("${ChatColor.RED}The converter is currently out of service.")
        /*
        if (completed && !asked) {
            asked = true
            sender.sendMessage("${ChatColor.RED}You seem to have already tried converting ranks, execute this command if are sure you want to convert again.")
            return
        }

        var succeeded = 0
        var message = "Successfully converted "

        val time = measureTimeMillis {

            LUCK_PERMS.groupManager.loadedGroups.forEach {

                val rank = Rank(it.name)

                rank.weight = it.weight.asInt

                for (node in it.nodes) {

                    when (node.type) {
                        NodeType.PREFIX -> rank.prefix = node.key.replace("prefix.$WEIGHT_REGEX.","")
                        NodeType.PERMISSION -> rank.permissions.add(node.key)
                        NodeType.INHERITANCE -> rank.inherits.add(node.key.replace("group.",""))
                    }

                }

                val color = ChatColor.getLastColors(rank.prefix)

                if (color != null) {
                    rank.color = color
                }

                if (CoreAPI.rankManager.repository.update(rank)) {
                    succeeded++
                    CoreAPI.rankManager.cache[rank.id.toLowerCase()] = rank
                }

            }

            message += "${ChatColor.WHITE}$succeeded ${ChatColor.GREEN}rank${if (succeeded <= 1) "" else "s"}"

            succeeded = 0

            if (grants) {

                for (uuid in LUCK_PERMS.userManager.uniqueUsers.get()) {

                    val user = LUCK_PERMS.userManager.loadUser(uuid).get() ?: continue
                    val group = LUCK_PERMS.groupManager.getGroup(user.primaryGroup) ?: continue
                    val rank = CoreAPI.rankManager.findById(group.name) ?: continue

                    if (rank.isDefault() || rank.id == "Default") {
                        continue
                    }

                    succeeded = 0
                    CoreAPI.grantManager.grant(rank,uuid,UUID.fromString(Profile.CONSOLE_UUID),"Converted")
                }

            }

            message += " and ${ChatColor.WHITE}$succeeded ${ChatColor.GREEN}grants${if (succeeded <= 1) "" else "s"}"
        }

        asked = false
        completed = true
        sender.sendMessage("$message in ${ChatColor.WHITE}${time}ms${ChatColor.GREEN};")
         */
    }


    //sender.sendMessage("${ChatColor.RED}The importer is currently out of service.")
    /*
            val importFolder = File(Core.get().dataFolder, "import")

    if (!importFolder.exists()) {
        importFolder.mkdir()

        sender.sendMessage("${ChatColor.RED}The import folder doesn't exist, but it has been created.")
        return
    }

    val ranksFileJson = File(importFolder, "$jsonFile.json")

    if (!ranksFileJson.exists()) {
        sender.sendMessage("${ChatColor.RED}The JSON file you are looking for does not exist.")
        return
    }

    val jsonString = ranksFileJson.readText()
    val jsonObject = JSONObject(jsonString)
    val groupsObject = jsonObject.getJSONObject("groups")
    val usersObject = jsonObject.getJSONObject("users")
    val permissionsByGroup = mutableMapOf<String, MutableList<String>>()
    val prioritiesByGroup = mutableMapOf<String, MutableList<Int>>()
    val prefixesByGroup = mutableMapOf<String, MutableList<String>>()
    val displaysByGroup = mutableMapOf<String, MutableList<String>>()
    val grantsByUser = mutableMapOf<String, MutableMap<String, String>>()
    val groupNames = groupsObject.keys()
    val userUUIDs = usersObject.keys()

    while (userUUIDs.hasNext()) {
        val userUUID = userUUIDs.next()
        val userObject = usersObject.getJSONObject(userUUID)
        val nodesUsersArray = userObject.getJSONArray("nodes")
        val userDataMap = mutableMapOf<String, String>()
        val userPermissions = mutableListOf<String>()

        if (userObject.has("username")) {
            val username = userObject.getString("username")
            val primaryGroup = userObject.optString("primaryGroup")

            print(username)
            print(primaryGroup)

            for (i in 0 until  nodesUsersArray.length()) {
                val nodeObject = nodesUsersArray.getJSONObject(i)

                when(nodeObject.getString("type")) {
                    "permission" -> {
                        val key = nodeObject.getString("key")
                        userPermissions.add(key)
                    }
                }
            }

            for((userName, userPermission) in permissionsByGroup) {
                val profile = CoreAPI.profileManager.findById(UUID.fromString(userUUID))

                if (profile != null) {
                    profile.permissions.addAll(userPermission)

                    CoreAPI.profileManager.repository.update(profile)
                } else {
                    val newProfile = Profile(UUID.fromString(userUUID))
                    newProfile.name = userName
                    newProfile.permissions.addAll(userPermission)

                    CoreAPI.profileManager.repository.update(newProfile)
                    CoreAPI.profileManager.cache[newProfile.id] = newProfile
                }

                val rank = CoreAPI.rankManager.findById(primaryGroup)

                if (rank != null) {
                    GrantModule.grant(rank, UUID.fromString(userUUID), UUID.fromString(Profile.CONSOLE_UUID), "Imported Luckperms Grant", Long.MAX_VALUE)
                } else {
                    sender.sendMessage("${ChatColor.RED}No rank could be granted to ${ChatColor.WHITE}$username ${ChatColor.RED}because the rank ${ChatColor.WHITE}$primaryGroup ${ChatColor.RED}oes not exist.")
                }
            }

            userDataMap[userUUID] = primaryGroup
            grantsByUser[userUUID] = userDataMap
        } else {
            println("Username not found for user with UUID: $userUUID")
        }
    }

    while (groupNames.hasNext()) {
        val groupName = groupNames.next()
        val groupObject = groupsObject.getJSONObject(groupName)
        val nodesGroupArray = groupObject.getJSONArray("nodes")
        val groupPermissions = mutableListOf<String>()
        val groupPriority = mutableListOf<Int>()
        val groupPrefix = mutableListOf<String>()
        val groupDisplayName = mutableListOf<String>()

        for (i in 0 until nodesGroupArray.length()) {
            val nodeObject = nodesGroupArray.getJSONObject(i)

            when (nodeObject.getString("type")) {
                "permission" -> {
                    val key = nodeObject.getString("key")
                    groupPermissions.add(key)
                }
                "weight" -> {
                    val key = nodeObject.getString("key")
                    val weight = key.split(".").last().toInt()
                    groupPriority.add(weight)
                }
                "prefix" -> {
                    val key = nodeObject.getString("key")
                    val prefix = key.split(".").last().toString()
                    groupPrefix.add(prefix)
                }
                "display_name" -> {
                    val key = nodeObject.getString("key")
                    val displayName = key.split(".").last().toString()
                    groupDisplayName.add(displayName)
                }
            }
        }

        permissionsByGroup[groupName] = groupPermissions
        prioritiesByGroup[groupName] = groupPriority
        prefixesByGroup[groupName] = groupPrefix
        displaysByGroup[groupName] = groupDisplayName
    }

    for ((groupName, groupPermissions) in permissionsByGroup) {
        val groupPriority = prioritiesByGroup[groupName]?.firstOrNull() ?: 0
        val groupPrefix = prefixesByGroup[groupName]?.firstOrNull() ?: ""
        val groupDisplay = displaysByGroup[groupName]?.firstOrNull() ?: ""
        var colorCode = extractColorCode(groupDisplay)

        if (colorCode == null) {
            colorCode = "GRAY"
        }

        val ranks = CoreAPI.rankManager.findById(groupName)
        if (ranks == null) {
            val rankToCreate = Rank(groupName.capitalize())
            rankToCreate.display = groupDisplay
            rankToCreate.prefix = groupPrefix
            rankToCreate.weight = groupPriority
            rankToCreate.color = Color.convertToChatColorName(colorCode)
            rankToCreate.permissions.addAll(groupPermissions)

            CoreAPI.rankManager.repository.update(rankToCreate)
            CoreAPI.backendManager.getJupiter().sendPacket(Jupiter(Rank.RANK_CREATED, rankToCreate))
        }
    }

    /*
        grantsByUser.forEach { entry ->
        val userId = entry.key
        val userDataMap = grantsByUser[userId]

        if (userDataMap != null) {
            val username = userDataMap["username"]
            val primaryGroup = userDataMap["primaryGroup"]

            if (username != null && primaryGroup != null) {

                val rank = CoreAPI.rankManager.findById(primaryGroup.capitalize())

                if (rank == null) {
                    sender.sendMessage("${ChatColor.RED}Se intento dar el rango ${primaryGroup.capitalize()}, pero este no existe en la base de datos")
                    return
                }

                val targetUUID = UUID.fromString(userId)

                GrantModule.grant(rank,targetUUID,UUID.fromString(Profile.CONSOLE_UUID),"Importing LuckPerm Grant",Long.MAX_VALUE)
                sender.sendMessage("${ChatColor.GREEN}Granted ${ChatColor.WHITE}${username} ${Color.color(rank.display)}${ChatColor.GREEN} rank.")
            }
        }
    }
     */

    if (debug) {
        val selectedGroup = "owner"
        val priorityByGroup = prioritiesByGroup[selectedGroup]?.get(0) ?: 0
        val prefixByGroup = prefixesByGroup[selectedGroup]?.get(0) ?: ""
        val displayByGroup = displaysByGroup[selectedGroup]?.get(0) ?: ""
        var colorCode = extractColorCode(displayByGroup)

        if (colorCode == null) {
            colorCode = "GRAY"
        }

        sender.sendMessage("")
        sender.sendMessage("")
        sender.sendMessage("${ChatColor.YELLOW}Group: ${ChatColor.AQUA}$selectedGroup")
        sender.sendMessage("${ChatColor.YELLOW}Prefix: ${Color.color(prefixByGroup)}")
        sender.sendMessage("${ChatColor.YELLOW}Display: ${Color.color(displayByGroup)}")
        sender.sendMessage("${ChatColor.YELLOW}Color: ${ChatColor.valueOf(Color.convertToChatColorName(colorCode))}${Color.convertToChatColorName(colorCode)}")
        sender.sendMessage("${ChatColor.YELLOW}Priority: ${ChatColor.AQUA}$priorityByGroup")
        sender.sendMessage("${ChatColor.YELLOW}Permissions: ${ChatColor.GRAY}(${permissionsByGroup[selectedGroup]?.size}) ${ChatColor.WHITE}[${permissionsByGroup[selectedGroup]?.joinToString(", ")}]")

        if (sender is Player) {
            sender.playSound(Sound.NOTE_PLING, 1.5f, 0.25f)
        }
    }

    permissionsByGroup.clear()
    prioritiesByGroup.clear()
    prefixesByGroup.clear()
    displaysByGroup.clear()
    grantsByUser.clear()
     */

/*
    private fun extractColorCode(input: String): String? {
        val regex = "&[0-9a-fA-Fk-oK-OrR]"
        val matchResult = regex.toRegex().find(input)
        return matchResult?.value
    }
 */

}