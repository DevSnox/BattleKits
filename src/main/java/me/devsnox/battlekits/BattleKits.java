package me.devsnox.battlekits;

import fr.minuskube.inv.InventoryManager;
import me.devsnox.battlekits.commands.*;
import me.devsnox.battlekits.listeners.FrameListener;
import me.devsnox.battlekits.listeners.KitListener;
import me.devsnox.battlekits.listeners.PlayerListener;
import me.devsnox.battlekits.listeners.ShardListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class BattleKits extends JavaPlugin {

    private InventoryManager inventoryManager;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        this.inventoryManager = new InventoryManager(this);
        this.inventoryManager.init();

        this.kitManager = new KitManager(this);
        this.kitManager.enable();

        Bukkit.getPluginManager().registerEvents(new KitListener(this.kitManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this.kitManager), this);
        Bukkit.getPluginManager().registerEvents(new ShardListener(this.kitManager), this);

        final FrameListener frameListener = new FrameListener();

        this.kitManager.setFrameListener(frameListener);

        Bukkit.getPluginManager().registerEvents(frameListener, this);

        this.getCommand("kiteditor").setExecutor(new KitEditorCommand(this.kitManager));
        this.getCommand("kits").setExecutor(new KitCommand(this.kitManager));
        this.getCommand("getshard").setExecutor(new ShardCommand(this.kitManager));
        this.getCommand("getbooster").setExecutor(new BoosterCommand(this.kitManager));
        this.getCommand("preview").setExecutor(new PreviewTest());
    }

    @Override
    public void onDisable() {
        this.kitManager.disable();
    }
}
