package me.devsnox.battlekits.kits.kit

import org.bukkit.inventory.ItemStack

data class BattleKit(
    var id: Int,
    var name: String,
    var icon: ItemStack,
    var description: String,
    var kitType: KitType,
    var items: List<ItemStack>
)