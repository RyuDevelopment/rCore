package dev.ryu.core.shared.system.orbit

import com.google.gson.JsonObject
import dev.ryu.core.shared.system.Profile
import dev.t4yrn.jupiter.orbit.Orbit
import dev.t4yrn.jupiter.orbit.OrbitListener
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 09:47
*/

class ProfileOrbitListener : OrbitListener {

    @Orbit(Profile.PROFILE_NAME_UPDATE)
    fun onNameUpdate(data: JsonObject) {
        dev.ryu.core.shared.Shared.profileManager.updateId(UUID.fromString(data["_id"].asString),data["name"].asString)
    }

}