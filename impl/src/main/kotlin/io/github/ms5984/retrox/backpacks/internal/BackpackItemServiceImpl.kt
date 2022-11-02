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

import io.github.ms5984.retrox.backpacks.api.Backpack
import io.github.ms5984.retrox.backpacks.api.BackpackItemService
import io.github.ms5984.retrox.backpacks.api.BackpackService
import io.github.ms5984.retrox.backpacks.internal.items.MetadataId
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class BackpackItemServiceImpl : BackpackItemService {
    private val key
        get() = JavaPlugin.getPlugin(BackpacksPlugin::class.java).backpackIdKey

    override fun getBackpack(item: ItemStack?): Backpack? {
        return item?.let {
            val backpackService = Bukkit.getServicesManager().load(BackpackService::class.java) ?: return null
            val id = it.itemMeta.persistentDataContainer.get(key, MetadataId)
            return id?.let { it1 -> backpackService.getById(it1) }
        }
    }

    override fun getAssociatedItem(backpack: Backpack): ItemStack? {
        TODO("Not yet implemented")
    }

    override fun disassociateItem(backpack: Backpack): Boolean {
        TODO("Not yet implemented")
    }
}
