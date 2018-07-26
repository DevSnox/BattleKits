package me.devsnox.battlekits.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class InventoryUtils {

    public static boolean hasSpace(Inventory inventory) {
        for(int i = 0; i < inventory.getSize(); i++) {
            if(inventory.getItem(i) == null) return true;
            if(inventory.getItem(i).getType() == Material.AIR) return true;
        }

        return false;
    }
}
