package me.devsnox.battlekits.events

import me.devsnox.battlekits.KitManager
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

class PlayerEventsTemplate(private val kitManager: KitManager) : EventsTemplate(), Setupable {

    override fun setup(plugin: Plugin) {
        listen<PlayerJoinEvent>(plugin) { event ->
            kitManager.loadPlayer(event.player.uniqueId)
        }.add()
        listen<PlayerDisconnectEvent>(plugin) { event ->
            kitManager.savePlayer(event.player.uniqueId)
        }.add()
    }

}
