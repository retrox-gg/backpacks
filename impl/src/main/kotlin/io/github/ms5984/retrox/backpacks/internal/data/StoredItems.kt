package io.github.ms5984.retrox.backpacks.internal.data
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

/**
 * Represents items stored in a backpack.
 *
 * @since 0.0.1
 */
data class StoredItems(private val _items: HashMap<Int, ItemStack> = HashMap()) {
    val items
        get() = _items.toMap()

    constructor(that: StoredItems) : this(HashMap(that._items))

    fun setItem(position: Int, item: ItemStack?) : Boolean {
        if (position < 0) throw IllegalArgumentException("position must be non-negative")
        if (item == null) return _items.remove(position) != null
        _items[position] = item
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoredItems

        if (_items != other._items) return false

        return true
    }

    override fun hashCode(): Int {
        return _items.hashCode()
    }
}
