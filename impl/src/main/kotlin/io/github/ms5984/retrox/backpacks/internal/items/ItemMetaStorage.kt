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

import io.github.ms5984.retrox.backpacks.internal.data.StoredItemSerializer
import io.github.ms5984.retrox.backpacks.internal.data.StoredItems
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

/**
 * Maps [StoredItems] to a String.
 *
 * @since 0.0.1
 */
object ItemMetaStorage : PersistentDataType<String, StoredItems> {
    override fun getPrimitiveType() = String::class.java
    override fun getComplexType() = StoredItems::class.java

    // Format: json
    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): StoredItems = StoredItemSerializer.gson.fromJson(primitive, StoredItems::class.java)

    override fun toPrimitive(complex: StoredItems, context: PersistentDataAdapterContext): String = StoredItemSerializer.gson.toJson(complex)
}
