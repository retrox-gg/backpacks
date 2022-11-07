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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import com.google.gson.reflect.TypeToken
import io.github.ms5984.retrox.backpacks.api.Backpack
import io.github.ms5984.retrox.backpacks.api.BackpackService
import io.github.ms5984.retrox.backpacks.internal.items.ItemMetaStorage
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType

data class BackpackServiceImpl(private val plugin: BackpacksPlugin) : BackpackService {
    // Force Gson to deserialize numbers as Number (not Double)
    private val gson: Gson = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LAZILY_PARSED_NUMBER).create()

    override fun test(item: ItemStack?): Boolean {
        item?.let {
            return it.itemMeta.persistentDataContainer.has(plugin.backpackKey, ItemMetaStorage)
        }
        return false
    }

    override fun loadFromItem(item: ItemStack?): Backpack? {
        item?.let {
            it.itemMeta.persistentDataContainer.apply {
                val items = get(plugin.backpackKey, ItemMetaStorage) ?: return null // This is not a backpack
                get(plugin.optionsKey, PersistentDataType.STRING)?.let { json ->
                    gson.fromJson<HashMap<String, Any>?>(
                        json,
                        TypeToken.getParameterized(HashMap::class.java, String::class.java, Any::class.java).type
                    )
                }?.let { map -> return BackpackImpl(items, map) }
                return BackpackImpl(items)
            }
        }
        return null
    }

    override fun create() = BackpackImpl()
}
