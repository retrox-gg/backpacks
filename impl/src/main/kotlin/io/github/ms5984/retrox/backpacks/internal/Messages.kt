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
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.jetbrains.annotations.PropertyKey
import java.util.PropertyResourceBundle

object Messages {
    private val bundle = PropertyResourceBundle.getBundle("lang/messages")
    val miniMessage = MiniMessage.miniMessage()

    fun get(@PropertyKey(resourceBundle = "lang.messages") key: String): LangString =
        LangString(key)

    fun getAsList(@PropertyKey(resourceBundle = "lang.messages") key: String): List<Chaining> =
        LangString(key).splitOnNewlines()

    @Suppress("MemberVisibilityCanBePrivate")
    sealed class Raw(val text: String) : Rendered {
        override fun asComponent() = miniMessage.deserialize(text)
        fun splitOnNewlines(): List<Chaining> = text.split("\\n").map { Chaining(it) }
        fun asDisplayName(): Component = miniMessage.deserialize(TagLib.ampersand(text).displayNameOverride())
        fun asLoreLine(): Component = miniMessage.deserialize(TagLib.ampersand(text).loreLineOverride())
    }

    class Chaining(text: String) : Raw(text)
    class LangString(@PropertyKey(resourceBundle = "lang.messages") val key: String) : Raw(bundle.getString(key))

    fun interface Rendered : ComponentLike {
        fun send(audience: Audience) = audience.sendMessage(this)
        fun sendConsole() = Bukkit.getConsoleSender().sendMessage(this)
    }
}
