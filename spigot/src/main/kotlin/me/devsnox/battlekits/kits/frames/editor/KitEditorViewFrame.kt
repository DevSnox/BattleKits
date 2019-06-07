package me.devsnox.battlekits.kits.frames.editor

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.frames
import me.devsnox.battlekits.inventory.Frame
import me.devsnox.battlekits.kits.kit.KitType
import me.devsnox.battlekits.kits.kit.toKitType
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.glow
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class KitEditorViewFrame(private val kitManager: KitManager) : Frame(54, "§a§lEditor: §c§lKits", true) {

    init {
        frames += this
    }

    fun render(player: Player, target: KitType) {
        fill()
        var start = 11
        for (i in 0..3) {
            calc(start)
            start += 9
        }
        var i = 9
        for (kitType in KitType.values()) {
            val itemBuilder = ItemBuilder(Material.STAINED_GLASS_PANE)
            when (i) {
                9 -> itemBuilder.setDurability(3)
                18 -> itemBuilder.setDurability(2)
                27 -> itemBuilder.setDurability(1)
                36 -> itemBuilder.setDurability(14)
            }
            itemBuilder.setDisplayName("${kitType.color}${kitType.displayName}")
            if (target == kitType) {
                itemBuilder.glow()
                addKits(kitType = kitType)
            }
            val nbtItem = NBTItem(itemBuilder.build())
            nbtItem.setString("type", kitType.displayName)
            inventory.setItem(i, nbtItem.item)
            i += 9
        }
        super.render(player)
    }

    override fun onClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val itemStack = event.currentItem ?: return
        if (itemStack.type == Material.AIR) return

        val nbtItem = NBTItem(itemStack)
        if (nbtItem.hasKey("kitId")) {
            val id = nbtItem.getInteger("kitId")
            val frame = KitEditorEditKitItemsFrame(kitManager, this, kitManager.kits[id] ?: return)
            frame.render(player)
            frames += frame
        } else if (nbtItem.hasKey("type") && !nbtItem.item.itemMeta.hasEnchants())
            render(player, nbtItem.toKitType() ?: return)
        super.onClick(event)
    }

    private fun addKits(start: Int = 11, kitType: KitType) {
        val kits = kitManager.getKitsByType(kitType)
        for (i in start until start + kits.size) {
            val battleKit = kits[i - start]
            val itemBuilder = ItemBuilder(battleKit.icon.type)
                .setDisplayName("${kitType.color}${kitType.displayName} §8| ${kitType.color}${battleKit.name}")
                .addLore("§aBeschreibung:", "§7${battleKit.description}")
            inventory.setItem(i, NBTItem(itemBuilder.build()).apply { setInteger("kitId", battleKit.id) }.item)
        }
    }

    private fun calc(start: Int) {
        for (i in start until start + 6) inventory.setItem(i, ItemStack(Material.AIR))
    }
}
