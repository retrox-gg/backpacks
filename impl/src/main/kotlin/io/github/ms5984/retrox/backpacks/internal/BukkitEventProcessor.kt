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

import io.github.ms5984.retrox.backpacks.api.BackpackService
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.ClickType
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
                    takeIf { BackpackService.getInstance().test(it) }?.let {
                        event.isCancelled = true
                        loadAndOpen(it, event.player)
                    }
                }
                else -> return
            }
        }
    }

    // open backpack on right click in inventory
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
    fun onRightClickInInventory(event: InventoryClickEvent) {
        // Only handle pure right clicks
        if (event.click != ClickType.RIGHT) return
        event.currentItem?.apply {
            // only cancel event + load if the item is a backpack
            takeIf { BackpackService.getInstance().test(it) }?.let {
                event.isCancelled = true
                loadAndOpen(it, event.whoClicked as Player)
            }
        }
    }

    private fun loadAndOpen(item: ItemStack, player: Player) {
        // try to load async
        Bukkit.getScheduler().runTaskAsynchronously(plugin) { ->
            item.let { BackpackService.getInstance().loadFromItem(it) }?.apply {
                // if present, open sync
                Bukkit.getScheduler().runTask(plugin) { -> open(player) }
            }
        }
    }
}
