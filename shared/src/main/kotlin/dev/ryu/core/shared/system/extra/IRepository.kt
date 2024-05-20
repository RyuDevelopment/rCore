package dev.ryu.core.shared.system.extra

/*
    * Author: T4yrn
    * Project: core
    * Date: 21/2/2024 - 00:27
*/

interface IRepository<K,V> {

    fun pull():Map<K,V>

    fun update(value: V):Boolean

    fun delete(value: V):Boolean

    fun findById(id: K):V?

}
