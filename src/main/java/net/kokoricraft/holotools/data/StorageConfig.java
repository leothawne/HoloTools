package net.kokoricraft.holotools.data;

import org.bukkit.configuration.ConfigurationSection;

public class StorageConfig {
    private final String host;
    private final String port;
    private final String user;
    private final String password;
    private final String database;

    public StorageConfig(ConfigurationSection section) {
        host = section.getString("host", "localhost");
        port = section.getString("port", "3306");
        user = section.getString("user", "user");
        password = section.getString("password", "123456");
        database = section.getString("database", "database");
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }
}
