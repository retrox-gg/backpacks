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

import io.github.ms5984.retrox.backpacks.internal.Messages
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun GUIControl.generateControl() : ItemStack {
    return when {
        this == GUIControl.PREV -> {
            ItemStack(Material.RED_CONCRETE).apply {
                val meta = itemMeta
                meta.displayName(Messages.get("gui.controls.prev.name").asComponent())
                meta.lore(Messages.getAsList("gui.controls.prev.lore").map { it.asComponent() })
                itemMeta = meta
            }
        }
        this == GUIControl.NEXT -> {
            ItemStack(Material.GREEN_CONCRETE).apply {
                val meta = itemMeta
                meta.displayName(Messages.get("gui.controls.next.name").asComponent())
                meta.lore(Messages.getAsList("gui.controls.next.lore").map { it.asComponent() })
                itemMeta = meta
            }
        }
        else -> throw IllegalArgumentException("Unknown control $this")
    }
}
