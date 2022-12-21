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

import cloud.commandframework.annotations.*
import cloud.commandframework.execution.CommandExecutionCoordinator
import cloud.commandframework.meta.SimpleCommandMeta
import cloud.commandframework.paper.PaperCommandManager
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.annotations.Nullable

private const val PERMISSION_ROOT = "backpacks"

class Commands(private val plugin: BackpacksPlugin) {
    private val manager = PaperCommandManager.createNative(
        plugin,
        CommandExecutionCoordinator.simpleCoordinator()
    )

    fun initCommands() {
        AnnotationParser(manager, CommandSender::class.java) { SimpleCommandMeta.empty() }.parse(this)
    }

    @CommandMethod("backpacks|bp give [player]")
    @CommandPermission("$PERMISSION_ROOT.give")
    @CommandDescription("Give a player a backpack")
    fun giveCommand(sender: CommandSender, @Argument("player") @Nullable player: Player?) {
        val target = player ?: sender as? Player
        target?.let { targetPlayer ->
            plugin.givePreset.item.let {
                // try to add item to target's inventory
                val result = targetPlayer.inventory.addItem(it)
                // message
                when (sender) {
                    targetPlayer -> {
                        // message receiver == sender
                        getResponse("give.received.unspecified")?.send(sender)
                    }
                    else -> {
                        // message receiver
                        getResponse("give.received.attributed_")?.resolveWith(
                            if (sender is Player) Placeholder.component("sender", sender.displayName())
                            else Placeholder.unparsed("sender", sender.name)
                        )?.send(targetPlayer)
                        // message sender
                        getResponse("give.sender_")?.resolveWith(
                            Placeholder.component("player", targetPlayer.displayName())
                        )?.send(sender)
                    }
                }
                if (result.isNotEmpty()) {
                    // drop item
                    targetPlayer.world.dropItem(targetPlayer.location, it)
                    // message sender
                    getResponse("give.dropped")?.send(sender)
                    if (targetPlayer != sender) {
                        // also message receiver
                        getResponse("give.dropped")?.send(targetPlayer)
                    }
                }
            }
            return
        }
        Messages.get("fail.command.target.playerOrSelf").send(sender)
    }

    private fun getResponse(path: String): Messages.Raw? = plugin.config.getString("responses.$path")?.let { Messages.Raw(it) }
}
