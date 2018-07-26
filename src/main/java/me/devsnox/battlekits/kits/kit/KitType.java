package me.devsnox.battlekits.kits.kit;

import org.bukkit.ChatColor;

public enum KitType {

    RARE(ChatColor.DARK_AQUA, "Selten", 30.0D),
    EPIC(ChatColor.DARK_PURPLE, "Episch", 25.0D),
    LEGENDARY(ChatColor.GOLD, "Legendär", 20.0D),
    MYTHIC(ChatColor.RED, "Mystisch", 15.0D);

    private ChatColor color;
    private String name;
    private double chance;

    KitType(ChatColor color, String name, double chance) {
        this.color = color;
        this.name = name;
        this.chance = chance;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public double getChance() {
        return chance;
    }
}
