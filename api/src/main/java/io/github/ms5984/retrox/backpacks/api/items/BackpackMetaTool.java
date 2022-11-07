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

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Apply backpack item metadata.
 *
 * @since 0.0.1
 * @author ms5984
 */
@ApiStatus.NonExtendable
public interface BackpackMetaTool {
    /**
     * Apply this backpack to an item.
     * <p>
     * <strong>{@code item} will be modified.</strong>
     *
     * @param item an item
     * @return true unless the item was not modified
     */
    @Contract(mutates = "param1")
    boolean apply(@NotNull ItemStack item);

    /**
     * Get a function which applies this backpack to an item.
     * <p>
     * <strong>The returned function will modify its argument.</strong>
     * <p>
     * The contract of the returned function is the same as described by
     * {@link #apply(ItemStack)}.
     *
     * @return a function applying this backpack to an item
     */
    default @NotNull Function<@NotNull ItemStack, Boolean> asFunction() {
        return this::apply;
    }
}
