package me.devsnox.battlekits.kits.kit;

import org.bukkit.ChatColor;

public enum KitType {

    RARE(ChatColor.DARK_AQUA, 3, "Selten", 30.0D),
    EPIC(ChatColor.DARK_PURPLE, 2, "Episch", 25.0D),
    LEGENDARY(ChatColor.GOLD, 1, "Legendär", 20.0D),
    MYTHIC(ChatColor.RED, 14,"Mystisch", 15.0D);

    private ChatColor color;
    private int durability;
    private String name;
    private double chance;

    KitType(ChatColor color, int durability, String name, double chance) {
        this.color = color;
        this.durability = durability;
        this.name = name;
        this.chance = chance;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getDurability() {
        return this.durability;
    }

    public String getName() {
        return name;
    }

    public double getChance() {
        return chance;
    }
}
