package dev.ryu.core.bukkit.prompt.server.info

object CustomServerInfo {

    var serverName: String? = null
    var spigot: String? = null
    var maxPlayers: Int = 250
    var port: Int? = null

    var spigotsAvailables: MutableList<String> = mutableListOf()
    var pluginsAvailables: HashMap<String, Boolean> = hashMapOf()
    var pluginsDefault: HashMap<String, Boolean> = hashMapOf(
        "Core" to true,
        "WorldEdit" to true,
        "ViaVersion" to true,
        "ProtocolLib" to true,
        "Spark" to true
    )

}