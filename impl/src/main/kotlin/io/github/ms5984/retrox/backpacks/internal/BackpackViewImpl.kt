package io.github.ms5984.retrox.backpacks.internal

import io.github.ms5984.retrox.backpacks.api.Backpack
import io.github.ms5984.retrox.backpacks.api.BackpackView
import io.github.ms5984.retrox.backpacks.internal.gui.BackpackGUI
import org.bukkit.entity.Player
import java.util.function.Function

class BackpackViewImpl(private val backpack: BackpackImpl, private val update: Function<in Backpack, Boolean>?): BackpackView {
    override fun getBackpack() = backpack

    override fun view(player: Player) {
        if (update != null) {
            BackpackGUI(backpack, player) { update.apply(it) }
        } else {
            BackpackGUI(backpack, player)
        }.page(1)
    }
}
