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

import org.bukkit.inventory.ItemStack

data class NavigationRow(val slots: IntRange) {
    private val items = arrayOfNulls<ItemStack>(9)
    private val actions: MutableMap<Int, () -> Unit> = mutableMapOf()
    private val placeholder by lazy { generatePlaceholder() }

    // expected slot [0-9)
    fun setItem(slot: Int, control: ItemStack?, action: (() -> Unit)? = null) {
        items[slot] = control
        action?.let { actions[slot] = it }
    }

    // expected slot [0-9)
    fun getItem(slot: Int): ItemStack {
        return items[slot] ?: placeholder
    }

    // slot normalized
    fun handle(slot: Int) {
        actions[slot % 9]?.let { it() }
    }
}
