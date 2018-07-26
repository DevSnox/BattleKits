package me.devsnox.battlekits.connection;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConnectionConfig {
    private File directory;
    private File config;
    private YamlConfiguration yamlConfiguration;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public ConnectionConfig(File directory, File file) {
        this.directory = directory;
        this.config = file;
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.config);
        if (!this.directory.exists()) {
            this.directory.mkdir();
            this.write();
        } else if (!this.config.exists()) {
            this.write();
        }

        this.host = this.yamlConfiguration.getString("mysql.host");
        this.port = this.yamlConfiguration.getInt("mysql.port");
        this.database = this.yamlConfiguration.getString("mysql.database");
        this.username = this.yamlConfiguration.getString("mysql.username");
        this.password = this.yamlConfiguration.getString("mysql.password");
    }

    private void write() {
        try {
            this.config.createNewFile();
            this.yamlConfiguration.set("mysql.host", "localhost");
            this.yamlConfiguration.set("mysql.port", Integer.valueOf(3306));
            this.yamlConfiguration.set("mysql.database", "database");
            this.yamlConfiguration.set("mysql.username", "root");
            this.yamlConfiguration.set("mysql.password", "");
            this.yamlConfiguration.save(this.config);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getDatabase() {
        return this.database;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
