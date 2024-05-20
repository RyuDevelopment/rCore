package dev.ryu.core.shared.backend.redis

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import dev.ryu.core.shared.backend.redis.jedis.CustomPoolConfig
import dev.t4yrn.jupiter.JupiterAPI
import dev.t4yrn.jupiter.type.RedisJupiterAPI
import redis.clients.jedis.JedisPool

/*
    * Author: T4yrn
    * Project: core
    * Date: 24/2/2024 - 01:39
*/

object RedisManager {

    var gson: Gson = GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create()

    var client: JedisPool
    var jupiter: JupiterAPI

    private val host = "127.0.0.1";
    private val port = 6379
    private val password = "foobared";
    private val channel = "core";

    init {
        if (isAuthenticationRequired()) {
            this.client = JedisPool(CustomPoolConfig(), this.host, this.port, 30000)
        } else {
            this.client = JedisPool(CustomPoolConfig(), this.host, this.port, 30000, this.password)
        }

        this.jupiter = RedisJupiterAPI(this.gson, this.client, this.channel, this.password)
    }

    private fun isAuthenticationRequired():Boolean {
        return this.password != ""
    }

}