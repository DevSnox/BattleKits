package me.devsnox.battlekits.events

import de.tr7zw.itemnbtapi.NBTItem
import me.devsnox.battlekits.KitManager
import me.devsnox.itemprotection.api.ItemProtectionFactory
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.removeItemInHand
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Chest
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.Plugin

class KitEventsTemplate(private val kitManager: KitManager) : EventsTemplate(), Setupable {

    override fun setup(plugin: Plugin) {
        listen<PlayerInteractEvent>(plugin) { event ->
            val player = event.player ?: return@listen
            val itemStack = event.item ?: return@listen
            val block = event.clickedBlock?.getRelative(BlockFace.UP) ?: return@listen

            if (event.action != Action.RIGHT_CLICK_BLOCK) return@listen
            if (itemStack.type != Material.STORAGE_MINECART) return@listen

            val nbtItem = NBTItem(itemStack)
            if (!nbtItem.hasKey("kitId")) return@listen
            val id = nbtItem.getInteger("kitId") ?: return@listen

            if (!kitManager.kits.containsKey(id)) return@listen
            val protectionAPI = ItemProtectionFactory.itemProtection ?: return@listen
            if (protectionAPI.alreadyUsed(itemStack)) return@listen

            block.type = Material.CHEST

            val chest = block.state as Chest
            val items = kitManager.kits[id]?.items ?: listOf()

            for (i in items.indices) {
                val item = items.getOrNull(i) ?: continue
                chest.blockInventory.setItem(i, item)
            }
            protectionAPI.validate(itemStack)

            player.removeItemInHand()
            event.cancel()
        }.add()
    }

}
