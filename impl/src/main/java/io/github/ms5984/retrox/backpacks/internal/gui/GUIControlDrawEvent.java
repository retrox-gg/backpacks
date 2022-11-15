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

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a GUI control is being drawn.
 *
 * @since 0.0.1
 * @author ms5984
 */
@SuppressWarnings("unused")
public final class GUIControlDrawEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList(); // for Event contract
    final GUIControl type;
    private ItemStack finalItem;
    private @Nullable Integer customSlot;

    /**
     * Create a new GUI control draw event.
     *
     * @param type the type of control being drawn
     * @param item an item representing the control
     */
    @ApiStatus.Internal
    public GUIControlDrawEvent(@NotNull GUIControl type, @NotNull ItemStack item) {
        this.type = type;
        this.finalItem = item;
    }

    /**
     * Get the type of control being drawn.
     *
     * @return the type of control
     */
    public GUIControl getType() {
        return type;
    }

    /**
     * Get the item placed for this control.
     *
     * @return the item that will be placed
     */
    public @NotNull ItemStack getFinalItem() {
        return finalItem;
    }

    /**
     * Set the item placed for this control.
     *
     * @param finalItem the item that will be placed
     * @return this event
     */
    public GUIControlDrawEvent setFinalItem(@NotNull ItemStack finalItem) {
        this.finalItem = finalItem;
        return this;
    }

    /**
     * Get the alternate slot this control will be placed in.
     * <p>
     * If null, the default slot will be used (see
     * {@link GUIControl#getDefaultSlot()}).
     *
     * @return an alternate slot or null
     */
    public @Nullable Integer getCustomSlot() {
        return customSlot;
    }

    /**
     * Set the alternate slot this control will be placed in.
     * <p>
     * If {@code customSlot} is null the default slot will be used (see
     * {@link GUIControl#getDefaultSlot()}).
     *
     * @param customSlot an alternate slot or null
     * @return this event
     */
    public GUIControlDrawEvent setCustomSlot(@Nullable Integer customSlot) {
        this.customSlot = customSlot;
        return this;
    }

    /**
     * Get the final slot this control will be placed in.
     * <p>
     * If {@link #getCustomSlot()} is null, this method will return the default
     * slot as defined by {@link #getType()}.
     *
     * @return the final slot
     */
    public int getSlot() {
        return customSlot == null ? type.getDefaultSlot() : customSlot;
    }

    // required by Event contract
    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
