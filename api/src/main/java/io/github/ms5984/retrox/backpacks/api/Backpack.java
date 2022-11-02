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

import io.github.ms5984.retrox.backpacks.api.items.BackpackMetaTool;
import io.github.ms5984.retrox.backpacks.api.model.BackpackId;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a backpack.
 *
 * @since 0.0.1
 * @author ms5984
 */
@ApiStatus.NonExtendable
public interface Backpack {
    /**
     * Get the id of this backpack.
     *
     * @return the id of this backpack
     * @see BackpackId#randomId()
     * @see BackpackId#fromUuid(UUID) 
     * @see BackpackId#fromString(String)
     * @see BackpackId#fromBytes(byte[])
     */
    @NotNull BackpackId id();

    /**
     * Get the meta tool for this backpack.
     * <p>
     * Useful for transforming items.
     *
     * @return the meta tool for this backpack
     */
    @NotNull BackpackMetaTool metaTool();
}
