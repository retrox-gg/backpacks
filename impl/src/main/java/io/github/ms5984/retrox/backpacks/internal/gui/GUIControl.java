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

import org.jetbrains.annotations.Range;

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
    ;

    private final int defaultSlot;

    GUIControl(int defaultSlot) {
        this.defaultSlot = defaultSlot;
    }

    /**
     * Get the default slot this control is drawn in.
     * <p>
     * This index is relative to the bottom row of the GUI.
     *
     * @return the slot where this control will be drawn
     */
    public @Range(from = 0, to = 8) int getDefaultSlot() {
        return defaultSlot;
    }
}
