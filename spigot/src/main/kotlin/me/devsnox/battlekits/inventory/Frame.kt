package me.devsnox.battlekits.inventory

import net.darkdevelopers.darkbedrock.darkness.spigot.builder.inventory.InventoryBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

@Suppress("LeakingThis")
abstract class Frame(
    size: Int,
    displayName: String,
    private val isNoItemMove: Boolean
) {

    var inventory: Inventory = InventoryBuilder(size, displayName).build()

    open fun render(player: Player) {
        player.openInventory(inventory)
    }

    open fun onClick(event: InventoryClickEvent) {
        if (isNoItemMove) event.cancel()
    }

    open fun onClose(event: InventoryCloseEvent) {}

    fun fill() {
        val itemBuilder = ItemBuilder(Material.STAINED_GLASS_PANE).setDisplayName(" ").setDurability(7)
        for (i in 0 until inventory.size) inventory.setItem(i, itemBuilder.build())
    }

    override fun toString(): String = "Frame(isNoItemMove=$isNoItemMove, inventory=${inventory.name})"

}
