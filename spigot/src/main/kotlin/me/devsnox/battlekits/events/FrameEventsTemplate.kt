package me.devsnox.battlekits.events

import me.devsnox.battlekits.functions.frames
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.Plugin

object FrameEventsTemplate : EventsTemplate(), Setupable {

    override fun setup(plugin: Plugin) {
        listen<InventoryClickEvent>(plugin) { event ->
            val frame = frames.find {
                it.inventory == event.whoClicked.openInventory.topInventory && it.inventory == event.clickedInventory
            }
            frame?.onClick(event)
        }.add()
        listen<InventoryCloseEvent>(plugin) { event ->
            val frame = frames.find { it.inventory.name == event.inventory?.name }
            frame?.onClose(event)
        }.add()
    }

}
