package io.github.ms5984.retrox.backpacks.internal
/*
 *  Copyright 2022 ms5984, Retrox
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import org.bukkit.Bukkit
import org.bukkit.block.Container
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class BukkitEventProcessor(private val plugin: BackpacksPlugin): Listener {
    // open backpack on right click in hand
    @EventHandler
    fun onRightClickHoldingBackpack(event: PlayerInteractEvent) {
        // skip if this interaction (a placement) was already cancelled by another assembly
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.useItemInHand() == Event.Result.DENY) return
        event.item?.apply {
            when (event.action) {
                Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> {
                    // only cancel event + load if the item is a backpack
                    takeIf { plugin.backpackService.test(it) }?.let {
                        event.isCancelled = true
                        // capture backpack item and player
                        val backpackItem = it
                        val player = event.player
                        loadAndOpen(it, event.player) { backpackImpl ->
                            val itemInMainHand = player.inventory.itemInMainHand
                            // if the player is still holding the backpack, replace it
                            if (itemInMainHand != backpackItem) false
                            else {
                                plugin.backpackService.saveToItem(backpackImpl, itemInMainHand)
                                player.inventory.setItemInMainHand(itemInMainHand)
                                true
                            }
                        }
                    }
                }
                else -> return
            }
        }
    }

    // TODO find a way (cancel events) to lock the backpack's item in place
    //  if it's in the player's inventory (which would make it less likely for
    //  write-out to fail)
    // open backpack on right click in inventory
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    fun onRightClickInInventory(event: InventoryClickEvent) {
        // Only handle pure right clicks
        if (event.click != ClickType.RIGHT) return
        // only cancel event + load if the item is a backpack
        event.currentItem?.takeIf(plugin.backpackService::test)?.let {
            event.isCancelled = true
            // Only let them open backpacks in an inventory we can write to
            val getHolderInventory = event.clickedInventory?.holder.let { holder ->
                when (holder) {
                    is Player -> { { holder.inventory } } // use player capture
                    is Container -> {
                        // capture the block
                        val block = holder.block
                        {
                            // if the block is still a container, return its inventory
                            (block.state as? Container)?.inventory
                        }
                    }
                    else -> null
                }
            } ?: return
            // Get the slot information so we can update it later
            val slot = event.slot
            val hotbarSlot = event.takeIf { event.clickedInventory?.holder is Player }?.slot.takeIf { i -> i in 0..8 }
            val backpackItem = it.clone()
            val lockInPlace = LockBackpackItemInPlaceListener(event.whoClicked as Player) { laterEvent ->
                when (laterEvent.action) {
                    // Cancel normal item pickup, swap, move, collect and drop for slot
                    InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_ONE,
                    InventoryAction.SWAP_WITH_CURSOR, InventoryAction.MOVE_TO_OTHER_INVENTORY,
                    InventoryAction.COLLECT_TO_CURSOR, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_SLOT ->
                        slot == laterEvent.slot && laterEvent.clickedInventory === getHolderInventory()
                    InventoryAction.HOTBAR_SWAP -> {
                        // TODO debug this section (it's not fully working)
                        // If slot was a hotbar slot and this is a hotbar swap with that slot, cancel
                        hotbarSlot?.let { key -> laterEvent.slot == key}
                            // if it wasn't a hotbar slot, cancel if the slot + inventory matches
                            ?: (laterEvent.slot == slot && laterEvent.clickedInventory === getHolderInventory())
                    }
                    else -> false
                }
            }
            Bukkit.getPluginManager().registerEvents(lockInPlace, plugin)
            loadAndOpen(it, event.whoClicked as Player) { backpack ->
                val holderInventory = getHolderInventory() ?: return@loadAndOpen false
                if (holderInventory.getItem(slot) == backpackItem) {
                    // Item is still in the same slot and same state, update it
                    plugin.backpackService.saveToItem(backpack, backpackItem)
                    holderInventory.setItem(slot, backpackItem)
                    lockInPlace.unregister()
                    return@loadAndOpen true
                }
                false
            }
        }
    }

    private fun loadAndOpen(item: ItemStack, player: Player, itemUpdater: (BackpackImpl) -> Boolean) {
        // try to load async
        Bukkit.getScheduler().runTaskAsynchronously(plugin) { ->
            item.let { plugin.backpackService.loadFromItem(it) }?.apply {
                // if present, open sync
                Bukkit.getScheduler().runTask(plugin) { ->
                    plugin.backpackService.open(this) { itemUpdater.invoke(it as BackpackImpl) }.view(player)
                }
            }
        }
    }

    companion object {
        private class LockBackpackItemInPlaceListener(val player: Player,
                                                      val test: ((event: InventoryClickEvent) -> Boolean)): Listener {
            // stop the player from moving the backpack item while it's open
            @EventHandler(ignoreCancelled = true)
            fun onBackpackClickWhileOpen(event: InventoryClickEvent) {
                if (event.whoClicked !== player) return
                if (test.invoke(event)) event.isCancelled = true
            }

            fun unregister() = InventoryClickEvent.getHandlerList().unregister(this)
        }
    }
}
