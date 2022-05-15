package com.koisv.onlyone

import com.koisv.onlyone.data.CraftTable
import net.kyori.adventure.key.Key
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
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
            if (
                hostInv.any {
                    listOf(it?.type ,leftHand.type ,cursor.type)
                        .contains(e.item.itemStack.type)
                }
            ) e.isCancelled = true
            else {
                e.isCancelled = true
                if (!e.item.isValid) return
                host.playPickupItemAnimation(e.item,1)
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
        val typeCheck =
            e.slotType == InventoryType.SlotType.CRAFTING
                    || e.slotType == InventoryType.SlotType.RESULT
                    || (
                    e.inventory.type == InventoryType.CHEST
                            && e.slotType == InventoryType.SlotType.CONTAINER
                    )
        if (
            (e.click != ClickType.LEFT
            || e.clickedInventory
                ?.any { it?.type == e.cursor?.type } == true)
            && status && !typeCheck
        ) e.isCancelled = true
        if (
            e.inventory.type == InventoryType.WORKBENCH
            || e.inventory.type == InventoryType.CHEST
        ) e.inventory.maxStackSize = 1
    }

    @EventHandler
    private fun dragBlock(e: InventoryDragEvent) {
        if (status) e.isCancelled = true
    }

    @EventHandler
    private fun noSurvivalCraft(e: InventoryClickEvent) {
        if (status
            && e.slotType == InventoryType.SlotType.CRAFTING
            && e.inventory.type == InventoryType.CRAFTING
        ) {
            e.isCancelled = true
            e.whoClicked.sendMessage(
                Component.text("[").append(
                    Component.text("!").color(TextColor.color(255,0,0))
                ).append(
                    Component.text("] 시스템 한계로 사용할 수 없습니다! [/조합]")
                )
            )
            e.whoClicked.playSound(
                Sound.sound(
                    Key.key("block.note_block.bell"),
                    Sound.Source.MASTER,
                1.0F,1.782F))
        }
    }

    @EventHandler
    private fun craftClose(e: InventoryCloseEvent) {
        val targetTable = e.inventory
        if (targetTable is CraftingInventory) {
            if (targetTable.matrix?.size != 9) return
            val hostTable = CraftTable(e.player.uniqueId)
            val applyTable = Array<ItemStack?>( 10){ null }
            for (i in 1..9) {
                applyTable[i - 1] = targetTable.matrix?.get(i - 1)
            }
            hostTable.table = applyTable.toList()
            e.inventory.contents = Array<ItemStack?>(10){ null }
        }
    }

    @EventHandler
    private fun restoreCraft(e: InventoryOpenEvent) {
        if (status) {
            val restoreTable = e.inventory
            if (restoreTable is CraftingInventory) {
                if (restoreTable.matrix?.size != 9) return
                val table = CraftTable(e.player.uniqueId)
                val loadData = table.table
                val applyTable = Array<ItemStack?>(10){ null }
                for (i in 1..9) {
                    applyTable[i] = loadData[i - 1]
                }
                e.inventory.contents = applyTable
            }
        }
    }
}