package me.devsnox.battlekits.events

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.kits.frames.shard.ShardOpenFrame
import me.devsnox.itemprotection.api.ItemProtectionFactory
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class ShardEventsTemplate(private val kitManager: KitManager) : EventsTemplate(), Setupable {

    override fun setup(plugin: Plugin) {
        listen<PlayerInteractEvent>(plugin) { event ->
            val player = event.player ?: return@listen
            val itemStack = event.item ?: return@listen
            if (itemStack.type == Material.AIR) return@listen
            val nbtItem = NBTItem(itemStack)

            if (itemStack.type != Material.PRISMARINE_CRYSTALS) return@listen
            if (!nbtItem.hasKey("kitId")) return@listen
            val id = nbtItem.getInteger("kitId")

            val itemProtectionAPI = ItemProtectionFactory.itemProtection ?: return@listen
            if (itemProtectionAPI.alreadyUsed(itemStack)) return@listen
            if (kitManager.isOpening(player)) return@listen
            val battleKit = kitManager.kits[id] ?: return@listen

            val shardOpenFrame = ShardOpenFrame(kitManager, battleKit, nbtItem.getDouble("chance"))
            shardOpenFrame.render(player)

            itemProtectionAPI.validate(itemStack)
            player.itemInHand = ItemStack(Material.AIR)

        }.add()
        listen<InventoryClickEvent>(plugin) { event ->
            val player = event.whoClicked as? Player ?: return@listen
            val shard = event.currentItem ?: return@listen
            val booster = event.cursor ?: return@listen
            val nbtShard = NBTItem(shard)
            val nbtBooster = NBTItem(booster)

            if (shard.type != Material.PRISMARINE_CRYSTALS) return@listen
            if (booster.type != Material.GOLD_NUGGET) return@listen
            if (!nbtShard.hasKey("chance")) return@listen
            if (!nbtBooster.hasKey("boost")) return@listen

            nbtShard.setDouble("chance", nbtShard.getDouble("chance") + nbtBooster.getDouble("boost"))
            player.inventory.setItem(event.slot, nbtShard.item)

            if (booster.amount != 1) booster.amount -= 1 else booster.type = Material.AIR
            player.inventory.addItem(booster)
        }.add()
    }

}
