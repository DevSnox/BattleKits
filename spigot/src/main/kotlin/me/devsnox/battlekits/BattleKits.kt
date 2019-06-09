package me.devsnox.battlekits

import com.google.gson.JsonObject
import kotlinx.coroutines.runBlocking
import me.devsnox.battlekits.commands.BoosterCommand
import me.devsnox.battlekits.commands.KitCommand
import me.devsnox.battlekits.commands.KitEditorCommand
import me.devsnox.battlekits.commands.ShardCommand
import me.devsnox.battlekits.configs.SQLStatements
import me.devsnox.battlekits.configs.sqlStatements
import me.devsnox.battlekits.events.*
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.save
import net.darkdevelopers.darkbedrock.darkness.general.functions.toConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class BattleKits : JavaPlugin() {

    private val kitManager by lazy { KitManager(this) }
    private val events by lazy {
        setOf<Setupable>(
            KitEventsTemplate(kitManager),
            PlayerEventsTemplate(kitManager),
            ShardEventsTemplate(kitManager),
            FrameEventsTemplate
        )
    }

    override fun onEnable() {

        val configData = dataFolder.toConfigData("sql-statements", prefix = "DOT_NOT_CHANGE_THIS_CONFIG.")
        sqlStatements = SQLStatements(configData.load<JsonObject>().toMap())
        configData.save(sqlStatements.toConfigMap())

        runBlocking { kitManager.enable() }
        initEvents()
        initCommands()
    }

    override fun onDisable() {
        runBlocking { kitManager.disable() }
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
