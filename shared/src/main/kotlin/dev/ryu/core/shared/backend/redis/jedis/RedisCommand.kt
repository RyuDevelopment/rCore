package dev.ryu.core.shared.backend.redis.jedis

import redis.clients.jedis.Jedis

/*
    * Author: T4yrn
    * Project: core
    * Date: 21/2/2024 - 00:39
*/

interface RedisCommand<T> {

    fun execute(jedis: Jedis):T

}