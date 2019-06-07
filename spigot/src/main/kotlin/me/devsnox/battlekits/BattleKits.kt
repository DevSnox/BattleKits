package me.devsnox.battlekits

import me.devsnox.battlekits.commands.BoosterCommand
import me.devsnox.battlekits.commands.KitCommand
import me.devsnox.battlekits.commands.KitEditorCommand
import me.devsnox.battlekits.commands.ShardCommand
import me.devsnox.battlekits.events.*
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class BattleKits : JavaPlugin() {

    private var kitManager: KitManager = KitManager(this)
    private val events: Set<Setupable> = setOf(
        KitEventsTemplate(kitManager),
        PlayerEventsTemplate(kitManager),
        ShardEventsTemplate(kitManager),
        FrameEventsTemplate
    )

    override fun onEnable() {
        kitManager.enable()
        initEvents()
        initCommands()
    }

    override fun onDisable() {
        kitManager.disable()
        FrameEventsTemplate.reset()
    }

    private fun initEvents(): Unit = events.forEach { it.setup(this) }

    private fun initCommands() {
        BoosterCommand(kitManager)
        KitCommand(kitManager)
        KitEditorCommand(kitManager)
        ShardCommand(kitManager)
    }
}
