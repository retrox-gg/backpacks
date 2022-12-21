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

data class BackpackImpl(val items: StoredItems = StoredItems(), val options: HashMap<String, Any> = HashMap()) : Backpack {
    internal var rows: Int
        get() = (options["rows"] as? Long)?.toInt() ?: 1
        set(value) { options["rows"] = value }

    internal var itemCollect: Boolean?
        get() = options["itemCollect"] as? Boolean
        set(value) { value?.let { options["itemCollect"] = it } ?: options.remove("itemCollect") }

    var lorePreview: Boolean?
        get() = options["lorePreview"] as? Boolean
        internal set(value) { value?.let { options["lorePreview"] = it } ?: options.remove("lorePreview") }

    override fun rows() = rows
    override fun itemCollect() = itemCollect
    override fun copy() = BackpackImpl(StoredItems(items), HashMap(options))
}
