package me.devsnox.battlekits.functions

import de.tr7zw.itemnbtapi.NBTItem
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun Inventory.hasSpace(): Boolean = any { it == null || it.type == Material.AIR }

fun Inventory.filLBattle(items: List<ItemStack>) {
    for (i in 18 until 27 + 18)
        if (i - 18 < items.size) setItem(i, items[i - 18])
        else setItem(i, ItemStack(Material.AIR))
}


val backItem: ItemStack = NBTItem(ItemBuilder(Material.REDSTONE).setDisplayName("§cZurück").build()).apply {
    setBoolean("back", true)
}.item