package io.github.ms5984.retrox.backpacks.internal.gui
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

import io.github.ms5984.retrox.backpacks.internal.BackpacksPlugin
import io.github.ms5984.retrox.backpacks.internal.Messages
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.ItemStack

private fun getControlSection(path: String): ConfigurationSection? =
    BackpacksPlugin.instance.config.getConfigurationSection("gui.controls.$path")

private fun ConfigurationSection.getControlMaterial(): Material? {
    return try {
        Material.valueOf(getString("material")!!)
    } catch (e: Exception) {
        null
    }
}

private fun ConfigurationSection.getControlCustomModelData(): Int? {
    // TODO see why getInt("custom-model-data", -1) doesn't work properly
    // this works but i can't specify a custom magic value, it has to be 0
    return getInt("custom-model-data").takeIf { it != 0 }
}

fun GUIControl.generateControl(): ItemStack {
    return when {
        this == GUIControl.PREV -> {
            val controlSection = getControlSection("previous-page")
            ItemStack(controlSection?.getControlMaterial() ?: Material.RED_CONCRETE).apply {
                val meta = itemMeta
                meta.displayName(Messages.get("gui.controls.prev.name").asComponent())
                meta.lore(Messages.getAsList("gui.controls.prev.lore").map { it.asComponent() })
                controlSection?.getControlCustomModelData()?.let { meta.setCustomModelData(it) }
                itemMeta = meta
            }
        }
        this == GUIControl.NEXT -> {
            val controlSection = getControlSection("next-page")
            ItemStack(controlSection?.getControlMaterial() ?: Material.GREEN_CONCRETE).apply {
                val meta = itemMeta
                meta.displayName(Messages.get("gui.controls.next.name").asComponent())
                meta.lore(Messages.getAsList("gui.controls.next.lore").map { it.asComponent() })
                controlSection?.getControlCustomModelData()?.let {
                    meta.setCustomModelData(it)
                    BackpacksPlugin.instance.logger.info("Setting ${this@generateControl} custom model data to $it")
                }
                itemMeta = meta
            }
        }
        else -> throw IllegalArgumentException("Unknown control $this")
    }
}

fun generatePlaceholder(): ItemStack {
    return ItemStack(Material.PURPLE_STAINED_GLASS_PANE).apply {
        val meta = itemMeta
        meta.displayName(Messages.miniMessage.deserialize(""))
        itemMeta = meta
    }
}
