package dev.ryu.core.shared.system.extra.grant

import dev.ryu.core.shared.system.module.GrantModule

/*
    * Author: T4yrn
    * Project: core
    * Date: 19/2/2024 - 23:16
*/

class GrantExpiryService : Runnable {

    override fun run() {

        val iterator = GrantModule.active.iterator()

        while (iterator.hasNext()) {

            val next = iterator.next()

            if (next.value.isEmpty()) {
                continue
            }

            val grantIterator = next.value.iterator()

            while (grantIterator.hasNext()) {

                val grant = grantIterator.next()

                if (grant.isPermanent()) {
                    continue
                }

                if (grant.isRemoved()) {
                    grantIterator.remove()
                    continue
                }

                if (!grant.isVoided()) {
                    continue
                }

                if (!GrantModule.active.containsKey(next.key)) {
                    iterator.remove()
                    continue
                }

                grantIterator.remove()

                GrantModule.setGrant(next.key,next.value)

                GrantModule.findProvider().ifPresent{it.onGrantExpire(next.key,grant)}
            }
        }
    }
}