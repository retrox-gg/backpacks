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
import org.jetbrains.annotations.PropertyKey

fun GUIControl.generateControl(): ItemStack {
    return when {
        this == GUIControl.PREV -> loadItem(
            "gui.controls.prev.name",
            "gui.controls.prev.lore",
            Material.RED_CONCRETE,
            "previous-page"
        )
        this == GUIControl.NEXT -> loadItem(
            "gui.controls.next.name",
            "gui.controls.next.lore",
            Material.GREEN_CONCRETE,
            "next-page"
        )
        this == GUIControl.CLOSE -> loadItem(
            "gui.controls.close.name",
            "gui.controls.close.lore",
            Material.PURPLE_STAINED_GLASS_PANE,
            "close"
        )
        else -> throw IllegalArgumentException("Unknown control $this")
    }
}

private fun loadItem(
    @PropertyKey(resourceBundle = "lang.messages") name: String,
    @PropertyKey(resourceBundle = "lang.messages") lore: String,
    defaultMaterial: Material,
    section: String
): ItemStack =
    getControlSection(section).let { controlSection ->
        ItemStack(controlSection?.getControlMaterial() ?: defaultMaterial).apply {
            itemMeta = itemMeta.apply {
                displayName(Messages.get(name).asComponent())
                lore(Messages.getAsList(lore).map { it.asComponent() })
                controlSection?.getControlCustomModelData()?.let { setCustomModelData(it) }
            }
        }
    }

private fun getControlSection(path: String): ConfigurationSection? =
    BackpacksPlugin.instance.config.getConfigurationSection("gui.controls.$path")

private fun ConfigurationSection.getControlMaterial(): Material? =
    getString("material")?.let { Material.matchMaterial(it) }

private fun ConfigurationSection.getControlCustomModelData(): Int? =
    getInt("custom-model-data").takeIf { it != 0 }
