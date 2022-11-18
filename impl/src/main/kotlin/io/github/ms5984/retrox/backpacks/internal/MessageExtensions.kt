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

import io.github.ms5984.commonlib.taglib.TagLib
import org.bukkit.configuration.ConfigurationSection

private fun asRenderedMessage(message: String?) : Messages.Rendered {
    return (message?.let { TagLib.ampersand(it).process() } ?: "null")
        .let { Messages.miniMessage.deserialize(it) }
        .let { Messages.Rendered { it } }
}

fun ConfigurationSection.getRenderedMessage(key: String): Messages.Rendered {
    return asRenderedMessage(getString(key))
}

fun ConfigurationSection.getRenderedMessageList(key: String): List<Messages.Rendered> {
    return getStringList(key).map { asRenderedMessage(it) }
}
