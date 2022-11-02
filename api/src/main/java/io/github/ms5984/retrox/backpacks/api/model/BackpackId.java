package io.github.ms5984.retrox.backpacks.api.model;
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

import java.util.UUID;

/**
 * Uniquely identifies a backpack.
 *
 * @since 0.0.1
 * @author ms5984
 */
@ApiStatus.NonExtendable
public interface BackpackId {
    /**
     * Get this id as a uuid.
     *
     * @return this id as a uuid
     */
    @NotNull UUID asUid();

    /**
     * Get this id as a string.
     *
     * @return this id as a string
     */
    @NotNull String asString();

    /**
     * Get this id as a byte array.
     *
     * @return this id as a new byte array
     */
    byte @NotNull [] asBytes();

    /**
     * Create a new, random backpack id.
     *
     * @return a new, random backpack id
     */
    static BackpackId randomId() {
        return new BackpackIdImpl(UUID.randomUUID());
    }

    /**
     * Create a new backpack id from an existing uuid.
     *
     * @param uuid a uuid
     * @return the respective backpack id
     */
    static BackpackId fromUuid(@NotNull UUID uuid) {
        return new BackpackIdImpl(uuid);
    }

    /**
     * Create a backpack id from its string representation.
     *
     * @param id a backpack id string representation
     * @return the respective backpack id
     * @throws IllegalArgumentException if {@code id}
     * does not represent a valid backpack id
     */
    static BackpackId fromString(@NotNull String id) throws IllegalArgumentException {
        return new BackpackIdImpl(id);
    }

    /**
     * Create a backpack id from its byte array representation.
     *
     * @param bytes a backpack id byte array representation
     * @return the respective backpack id
     * @throws IllegalArgumentException if {@code bytes}
     * does not represent a valid backpack id
     */
    static BackpackId fromBytes(byte[] bytes) throws IllegalArgumentException {
        return new BackpackIdImpl(bytes);
    }
}
