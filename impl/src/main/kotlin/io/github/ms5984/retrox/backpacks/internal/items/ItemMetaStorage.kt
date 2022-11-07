package io.github.ms5984.retrox.backpacks.internal.items
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

import io.github.ms5984.retrox.backpacks.internal.data.StoredItems
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer

/**
 * Maps [StoredItems] to byte array.
 *
 * @since 0.0.1
 */
object ItemMetaStorage : PersistentDataType<ByteArray, StoredItems> {
    override fun getPrimitiveType() = ByteArray::class.java
    override fun getComplexType() = StoredItems::class.java

    // Format:
    // first int = number of bytes used to store next item
    // second int = item position
    // byte[0]..byte[firstInt-1] = item data
    // repeat
    override fun fromPrimitive(primitive: ByteArray, context: PersistentDataAdapterContext): StoredItems {
        val items = StoredItems()
        ByteBuffer.wrap(primitive).run {
            while (hasRemaining()) {
                val itemSize = int
                val itemPosition = int
                val itemData = ByteArray(itemSize)
                get(itemData)
                items.setItem(itemPosition, ItemStack.deserializeBytes(itemData))
            }
        }
        return items
    }

    override fun toPrimitive(complex: StoredItems, context: PersistentDataAdapterContext): ByteArray {
        val items = complex.items.mapValues { (_, item) -> item.serializeAsBytes() }
        val size = items.values.sumOf { it.size } + items.size * 8
        return ByteBuffer.wrap(ByteArray(size)).apply {
            items.forEach { (position, item) ->
                putInt(item.size)
                putInt(position)
                put(item)
            }
        }.array()
    }
}
