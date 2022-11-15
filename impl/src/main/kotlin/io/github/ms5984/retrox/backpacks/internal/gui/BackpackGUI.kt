package io.github.ms5984.retrox.backpacks.internal.gui
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

import io.github.ms5984.retrox.backpacks.internal.BackpackImpl
import org.bukkit.entity.Player

data class BackpackGUI(val backpack: BackpackImpl, val player: Player) {
    val pages
        get() = backpack.rows / MAX_ROWS_DISPLAYED + if (backpack.rows % MAX_ROWS_DISPLAYED == 0) 0 else 1

    /**
     * Render a page of the backpack GUI.
     *
     * Indexed by natural number (1, 2, 3, ...so on).
     * @param page page to render
     */
    fun page(page: Int) {
        if (page !in 1..pages) {
            throw IllegalArgumentException("page must be between 1 and $pages")
        }
        // Last page may have fewer rows
        Render(this, page, if (page == pages) backpack.rows % MAX_ROWS_DISPLAYED else MAX_ROWS_DISPLAYED).open(player)
    }
}
