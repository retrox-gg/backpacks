package io.github.ms5984.retrox.backpacks.internal.gui;
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

import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Represents a GUI control.
 *
 * @since 0.0.1
 * @author ms5984
 */
public enum GUIControl {
    /**
     * Return to the previous page.
     */
    PREV(0),
    /**
     * Advance to the next page.
     */
    NEXT(8),
    /**
     * Close the GUI.
     */
    CLOSE(Set.of(2, 3, 4, 5, 6, 7)),
    ;

    private final Set<Integer> defaultSlots;

    GUIControl(int defaultSlot) {
        this(Set.of(defaultSlot));
    }

    GUIControl(Set<@NotNull Integer> defaultSlots) {
        this.defaultSlots = Set.copyOf(defaultSlots);
    }

    /**
     * Get the default slot or slots this control is drawn in.
     * <p>
     * Each index is relative to the bottom row of the GUI.
     *
     * @return the slot or slots where this control will be drawn
     */
    public @NotNull Set<@NotNull Integer> getDefaultSlots() {
        return defaultSlots;
    }
}
