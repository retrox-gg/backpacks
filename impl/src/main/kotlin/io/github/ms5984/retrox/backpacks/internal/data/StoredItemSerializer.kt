package io.github.ms5984.retrox.backpacks.internal.data
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

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import org.bukkit.inventory.ItemStack
import java.util.Base64

object StoredItemSerializer : TypeAdapter<StoredItems>() {
    val gson : Gson = GsonBuilder().registerTypeAdapter(StoredItems::class.java, this).create()

    // [{i:ABC192038198}, {}, {}]
    override fun write(writer: JsonWriter, value: StoredItems?) {
        if (value == null) {
            writer.nullValue()
            return
        }
        writer.beginArray()
        value.items.forEach { (position, item) ->
            writer.beginObject()
                .name(position.toString()).apply {
                    value(Base64.getEncoder().encodeToString(item.serializeAsBytes()))
                }.endObject()
        }
        writer.endArray()
    }

    override fun read(reader: JsonReader): StoredItems? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }
        reader.beginArray()
        val items = StoredItems()
        while (reader.hasNext()) {
            reader.beginObject()
            val position = reader.nextName().toInt()
            val bytes = Base64.getDecoder().decode(reader.nextString())
            items.setItem(position, ItemStack.deserializeBytes(bytes))
            reader.endObject()
        }
        reader.endArray()
        return items
    }
}