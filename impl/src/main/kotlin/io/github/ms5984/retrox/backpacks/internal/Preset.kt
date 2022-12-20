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

import io.github.ms5984.retrox.accessories.api.AccessoryService
import io.github.ms5984.retrox.accessories.model.Category
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

data class Preset(
    val rows: Int = 1,
    val itemCollection: Boolean = false,
    val material: Material,
    val modelData: Int? = null,
    val displayName: Messages.Raw,
    val lore: List<Messages.Raw> = emptyList()
) {
    val item by lazy {
        ItemStack(material).apply {
            itemMeta = itemMeta.apply {
                modelData?.let { setCustomModelData(it) }
                displayName(this@Preset.displayName.asDisplayName())
                this@Preset.lore.takeIf { it.isNotEmpty() }?.map { it.asLoreLine() }?.let { lore(it) }
            }
            // apply category
            AccessoryService.getInstance().addNBT(this, Category.fromId("backpack"))
            // apply backpack
            BackpacksPlugin.instance.backpackService.create().apply {
                rows = this@Preset.rows
                if (itemCollection) itemCollect = false
            }.let { BackpacksPlugin.instance.backpackService.saveToItem(it, this) }
        }
    }

    companion object {
        fun fromConfig(config: ConfigurationSection): Preset {
            return Preset(
                config.getInt("rows").takeIf { it > 0 }!!,
                config.getBoolean("itemCollection"),
                Material.matchMaterial(config.getString("material")!!)!!,
                config.getInt("model-data").takeIf { it != 0 },
                Messages.Chaining(config.getString("displayName") ?: "Backpack"),
                config.getStringList("lore").map { Messages.Chaining(it) }
            )
        }
    }
}
