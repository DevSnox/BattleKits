package me.devsnox.battlekits.commands

import me.devsnox.battlekits.KitManager
import me.devsnox.battlekits.functions.javaPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendTo
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender

class ShardCommand(private val kitManager: KitManager) : Command(
    javaPlugin,
    "Shard",
    "battlekits.getshard",
    usage = "<id>",
    minLength = 1,
    maxLength = 1
) {
    override fun perform(sender: CommandSender, args: Array<String>): Unit = sender.isPlayer { player ->
        val id = args[0].toIntOrNull()
        if (id == null) "${ChatColor.RED}Gib eine Ganzzahl an!".sendTo(sender)
        else player.inventory.addItem(kitManager.getShard(id))
    }
}
