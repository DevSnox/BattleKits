package me.devsnox.battlekits.kits.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class BattleKit {

    private int id;
    private String name;

    private ItemStack icon;
    private String description;

    private KitType kitType;
    private ArrayList<ItemStack> items;

    public BattleKit(Integer id, String name, Material icon, String description, KitType kitType, ArrayList<ItemStack> items) {
        this.id = id;
        this.name = name;
        this.icon = new ItemStack(icon, 1);
        this.description = description;
        this.kitType = kitType;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public KitType getKitType() {
        return kitType;
    }

    public ArrayList<ItemStack> getItems() {
        return items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }

    public void setItems(ArrayList<ItemStack> items) {
        this.items = items;
    }
}
