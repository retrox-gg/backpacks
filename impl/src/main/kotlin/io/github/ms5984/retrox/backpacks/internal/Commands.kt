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
import io.github.ms5984.retrox.accessories.api.AccessoryService
import io.github.ms5984.retrox.backpacks.api.BackpackService
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.jetbrains.annotations.Nullable

private const val PERMISSION_ROOT = "backpacks"

data class Commands(private val plugin: BackpacksPlugin) {
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
            // TODO: make proper backpack item
            ItemStack(Material.ACACIA_BOAT).also {
                it.editMeta { meta ->
                    meta.displayName(Messages.miniMessage.deserialize("<aqua>Backpack"))
                    meta.lore(listOf(Messages.miniMessage.deserialize("<!i><white>I'm gonna store items someday!!")))
                }
                // apply backpack category
                AccessoryService.getInstance().addNBT(it) { "backpack" } // TODO: this is a hack, i need to fix categories
                // apply backpack
                BackpackService.getInstance().create().also { backpack ->
                    // stick the styled item in the backpack
                    backpack as BackpackImpl
                    backpack.items.setItem(0, it)
                }.metaTool().apply(it)
            }.also { targetPlayer.inventory.addItem(it) }.also { plugin.logger.info("$it") }
            return
        }
        Messages.get("fail.command.target.playerOrSelf").send(sender)
    }
}