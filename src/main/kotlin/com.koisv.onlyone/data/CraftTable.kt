package com.koisv.onlyone.data

import com.koisv.onlyone.Main
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import java.util.UUID

data class CraftTable(val uuid: UUID) {

    private fun getCraft() : YamlConfiguration {
        return Main.craft
    }

    private fun writeTable(target: UUID, data: List<ItemStack?>) {
        val targetData = getCraft().getConfigurationSection(target.toString())
            ?: getCraft().createSection(target.toString())
        for (i in 0..8) {
            val current = data[i]
            targetData.set(i.toString(),current)
        }
        getCraft().save(Main.craftLoc)
    }

    private fun readTable(target: UUID) : List<ItemStack?> {
        getCraft().load(Main.craftLoc)
        val targetData = getCraft().getConfigurationSection(target.toString())
        return if (targetData != null) {
            val result = mutableListOf<ItemStack?>()
            for (i in 0..8) {
                val current = targetData.getItemStack(i.toString())
                if (current != null) result.add(current) else result.add(null)
            }
            return result
        } else listOf()
    }

    var table
    get() = readTable(uuid)
    set(value) {
        writeTable(uuid, value)
    }
}
