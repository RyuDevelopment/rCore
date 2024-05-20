package dev.ryu.core.shared.system.module

import dev.ryu.core.linker.data.IModule
import dev.ryu.core.shared.system.Tag
import org.bson.Document
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/*
    * Author: T4yrn
    * Project: tags
    * Date: 13/2/2024 - 16:51
*/

object TagModule : IModule {

    val tags: ConcurrentMap<String, Tag> = ConcurrentHashMap()

    override fun id(): Int {
        return 8
    }

    override fun onEnable() {
        BackendModule.getCollection("tags").find().forEach{
            val tag = parse(it)
            tags[tag.name] = tag
        }
    }

    override fun onDisable() {
        tags.values.forEach{
            it.save(true)
            tags.remove(it.name, it)
        }
    }

    override fun onReload() {
        tags.clear()

        BackendModule.getCollection("tags").find().forEach{
            val tag = parse(it)
            tags[tag.name] = tag
        }
    }

    override fun moduleName(): String {
        return "Tag System"
    }

    override fun isDefault(): Boolean {
        return true
    }

    fun getTags(): Collection<Tag> {
        return tags.values
    }

    fun findByName(name: String): Tag? {
        return name.let { tag ->
            tags.values.firstOrNull { it.name == tag }
        }
    }

    fun findByType(typeTag: String): Tag? {
        return typeTag.let { type ->
            tags.values.firstOrNull { it.type == type }
        }
    }

    private fun parse(document: Document) : Tag {
        return Tag(document.getString("_id"))
    }

}