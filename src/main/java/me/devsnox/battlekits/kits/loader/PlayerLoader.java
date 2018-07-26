package me.devsnox.battlekits.kits.loader;

import me.devsnox.battlekits.KitManager;
import me.devsnox.battlekits.connection.ConnectionConfig;
import me.devsnox.battlekits.connection.SkyConnection;
import me.devsnox.battlekits.connection.SyncMySQL;
import me.devsnox.battlekits.kits.kit.BattleKit;
import me.devsnox.battlekits.kits.entites.KitPlayer;
import me.devsnox.battlekits.kits.kit.PlayerKit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class PlayerLoader {

    private KitManager kitManager;
    private Plugin plugin;

    private String table;

    private SkyConnection skyConnection;
    private SyncMySQL connection;

    public PlayerLoader(KitManager kitManager) {
        this.kitManager = kitManager;
        this.plugin = kitManager.getPlugin();

        this.table = "battlekits_users";

        this.skyConnection = new SkyConnection(new ConnectionConfig(plugin.getDataFolder(), new File(plugin.getDataFolder() + File.separator + "mysql.yml")));
        this.skyConnection.connect();
        this.connection = this.skyConnection.sync();

        this.connection.update("CREATE TABLE IF NOT EXISTS " + table + " (UUID VARCHAR(36), ID INT, LAST_RECEIVE BIGINT)");
    }

    public boolean existPlayer(UUID uuid) {
        ResultSet resultSet = this.connection.query("SELECT * FROM " + table + " WHERE UUID='" + uuid + "'");
        try {
            if(resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void removePlayer(UUID uuid) {
        connection.update("DELETE * FROM " + table + "  WHERE UUID='" + uuid + "'");
    }

    public KitPlayer loadPlayer(UUID uuid) {
        ResultSet resultSet = this.connection.query("SELECT * FROM " + this.table + " WHERE UUID='" + uuid + "'");

        HashMap<Integer, BattleKit> rawKits = this.kitManager.getKits();

        HashMap<Integer, PlayerKit> kits = new HashMap<>();

        try {
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                if(rawKits.containsKey(id)) {
                    kits.put(resultSet.getInt("ID"), new PlayerKit(resultSet.getInt("ID"), rawKits.get(id), resultSet.getLong("LAST_RECEIVE")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new KitPlayer(uuid, kits);
    }

    public void savePlayer(KitPlayer kitPlayer) {
        for(PlayerKit playerKit : kitPlayer.getKits().values()) {
            connection.update("INSERT INTO " + this.table + "(UUID, ID, LAST_RECEIVE) VALUES('" + kitPlayer.getUuid() + "', '" + playerKit.getId() + "', '" + playerKit.getLastReceive() + "')");
        }
    }
}
