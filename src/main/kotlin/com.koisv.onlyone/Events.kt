package com.koisv.onlyone

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class Events : Listener {
    @EventHandler
    private fun pickupLimit(e: EntityPickupItemEvent) {
        if (e.entityType == EntityType.PLAYER) {
            val host = e.entity as Player
            if (
                host.inventory.storageContents?.contains(e.item.itemStack) == true
            ) e.item.setCanPlayerPickup(true) else {
                e.item.owner = host.uniqueId
                e.item.setCanPlayerPickup(false)
            }
        }
    }
}