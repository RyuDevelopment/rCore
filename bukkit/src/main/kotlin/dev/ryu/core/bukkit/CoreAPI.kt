package dev.ryu.core.bukkit

import dev.ryu.core.bukkit.manager.StaffModeManager
import dev.ryu.core.bukkit.system.reboot.RebootManager
import dev.ryu.core.bukkit.system.reboot.task.ServerRebootTask
import dev.ryu.core.shared.Shared
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.Profile
import dev.ryu.core.shared.system.Rank
import dev.ryu.core.shared.system.module.ProfileModule
import dev.ryu.core.shared.system.module.RankModule
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.util.*

object CoreAPI {

    ////////////////////
    //                //
    // PROFILE SYSTEM //
    //                //
    ////////////////////

    @JvmStatic val profileSystem = Shared.profileManager

    @JvmStatic
    fun getProfileById(uuid: UUID): Profile? {
        return profileSystem.findById(uuid)
    }

    @JvmStatic
    fun getProfileByName(name: String): Profile? {
        return profileSystem.findByName(name)
    }

    @JvmStatic
    fun getAllProfiles(): HashMap<UUID, Profile> {
        return profileSystem.cache
    }

    /////////////////
    //             //
    // RANK SYSTEM //
    //             //
    /////////////////

    @JvmStatic val rankSystem = Shared.rankManager

    @JvmStatic
    fun getRankByName(name: String): Rank? {
        return rankSystem.findById(name)
    }

    @JvmStatic
    fun getAllRanks(): HashMap<String, Rank> {
        return rankSystem.cache
    }

    //////////////////
    //              //
    // GRANT SYSTEM //
    //              //
    //////////////////

    @JvmStatic val grantSystem = Shared.grantManager

    @JvmStatic
    fun getFindBestGrant(grants: MutableSet<Grant>): Rank {
        return grantSystem.findBestRank(grants)
    }

    ////////////////
    //            //
    // TAG SYSTEM //
    //            //
    ////////////////

    @JvmStatic val tagSystem = Shared.tagManager

    @JvmStatic
    fun getActivePlayerTag(player: Player): String {
        val profile = ProfileModule.findById(player.uniqueId)!!

        if (profile.tag.isNullOrBlank()) {
            return ""
        }

        val foundTag = tagSystem.findByName(profile.tag!!)

        if (foundTag?.display != null) {
            return ChatColor.translateAlternateColorCodes('&', foundTag.display)
        }

        return ""
    }

    ///////////////////////
    //                   //
    // STAFF MODE SYSTEM //
    //                   //
    ///////////////////////

    @JvmStatic
    fun enableStaffMode(player: Player) {
        StaffModeManager.enableStaffMode(player)
    }

    @JvmStatic
    fun disableStaffMode(player: Player) {
        StaffModeManager.disableStaffMode(player)
    }

    @JvmStatic
    fun enableVanish(staff: Player) {
        StaffModeManager.enableVanish(staff)
    }

    @JvmStatic
    fun disableVanish(staff: Player) {
        StaffModeManager.disableVanish(staff)
    }

    @JvmStatic
    fun freezePlayer(sender: Player, target: Player) {
        StaffModeManager.freezePlayer(sender, target)
    }

    @JvmStatic
    fun unFreezePlayer(sender: Player, target: Player) {
        StaffModeManager.unFreezePlayer(sender, target)
    }

    @JvmStatic
    fun isPlayerFrozen(player: Player): Boolean {
        return StaffModeManager.isPlayerFrozen(player)
    }

    @JvmStatic
    fun hasStaffModeEnabled(player: Player): Boolean {
        return StaffModeManager.hasStaffModeEnabled(player)
    }

    @JvmStatic
    fun hasStaffVanished(player: Player) : Boolean {
        return StaffModeManager.hasStaffVanished(player)
    }

    @JvmStatic
    fun getAllStaffInStaffMode(): List<Player> {
        return StaffModeManager.getAllStaffInStaffMode()
    }

    @JvmStatic
    fun getAllStaffVanished(): List<Player> {
        return StaffModeManager.getAllStaffVanished()
    }

    ///////////////////
    //               //
    // REBOOT SYSTEM //
    //               //
    ///////////////////

    @JvmStatic
    fun reboot(time: Long, reason: String) {
        RebootManager.reboot(time, reason)
    }

    @JvmStatic
    fun isRebooting(): Boolean {
        return RebootManager.isRebooting()
    }

    @JvmStatic
    fun getRebootSecondsRemaining(): Int {
        return RebootManager.getRebootSecondsRemaining()
    }

    @JvmStatic
    fun cancelReboot() {
        RebootManager.cancelReboot()
    }

}