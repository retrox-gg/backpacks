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

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.ms5984.retrox.backpacks.api.items.BackpackMetaTool
import io.github.ms5984.retrox.backpacks.internal.BackpackImpl
import io.github.ms5984.retrox.backpacks.internal.BackpacksPlugin
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.jetbrains.annotations.NotNull

data class BackpackMetaToolImpl(private val backpack: BackpackImpl) : BackpackMetaTool {
    override fun apply(item: @NotNull ItemStack): Boolean {
        val meta = item.itemMeta
        // generate json from options
        val options = Gson().toJson(backpack.options, TypeToken.getParameterized(HashMap::class.java, String::class.java, Any::class.java).type)
        var update = false
        if (meta.persistentDataContainer.get(BackpacksPlugin.instance.backpackKey, ItemMetaStorage) != backpack.items) {
            // update items
            meta.persistentDataContainer.set(BackpacksPlugin.instance.backpackKey, ItemMetaStorage, backpack.items)
            update = true
        }
        if (meta.persistentDataContainer.get(BackpacksPlugin.instance.optionsKey, PersistentDataType.STRING) != options) {
            // update options
            meta.persistentDataContainer.set(BackpacksPlugin.instance.optionsKey, PersistentDataType.STRING, options)
            update = true
        }
        if (update) item.itemMeta = meta
        return update
    }
}
