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

import io.github.ms5984.retrox.backpacks.api.BackpackService
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.plugin.ServicePriority.Normal
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class BackpacksPlugin : JavaPlugin() {
    val backpackKey = NamespacedKey(this, "backpack")
    private val backpackService = BackpackServiceImpl(this)

    override fun onEnable() {
        // Plugin startup logic
        instance = this
        Bukkit.getServicesManager().register(BackpackService::class.java, backpackService, this, Normal)
        Commands(this).initCommands()
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object {
        lateinit var instance: BackpacksPlugin
            private set
    }
}
