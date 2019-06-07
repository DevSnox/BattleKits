package me.devsnox.battlekits.kits.frames.editor

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.backItem
import me.devsnox.battlekits.functions.filLBattle
import me.devsnox.battlekits.inventory.Frame
import me.devsnox.battlekits.kits.kit.BattleKit
import me.devsnox.battlekits.kits.kit.KitType
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class KitEditorEditKitItemsFrame(
    private val kitManager: KitManager,
    private val kitEditorViewFrame: KitEditorViewFrame,
    private val battleKit: BattleKit
) : Frame(
    45,
    "§a§lEditor: ${battleKit.kitType.color}§l${battleKit.name}",
    false
) {

    override fun render(player: Player) {
        fill()
        val itemBuilder = ItemBuilder(battleKit.icon.type)
            .setDisplayName("${battleKit.kitType.color}${battleKit.name}")
            .addLore(battleKit.description)

        inventory.setItem(4, itemBuilder.build())
        inventory.setItem(7, backItem)
        inventory.filLBattle(battleKit.items)

        super.render(player)
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val itemStack = event.currentItem ?: return
        if (itemStack.type == Material.AIR) return
        if (event.slot < 18) event.cancel()

        val nbtItem = NBTItem(itemStack)
        if (nbtItem.hasKey("back")) {
            updateKit()
            kitEditorViewFrame.render(player, KitType.RARE)
        }
        super.onClick(event)
    }

    override fun onClose(event: InventoryCloseEvent) {
        updateKit()
        super.onClose(event)
    }

    private fun updateKit() {
        battleKit.items = inventory.drop(18)
        kitManager.updateKit(battleKit)
    }

}

