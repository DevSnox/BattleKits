package me.devsnox.battlekits.kits.kit

import de.tr7zw.itemnbtapi.NBTItem
import org.bukkit.ChatColor

enum class KitType(
    val color: ChatColor,
    val displayName: String,
    val chance: Double
) {
    RARE(ChatColor.DARK_AQUA, "Selten", 30.0),
    EPIC(ChatColor.DARK_PURPLE, "Episch", 25.0),
    LEGENDARY(ChatColor.GOLD, "Legendär", 20.0),
    MYTHIC(ChatColor.RED, "Mystisch", 15.0)
}

fun NBTItem.toKitType(): KitType? = KitType.values().find { it.displayName == getString("type") }
