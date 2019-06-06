package me.devsnox.battlekits.kits.frames.playerview

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.frames
import me.devsnox.battlekits.inventory.Frame
import me.devsnox.battlekits.kits.entites.KitPlayer
import me.devsnox.battlekits.kits.kit.KitType
import me.devsnox.battlekits.kits.kit.toKitType
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.glow
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class KitSelectionFrame(private val kitManager: KitManager) : Frame(54, "§c§lKits", true) {
    private var kitPlayer: KitPlayer? = null

    init {
        frames += this
    }

    fun render(player: Player, target: KitType) {
        kitPlayer = kitManager.getPlayer(player.uniqueId)
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
            itemBuilder.setDisplayName(kitType.color.toString() + kitType.displayName)
            if (target == kitType) {
                itemBuilder.glow()
                addKits(11, kitType)
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
        if (!nbtItem.hasKey("kitId")) {
            if (nbtItem.hasKey("type") && !nbtItem.item.itemMeta.hasEnchants())
                render(player, nbtItem.toKitType() ?: return)
        } else {
            val id = nbtItem.getInteger("kitId")
            val kitPlayer = kitPlayer ?: return
            if (kitPlayer.kits.containsKey(id)) {
                val frame = KitInformationFrame(this, kitPlayer.kits[id] ?: return)
                frame.render(player)
            } else {
                //TODO: send message
            }
        }
        super.onClick(event)
    }

    private fun addKits(start: Int, kitType: KitType) {
        val kits = kitManager.getKitsByType(kitType)
        for (i in start until start + kits.size) {
            val battleKit = kits[i - start]
            val itemBuilder = ItemBuilder(battleKit.icon.type)
            val kitPlayer = kitPlayer ?: return
            val extra: String = "§8[" + when {
                !kitPlayer.kits.containsKey(battleKit.id) -> {
                    itemBuilder.setType(Material.EXPLOSIVE_MINECART)
                    "§cGESPERRT"
                }
                kitPlayer.kits[battleKit.id]?.checkReceive() == true -> {
                    itemBuilder.setType(Material.STORAGE_MINECART)
                    itemBuilder.glow()
                    "§aAbholbar"
                }
                else -> {
                    itemBuilder.setType(Material.MINECART)
                    "§6Cooldown"
                }
            } + "§8]"
            itemBuilder.setDisplayName("${kitType.color}${kitType.displayName} §8| ${kitType.color}${battleKit.name} $extra")
            itemBuilder.addLore("§aBeschreibung:", "§7${battleKit.description}")
            inventory.setItem(i, NBTItem(itemBuilder.build()).apply { setInteger("kitId", battleKit.id) }.item)
        }
    }

    private fun calc(start: Int) {
        for (i in start until start + 6) inventory.setItem(i, ItemStack(Material.AIR))
    }
}
