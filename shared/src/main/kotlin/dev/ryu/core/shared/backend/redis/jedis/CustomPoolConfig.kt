package dev.ryu.core.shared.backend.redis.jedis

import org.apache.commons.pool2.impl.GenericObjectPoolConfig

/*
    * Author: T4yrn
    * Project: core
    * Date: 21/2/2024 - 00:39
*/
class CustomPoolConfig : GenericObjectPoolConfig<Any>() {

    init {
        testWhileIdle = true
        numTestsPerEvictionRun = -1
        minEvictableIdleTimeMillis = 60000L
        timeBetweenEvictionRunsMillis = 30000L
    }

}