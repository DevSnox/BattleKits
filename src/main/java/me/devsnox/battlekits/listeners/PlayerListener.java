package me.devsnox.battlekits.listeners;

import me.devsnox.battlekits.KitManager;
import me.devsnox.battlekits.kits.entites.KitPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private KitManager kitManager;

    public PlayerListener(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.kitManager.loadPlayer(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.kitManager.savePlayer(player.getUniqueId());
    }
}
