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

import java.nio.ByteBuffer;
import java.util.UUID;

@ApiStatus.Internal
record BackpackIdImpl(UUID uuid) implements BackpackId {
    BackpackIdImpl(byte[] bytes) {
        this(unwrap(bytes));
    }

    BackpackIdImpl(String stringRepresentation) {
        this(sanityCheck(stringRepresentation));
    }

    @Override
    public @NotNull UUID asUid() {
        return uuid;
    }

    @Override
    public @NotNull String asString() {
        return uuid.toString();
    }

    @Override
    public byte @NotNull [] asBytes() {
        return ByteBuffer.wrap(new byte[16])
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits())
                .array();
    }

    static UUID unwrap(byte[] bytes) {
        if (bytes.length != 16) throw new IllegalArgumentException("bytes must be 16 bytes long");
        final ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    static UUID sanityCheck(String stringRepresentation) {
        try {
            return UUID.fromString(stringRepresentation);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("stringRepresentation is not a valid backpack id", e);
        }
    }
}
