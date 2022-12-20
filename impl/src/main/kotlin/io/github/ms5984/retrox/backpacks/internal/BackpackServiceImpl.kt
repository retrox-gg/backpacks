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
import java.util.function.Function

class BackpackServiceImpl(private val plugin: BackpacksPlugin) : BackpackService {
    // Force Gson to deserialize numbers as Long or Double (not just Double)
    private val gson: Gson = GsonBuilder().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create()

    override fun test(item: ItemStack?): Boolean {
        item?.itemMeta?.let {
            return it.persistentDataContainer.has(plugin.backpackKey, ItemMetaStorage)
        }
        return false
    }

    override fun create() = BackpackImpl()

    override fun loadFromItem(item: ItemStack?): BackpackImpl? {
        item?.itemMeta?.let {
            it.persistentDataContainer.apply {
                val items = get(plugin.backpackKey, ItemMetaStorage) ?: return null // This is not a backpack
                if (has(plugin.optionsKey, PersistentDataType.STRING)) {
                    get(plugin.optionsKey, PersistentDataType.STRING)?.let { json ->
                        gson.fromJson<HashMap<String, Any>?>(
                            json,
                            TypeToken.getParameterized(HashMap::class.java, String::class.java, Any::class.java).type
                        )
                    }?.let { map -> return BackpackImpl(items, map) }
                }
                return BackpackImpl(items)
            }
        }
        return null
    }

    override fun saveToItem(backpack: Backpack, item: ItemStack): Boolean {
        backpack as BackpackImpl
        val meta = item.itemMeta!!
        // generate json from options
        val options = gson.toJson(backpack.options, TypeToken.getParameterized(HashMap::class.java, String::class.java, Any::class.java).type)
        var update = false
        val dataContainer = meta.persistentDataContainer
        if (!dataContainer.has(BackpacksPlugin.instance.backpackKey, ItemMetaStorage) || // The data is empty or its type is mismatched
            dataContainer.get(BackpacksPlugin.instance.backpackKey, ItemMetaStorage) != backpack.items) { // The data is different
            // update items
            dataContainer.set(BackpacksPlugin.instance.backpackKey, ItemMetaStorage, backpack.items)
            update = true
        }
        if (!dataContainer.has(BackpacksPlugin.instance.optionsKey, PersistentDataType.STRING) || // The data is empty or its type is mismatched
            dataContainer.get(BackpacksPlugin.instance.optionsKey, PersistentDataType.STRING) != options) { // The data is different
            // update options
            dataContainer.set(BackpacksPlugin.instance.optionsKey, PersistentDataType.STRING, options)
            update = true
        }
        if (update) item.itemMeta = meta
        return update
    }

    override fun open(backpack: Backpack, update: Function<in Backpack, Boolean>?) = BackpackViewImpl(backpack as BackpackImpl, update)
}
