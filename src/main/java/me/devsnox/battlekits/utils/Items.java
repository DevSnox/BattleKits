package me.devsnox.battlekits.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Items {

    public static ItemStack space(int durability) {
        ItemBuilder builder = new ItemBuilder();

        builder.setDisplayName(" ");
        builder.setType(Material.STAINED_GLASS_PANE);
        builder.setDurability(durability);

        return builder.build();
    }
}
