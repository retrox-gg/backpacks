package io.github.ms5984.retrox.backpacks.api;
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

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Resolve backpack data for this and other plugins.
 *
 * @since 0.0.1
 * @author ms5984
 */
@ApiStatus.NonExtendable
public interface BackpackService extends Predicate<ItemStack> {
    /**
     * Checks if an item has backpack data.
     *
     * @param item an item
     * @return true if the item has backpack data
     */
    @Override
    @Contract("null -> false")
    boolean test(@Nullable ItemStack item);

    /**
     * Creates a new, empty backpack.
     *
     * @return a new backpack
     */
    @NotNull Backpack create();

    /**
     * Loads the backpack data stored in an item.
     *
     * @param item an item
     * @return the backpack represented by the item or null if not a backpack
     */
    @Contract("null -> null")
    @Nullable Backpack loadFromItem(@Nullable ItemStack item);

    /**
     * Saves backpack data to a given item's meta.
     * <p>
     * <strong>{@code item} will be modified.</strong>
     *
     * @param backpack a backpack
     * @param item an item
     * @return true only if the item's meta was updated
     */
    boolean saveToItem(@NotNull Backpack backpack, @NotNull ItemStack item);

    /**
     * Prepares a backpack view.
     * <p>
     * {@code update} should return false if the update is unsuccessful.
     *
     * @param backpack a backpack
     * @param update a callback to update the backpack
     * @return a backpack view
     */
    @NotNull BackpackView open(@NotNull Backpack backpack, @Nullable Function<? super Backpack, Boolean> update);

    /**
     * Gets the current service instance.
     *
     * @return the current service instance
     * @throws IllegalStateException if no service yet registered
     */
    static @NotNull BackpackService getInstance() {
        final var instance = Bukkit.getServicesManager().load(BackpackService.class);
        if (instance == null) throw new IllegalStateException("BackpackService not registered");
        return instance;
    }
}
