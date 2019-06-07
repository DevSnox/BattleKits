package me.devsnox.battlekits.commands

import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.javaPlugin
import me.devsnox.battlekits.kits.frames.editor.KitEditorViewFrame
import me.devsnox.battlekits.kits.kit.KitType
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.command.CommandSender

class KitEditorCommand(private val kitManager: KitManager) : Command(
    javaPlugin,
    "KitEditor",
    "battlekits.kiteditor"
) {
    override fun perform(sender: CommandSender, args: Array<String>): Unit = sender.isPlayer { player ->
        KitEditorViewFrame(kitManager).render(player, KitType.RARE)
    }
}
