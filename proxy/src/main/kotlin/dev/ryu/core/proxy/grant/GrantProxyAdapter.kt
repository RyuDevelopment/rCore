package dev.ryu.core.proxy.grant

import dev.ryu.core.proxy.Core
import dev.ryu.core.shared.system.Grant
import dev.ryu.core.shared.system.module.GrantModule
import java.util.*

/*
    * Author: T4yrn
    * Project: core
    * Date: 26/2/2024 - 19:05
*/

class GrantProxyAdapter(private val instance: Core) : GrantModule.GrantAdapter {

    override fun onGrantApply(uuid: UUID,grant: Grant) {
        this.instance.proxy.getPlayer(uuid).also{this.instance.permissionHandler.update(it)}
    }

    override fun onGrantChange(uuid: UUID,grant: Grant) {
        this.instance.proxy.getPlayer(uuid).also{this.instance.permissionHandler.update(it)}
    }

    override fun onGrantExpire(uuid: UUID,grant: Grant) {
        this.instance.proxy.getPlayer(uuid).also{this.instance.permissionHandler.update(it)}
    }

    override fun onGrantRemove(uuid: UUID,grant: Grant) {
        this.instance.proxy.getPlayer(uuid).also{this.instance.permissionHandler.update(it)}
    }

}