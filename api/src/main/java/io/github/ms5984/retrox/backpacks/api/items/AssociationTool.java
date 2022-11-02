package io.github.ms5984.retrox.backpacks.api.items;
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

import io.github.ms5984.retrox.backpacks.api.Backpack;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Manage the association between a backpack and an item.
 *
 * @since 0.0.1
 * @author ms5984
 */
@ApiStatus.NonExtendable
public interface AssociationTool {
    /**
     * Get the backpack associated with this tool.
     *
     * @return the backpack associated with this tool
     */
    @NotNull Backpack backpack();

    /**
     * Get the item associated with this tool.
     *
     * @return the item associated with this tool
     */
    @NotNull ItemStack item();

    /**
     * Associate the item with the backpack.
     * <p>
     * Internally, this causes Backpacks to store the item's NBT data.
     */
    void associate();
}
