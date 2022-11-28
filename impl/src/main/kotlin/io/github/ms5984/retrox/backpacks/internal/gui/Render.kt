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

import io.github.ms5984.retrox.backpacks.api.BackpackService
import io.github.ms5984.retrox.backpacks.internal.BackpacksPlugin
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryAction.*
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

data class Render(val gui: BackpackGUI, val page: Int, val itemRows: Int) : Listener {
    private val inventory = Bukkit.createInventory(null, (itemRows + 1) * 9, Component.text("Backpack page $page"))
    private val slots: IntRange = (page - 1).let { it * MAX_ITEMS_PER_PAGE until it * itemRows * 9 }
    private val nav: NavigationRow = NavigationRow(itemRows * 9 until (itemRows + 1) * 9)

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
        gui.backpack.itemCollect()?.let { itemCollect -> // Control item collection
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
    }

    fun open(player: Player) = player.openInventory(inventory)

    private fun closeSoon(player: Player) =
        player.openInventory.takeIf { it.topInventory == inventory }?.run {
            Bukkit.getScheduler().runTask(BackpacksPlugin.instance, ::close)
        }

    private fun previous() {
        if (page == 1) {
            throw IllegalArgumentException("This is the first page")
        }
        Bukkit.getScheduler().runTask(BackpacksPlugin.instance) { -> gui.page(page - 1) }
        release()
    }

    private fun next() {
        if (page == gui.pages) {
            throw IllegalArgumentException("This is the last page")
        }
        Bukkit.getScheduler().runTask(BackpacksPlugin.instance) { -> gui.page(page + 1) }
        release()
    }

    private fun setItemCollect(newState: Boolean) {
        gui.backpack.options["itemCollect"] = newState
        Bukkit.getScheduler().runTask(BackpacksPlugin.instance) { -> gui.page(page) }
        release()
    }

    @EventHandler
    fun releaseOnClose(event: InventoryCloseEvent) {
        if (event.inventory === inventory) release()
    }

    @EventHandler(ignoreCancelled = true)
    fun onClick(event: InventoryClickEvent) {
        if (event.inventory !== inventory) return
        if (event.slot in nav.slots) {
            event.isCancelled = true
            nav.handle(event.slot) ?: closeSoon(event.whoClicked as Player)
        }
    }

    // Prevent moving this or other backpacks into this inventory
    @EventHandler(ignoreCancelled = true)
    fun onMoveBackpackToOtherInventory(event: InventoryClickEvent) {
        when {
            // handle placement/swap into this inventory
            when (event.action) {
                PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR -> event.clickedInventory === inventory
                else -> false
            } -> event.cursor
            // handle shift-clicks on bottom inventory
            event.isShiftClick && event.clickedInventory === event.view.bottomInventory -> event.currentItem
            // handle hotbar-type "hover clicks" on top (backpack render) inventory
            event.click == ClickType.NUMBER_KEY && event.clickedInventory === inventory -> {
                // get the item in the hotbar slot
                event.view.bottomInventory.getItem(event.hotbarButton)
            }
            else -> null
        }?.apply {
            // cancel if the item is a backpack
            takeIf { BackpackService.getInstance().test(it) }?.let {
                event.isCancelled = true
            }
        }
    }

    private fun release() = HandlerList.unregisterAll(this)
}
