package dev.ryu.core.shared.system.extra

/*
    * Author: T4yrn
    * Project: core
    * Date: 19/2/2024 - 23:14
*/

interface IData {

    fun save(async: Boolean)

    fun load()

    fun delete()

}