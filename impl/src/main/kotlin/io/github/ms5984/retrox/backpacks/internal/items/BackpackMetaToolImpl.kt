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

import io.github.ms5984.retrox.backpacks.api.Backpack
import io.github.ms5984.retrox.backpacks.api.items.AssociationTool
import io.github.ms5984.retrox.backpacks.api.items.BackpackMetaTool
import io.github.ms5984.retrox.backpacks.internal.BackpacksPlugin
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.jetbrains.annotations.NotNull
import java.util.function.Function

data class BackpackMetaToolImpl(private val backpack: Backpack) : BackpackMetaTool {
    private val key
        get() = JavaPlugin.getPlugin(BackpacksPlugin::class.java).backpackIdKey

    override fun setBackpack(item: @NotNull ItemStack): AssociationTool {
        val meta = item.itemMeta
        meta.persistentDataContainer.set(key, MetadataId, backpack.id())
        item.itemMeta = meta
        return AssociationToolImpl(backpack, item)
    }

    override fun asFunction(): Function<@NotNull ItemStack, AssociationTool> {
        return Function(::setBackpack)
    }
}
