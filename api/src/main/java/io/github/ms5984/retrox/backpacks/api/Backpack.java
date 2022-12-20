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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a backpack.
 *
 * @since 0.0.1
 * @author ms5984
 */
@ApiStatus.NonExtendable
public interface Backpack {
    /**
     * Gets the number of rows this backpack has.
     *
     * @return the number of rows
     */
    int rows();

    /**
     * Checks if the backpack supports item collection, and if so,
     * checks if item collect is currently enabled.
     *
     * @return {@code null} if item collect is not supported,
     * {@code true} if enabled, {@code false} if disabled
     */
    default @Nullable Boolean itemCollect() {
        return null;
    }

    /**
     * Copies this backpack.
     *
     * @return a copy of this backpack and its content
     */
    @NotNull Backpack copy();

    /**
     * Creates a new, empty backpack.
     * <p>
     * Delegates to {@link BackpackService#create()}.
     *
     * @return a new backpack
     * @throws IllegalStateException if no {@linkplain BackpackService}
     * yet registered
     */
    static @NotNull Backpack create() {
        return BackpackService.getInstance().create();
    }
}
