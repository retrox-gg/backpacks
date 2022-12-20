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

import java.util.Optional;
import java.util.Set;

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
    private @Nullable Set<Integer> customSlots;

    /**
     * Creates a new GUI control draw event.
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
     * Gets the type of control being drawn.
     *
     * @return the type of control
     */
    public GUIControl getType() {
        return type;
    }

    /**
     * Gets the item placed for this control.
     *
     * @return the item that will be placed
     */
    public @NotNull ItemStack getFinalItem() {
        return finalItem;
    }

    /**
     * Sets the item placed for this control.
     *
     * @param finalItem the item that will be placed
     * @return this event
     */
    public GUIControlDrawEvent setFinalItem(@NotNull ItemStack finalItem) {
        this.finalItem = finalItem;
        return this;
    }

    /**
     * Gets the alternate slots this control will be placed in, if defined.
     *
     * @return an Optional describing the alternate slots if present
     */
    public @NotNull Optional<Set<Integer>> getCustomSlots() {
        return Optional.ofNullable(customSlots);
    }

    /**
     * Sets the alternate slot or slots this control will be placed in.
     * <p>
     * If {@code customSlots} is null default slots will be used
     * (see {@link GUIControl#getDefaultSlots()}).
     *
     * @param customSlots a set of alternate slots or null
     * @return this event
     */
    public GUIControlDrawEvent setCustomSlots(@Nullable Set<@NotNull Integer> customSlots) {
        this.customSlots = (customSlots != null) ? Set.copyOf(customSlots) : null;
        return this;
    }

    /**
     * Gets the final slot or slots this control will be placed in.
     * <p>
     * If {@link #getCustomSlots()} is empty this method will return the
     * defaults as defined by {@link #getType()}.
     *
     * @return the final set of slots
     */
    public @NotNull Set<Integer> getSlots() {
        return getCustomSlots().orElseGet(type::getDefaultSlots);
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
