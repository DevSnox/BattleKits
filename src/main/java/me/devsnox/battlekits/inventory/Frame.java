package me.devsnox.battlekits.inventory;

import me.devsnox.battlekits.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public abstract class Frame {

    private FrameOptions frameOptions;

    public Inventory inventory;

    public Frame(int size, String displayName, FrameOptions frameOptions) {
        this.frameOptions = frameOptions;
        this.inventory = Bukkit.createInventory(null, size, displayName);
    }

    public void render(Player player) {
        player.openInventory(this.inventory);
    }

    public void onClick(InventoryClickEvent event) {
        if(this.frameOptions.isNoItemMove()) {
            event.setCancelled(true);
        }
    }

    public void onClose(InventoryCloseEvent event) {}

    public void fill(ItemStack itemStack) {
        for(int i = 0; i < this.inventory.getSize(); i++) {
            this.inventory.setItem(i, itemStack);
        }
    }

    public void fill(int durability) {
        ItemBuilder itemBuilder = new ItemBuilder();

        itemBuilder.setType(Material.STAINED_GLASS_PANE);
        itemBuilder.setDisplayName(" ");
        itemBuilder.setDurability(7);

        for(int i = 0; i < this.inventory.getSize(); i++) {
            this.inventory.setItem(i, itemBuilder.build());
        }
    }
}
