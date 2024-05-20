package dev.ryu.core.bukkit.system.webhook.data

import java.awt.Color

interface IWebHook {

    fun title(): String

    fun description(): MutableList<String>

    fun image(): String

    fun thumbnail(): String

    fun color(): Color

    fun url(): String

}