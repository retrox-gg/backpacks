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
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent

data class Render(val gui: BackpackGUI, val page: Int, val itemRows: Int) : Listener {
    private val inventory = Bukkit.createInventory(null, (itemRows + 1) * 9, Component.text("Backpack page $page"))
    private val slots: IntRange = (page - 1).let { it * MAX_ITEMS_PER_PAGE until it * itemRows * 9 }

    init {
        // Setup listener
        Bukkit.getPluginManager().registerEvents(this, BackpacksPlugin.instance)
        // Insert contents
        gui.backpack.items.items.forEach { (slot, item) -> if (slot in slots) inventory.setItem(slot % MAX_ITEMS_PER_PAGE, item) }
        // TODO: draw controls
    }

    fun open(player: Player) {
        player.openInventory(inventory)
    }

    fun previous() {
        if (page > 1) {
            gui.page(page - 1) //TODO: schedule open
            release()
        }
        throw IllegalArgumentException("This is the first page")
    }

    fun next() {
        if (page < gui.pages) {
            gui.page(page + 1) //TODO: schedule open
            release()
        }
        throw IllegalArgumentException("This is the last page")
    }

    @EventHandler
    fun releaseOnClose(event: InventoryCloseEvent) {
        if (event.inventory === inventory) release()
    }

    private fun release() = HandlerList.unregisterAll(this)
}
