package me.devsnox.battlekits.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack itemStack;
    private ItemMeta itemMeta;
    private ArrayList<String> lore;

    public ItemBuilder() {
        this.itemStack = new ItemStack(Material.REDSTONE, 1);
        this.itemMeta = itemStack.getItemMeta();
        this.lore = new ArrayList<>();
    }

    public void setType(Material material) {
        this.itemStack.setType(material);
    }

    public void setDurability(int durability) {
        this.itemStack.setDurability((byte) durability);
    }

    public void setDisplayName(String displayName) {
        this.itemMeta.setDisplayName(displayName);
    }

    public void setLore(List<String> lore) {
        this.lore = (ArrayList<String>) lore;
    }

    public void setLoreLine(int row, String value) {
        this.lore.add(row, value);
    }

    public void addLoreLine(String value) {
        this.lore.add(value);
    }

    public void removeLoreLine(int row) {
        this.lore.remove(row);
    }

    public void setUnbreakable(boolean value) {
        this.itemMeta.spigot().setUnbreakable(value);
    }

    public void setGlow(boolean value) {
        if(value == true) {
            this.itemMeta.addEnchant(Enchantment.DAMAGE_ARTHROPODS, 1, true);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            this.itemMeta.removeEnchant(Enchantment.DAMAGE_ARTHROPODS);
        }
    }

    public ItemStack build() {
        this.itemMeta.setLore(this.lore);
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack.clone();
    }
}
