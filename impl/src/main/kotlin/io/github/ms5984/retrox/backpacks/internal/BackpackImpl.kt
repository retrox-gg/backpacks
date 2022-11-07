package io.github.ms5984.retrox.backpacks.internal
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

import io.github.ms5984.retrox.backpacks.api.Backpack
import io.github.ms5984.retrox.backpacks.internal.data.StoredItems
import io.github.ms5984.retrox.backpacks.internal.items.BackpackMetaToolImpl
import org.bukkit.entity.Player

data class BackpackImpl(val items: StoredItems = StoredItems(), var extraRows: Int = 0) : Backpack {
    override fun open(player: Player) {
        TODO("Not yet implemented")
    }

    override fun extraRows() = extraRows

    override fun metaTool() = BackpackMetaToolImpl(this)

    override fun copy() = BackpackImpl(StoredItems(items))
}
