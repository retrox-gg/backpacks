package io.github.ms5984.retrox.backpacks.internal.gui
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

import io.github.ms5984.retrox.backpacks.internal.BackpacksPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask

data class Render(val gui: BackpackGUI, val page: Int, val itemRows: Int) : Listener {
    private val inventory = Bukkit.createInventory(null, (itemRows + 1) * 9, Component.text("Backpack page $page"))
    private val slots: IntRange = ((page - 1) * MAX_ITEMS_PER_PAGE).let { it until it + itemRows * 9 }
    private val nav: NavigationRow = NavigationRow(itemRows * 9 until (itemRows + 1) * 9)
    private val updates = mutableMapOf<ItemStack, Int>()
    private val updateTask: BukkitTask

    init {
        // Setup listener
        Bukkit.getPluginManager().registerEvents(this, BackpacksPlugin.instance)
        // Insert contents
        gui.backpack.items.items.forEach { (slot, item) -> if (slot in slots) inventory.setItem(slot % MAX_ITEMS_PER_PAGE, item) }
        // Draw navigation
        if (page > 1) { // Previous page
            // generate
            val prevPageEvent = GUIControlDrawEvent(GUIControl.PREV, GUIControl.PREV.generateControl())
            Bukkit.getPluginManager().callEvent(prevPageEvent)
            // place item + bind action
            prevPageEvent.slots.forEach { nav.setItem(it, prevPageEvent.finalItem, ::previous) }
        }
        if (page < gui.pages) { // Next page
            // generate
            val nextPageEvent = GUIControlDrawEvent(GUIControl.NEXT, GUIControl.NEXT.generateControl())
            Bukkit.getPluginManager().callEvent(nextPageEvent)
            // place item + bind action
            nextPageEvent.slots.forEach { nav.setItem(it, nextPageEvent.finalItem, ::next) }
        }
        gui.backpack.itemCollect?.let { itemCollect -> // Control item collection
            // generate control
            val itemCollectEvent = GUIControlDrawEvent(GUIControl.ITEM_COLLECT, GUIControl.ITEM_COLLECT.generateControl(itemCollect))
            Bukkit.getPluginManager().callEvent(itemCollectEvent)
            // place item + bind action
            itemCollectEvent.slots.forEach {
                nav.setItem(it, itemCollectEvent.finalItem) { setItemCollect(!itemCollect) }
            }
        }
        for (i in nav.slots) {
            inventory.setItem(i, nav.getItem(i % 9))
        }
        updateTask = Bukkit.getScheduler().runTaskTimer(BackpacksPlugin.instance, ::syncInventory, 1, 1)
    }

    fun open(player: Player) {
        Bukkit.getScheduler().runTask(BackpacksPlugin.instance) { -> player.openInventory(inventory) }
    }

    private fun closeSoon(player: Player) =
        player.openInventory.takeIf { it.topInventory === inventory }?.run {
            Bukkit.getScheduler().runTask(BackpacksPlugin.instance, ::close)
        }

    private fun previous() {
        if (page == 1) {
            throw IllegalArgumentException("This is the first page")
        }
        gui.page(page - 1)
    }

    private fun next() {
        if (page == gui.pages) {
            throw IllegalArgumentException("This is the last page")
        }
        gui.page(page + 1)
    }

    private fun setItemCollect(newState: Boolean) {
        gui.backpack.itemCollect = newState
        gui.page(page)
    }

    // Sync inventory
    private fun syncInventory() {
        // Read inventory storage slots
        val items = inventory.contents
        for (i in slots) {
            items[i % MAX_ITEMS_PER_PAGE].let {
                val inStorage = gui.backpack.items.items[i]
                if (inStorage != it) {
                    // update item in storage
                    gui.backpack.items.setItem(i, it)
                    // are items similar?
                    if (inStorage != null && it != null && inStorage.isSimilar(it)) {
                        // easy update
                        val delta = it.amount - inStorage.amount
                        updates.merge(it.asOne(), delta, Int::plus)
                        return@let
                    }
                    // calculate separate deltas
                    inStorage?.let { old -> updates.merge(old.asOne(), -old.amount, Int::plus) }
                    it?.let { new -> updates.merge(new.asOne(), new.amount, Int::plus) }
                }
            }
        }
    }

    // Cleanup on close
    @EventHandler
    fun releaseOnClose(event: InventoryCloseEvent) {
        if (event.inventory === inventory) release()
    }

    // Handle nav slots
    @EventHandler(ignoreCancelled = true)
    fun onNavClick(event: InventoryClickEvent) {
        if (event.inventory !== inventory) return
        if (event.slot in nav.slots) {
            event.isCancelled = true
            nav.handle(event.slot) ?: closeSoon(event.whoClicked as Player)
        }
    }

    // Prevent moving this or other backpacks into this inventory
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onMoveBackpackToBackpackInventory(event: InventoryClickEvent) {
        movingItemToBackpack(event)?.apply {
            // cancel if the item is a backpack
            takeIf { BackpacksPlugin.instance.backpackService.test(it) }?.let {
                event.isCancelled = true
            }
        }
    }

    private fun movingItemToBackpack(event: InventoryClickEvent) =
        when {
            // handle placement/swap into this inventory
            when (event.action) {
                PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR -> event.clickedInventory === inventory
                else -> false
            } -> event.cursor
            // handle shift-clicks on bottom inventory
            event.isShiftClick && event.clickedInventory === event.view.bottomInventory && event.view.topInventory === inventory -> event.currentItem
            // handle hotbar-type "hover clicks" on top (backpack render) inventory
            event.click == ClickType.NUMBER_KEY && event.clickedInventory === inventory -> {
                // get the item in the hotbar slot
                event.view.bottomInventory.getItem(event.hotbarButton)
            }
            else -> null
        }

    private fun release() {
        gui.onClose?.invoke(gui.backpack)?.let {
            if (!it) {
                updates.values.removeIf { i -> i == 0 }
                // If we can't save the backpack, we need to reverse the changes
                val failedRemovals = mutableListOf<ItemStack>()
                updates.forEach { (item, delta) ->
                    if (delta < 0) {
                        // steal items back from the player if necessary
                        gui.player.inventory.removeItemAnySlot(item.asQuantity(-delta)).run {
                            if (isNotEmpty()) failedRemovals.add(values.first())
                        }
                    } else {
                        // give items we failed to store back to the player
                        gui.player.inventory.addItem(item.asQuantity(delta)).run {
                            // if we fail to give the items back directly drop them on the ground
                            if (isNotEmpty()) gui.player.location.let { loc -> loc.world.dropItem(loc, values.first()) }
                        }
                    }
                }
                if (failedRemovals.isNotEmpty()) {
                    BackpacksPlugin.instance.logger.run {
                        warning("Failed to reverse changes to backpack for player ${gui.player.name}")
                        warning("The following items may have been duped: $failedRemovals")
                    }
                }
            }
            updates.clear()
        }
        updateTask.cancel()
        HandlerList.unregisterAll(this)
    }
}
