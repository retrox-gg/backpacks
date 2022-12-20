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

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a backpack to be viewed.
 *
 * @since 0.0.1
 * @author ms5984
 */
public interface BackpackView {
    /**
     * Gets the backpack associated with this view.
     *
     * @return the backpack
     */
    @NotNull Backpack getBackpack();

    /**
     * Adds a player to this view.
     *
     * @param player a player
     */
    void view(@NotNull Player player);
}
