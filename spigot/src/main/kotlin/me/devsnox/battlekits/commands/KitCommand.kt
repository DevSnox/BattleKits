package me.devsnox.battlekits.commands

import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.javaPlugin
import me.devsnox.battlekits.kits.frames.playerview.KitSelectionFrame
import me.devsnox.battlekits.kits.kit.KitType
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.command.CommandSender

class KitCommand(private val kitManager: KitManager) : net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command(
    javaPlugin,
    "Kit",
    "battlekits.kit"
) {
    override fun perform(sender: CommandSender, args: Array<String>): Unit = sender.isPlayer { player ->
        KitSelectionFrame(kitManager).render(player, KitType.RARE)
    }
}
