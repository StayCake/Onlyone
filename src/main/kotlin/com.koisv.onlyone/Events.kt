package com.koisv.onlyone

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent

class Events : Listener {
    @EventHandler
    private fun pickupLimit(e: EntityPickupItemEvent) {
        if (e.entityType == EntityType.PLAYER && status) {
            val host = e.entity as Player
            val hostInv = host.inventory.storageContents ?: return
            val leftHand = host.inventory.itemInOffHand
            if (hostInv.any { listOf(e.item.itemStack.type ,leftHand.type).contains(it?.type) }) {
                e.isCancelled = true
            } else {
                if (!e.item.isValid) return
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
                host.playPickupItemAnimation(e.item,1)
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
}