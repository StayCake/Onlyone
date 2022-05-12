package com.koisv.onlyone

import com.koisv.onlyone.data.CraftTable
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.*
import org.bukkit.inventory.CraftingInventory
import org.bukkit.inventory.ItemStack

class Events : Listener {
    @EventHandler
    private fun pickupLimit(e: EntityPickupItemEvent) {
        if (e.entityType == EntityType.PLAYER && status) {
            val host = e.entity as Player
            val hostInv = host.inventory.storageContents ?: return
            val leftHand = host.inventory.itemInOffHand
            val cursor = host.itemOnCursor
            if (hostInv.any { listOf(it?.type ,leftHand.type ,cursor.type).contains(e.item.itemStack.type) }) {
                e.isCancelled = true
            } else {
                if (!e.item.isValid) return
                host.playPickupItemAnimation(e.item.apply { itemStack = itemStack.asOne() })
                e.isCancelled = true
                e.item.apply {
                    if (itemStack.amount > 1) {
                        itemStack = itemStack.apply {
                            itemStack.amount -= 1
                        }
                    } else {
                        this.remove()
                    }
                }
                host.inventory.addItem(e.item.itemStack.asOne())
            }
        }
    }
    @EventHandler
    private fun oneCraft(e: PrepareItemCraftEvent) {
        val res = e.inventory.result
        if (res != null && status) {
            e.inventory.result = res.asOne()
        }
    }

    @EventHandler
    private fun chestLock(e: InventoryClickEvent) {
        if (
            (e.click != ClickType.LEFT
            || e.clickedInventory?.any {
                it?.type == e.cursor?.type
            } == true) && status
        ) e.isCancelled = true
    }

    @EventHandler
    private fun dragBlock(e: InventoryDragEvent) {
        if (status) e.isCancelled = true
    }

    @EventHandler
    private fun craftClose(e: InventoryCloseEvent) {
        val targetTable = e.inventory
        if (targetTable is CraftingInventory) {
            val hostTable = CraftTable(e.player.uniqueId)
            val targetList = mutableListOf<ItemStack?>()
            targetTable.matrix?.forEach {
                targetList.add(it)
            }
            hostTable.table = targetList
            e.inventory.contents = Array<ItemStack?>(10){ null }
        }
    }

    @EventHandler
    private fun restoreCraft(e: InventoryOpenEvent) {
        if (status) {
            val restoreTable = e.inventory
            if (restoreTable is CraftingInventory) {
                val loadData = CraftTable(e.player.uniqueId).table
                val applyTable = Array<ItemStack?>(10){ null }
                for (i in 1..9) {
                    applyTable[i] = loadData[i - 1]
                }
                e.inventory.contents = applyTable
            }
        }
    }
}