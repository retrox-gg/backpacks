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

object ItemMetaStorage : PersistentDataType<Array<ByteArray?>, StoredItems> {
    override fun getPrimitiveType() = Array<ByteArray?>::class.java

    override fun getComplexType() = StoredItems::class.java

    override fun fromPrimitive(primitive: Array<ByteArray?>, context: PersistentDataAdapterContext): StoredItems {
        val items = HashMap<Int, ItemStack>()
        for (i in primitive.indices) {
            primitive[i]?.let { items[i] = ItemStack.deserializeBytes(it) }
        }
        return StoredItems(items)
    }

    override fun toPrimitive(complex: StoredItems, context: PersistentDataAdapterContext): Array<ByteArray?> {
        val items = complex.items
        val primitive = arrayOfNulls<ByteArray>(items.keys.maxOrNull() ?: return emptyArray())
        items.forEach { (position, item) ->
            primitive[position] = item.serializeAsBytes()
        }
        return primitive
    }
}
