package net.kokoricraft.holotools.data.types;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kokoricraft.holotools.HoloTools;
import net.kokoricraft.holotools.data.Storage;
import net.kokoricraft.holotools.data.StorageConfig;
import net.kokoricraft.holotools.enums.StorageMode;
import net.kokoricraft.holotools.objects.players.HoloPlayer;

import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class MySqlStorage implements Storage {
    protected final HoloTools plugin = HoloTools.getInstance();
    private final StorageConfig config = plugin.getConfigManager().STORAGE_CONFIG;
    private Connection connection;

    public MySqlStorage() {
        try {
            connect();
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) return;

        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();
        connection = DriverManager.getConnection(url, config.getUser(), config.getPassword());
    }

    private void createTables() throws SQLException {
        String createPlayerTable = "CREATE TABLE IF NOT EXISTS holotools_players (" +
                "uuid VARCHAR(36) PRIMARY KEY, " +
                "data TEXT" +
                ")";

        String createHoloTable = "CREATE TABLE IF NOT EXISTS holotools_holos (" +
                "id INT PRIMARY KEY, " +
                "data TEXT" +
                ")";

        String createIdTrackerTable = "CREATE TABLE IF NOT EXISTS holotools_id_tracker (" +
                "key_name VARCHAR(50) PRIMARY KEY, " +
                "next_id INT NOT NULL" +
                ")";

        String insertInitialHoloId = "INSERT IGNORE INTO holotools_id_tracker (key_name, next_id) VALUES ('holos', 1)";

        Statement statement = connection.createStatement();
        statement.executeUpdate(createPlayerTable);
        statement.executeUpdate(createHoloTable);
        statement.executeUpdate(createIdTrackerTable);
        statement.executeUpdate(insertInitialHoloId);
    }

    @Override
    public HoloPlayer loadPlayer(UUID uuid) {
        HoloPlayer holoPlayer = new HoloPlayer(uuid);
        if (plugin.getConfigManager().STORAGE_MODE == StorageMode.PLAYER) {
            try {
                connect();
                String query = "SELECT data FROM holotools_players WHERE uuid = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String data = resultSet.getString("data");
                    JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
                    for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                        holoPlayer.setData(entry.getKey(), entry.getValue().getAsJsonObject());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return holoPlayer;
    }

    @Override
    public void savePlayer(HoloPlayer holoPlayer) {
        UUID uuid = holoPlayer.getUUID();
        if (plugin.getConfigManager().STORAGE_MODE == StorageMode.PLAYER) {
            try {
                connect();
                Map<String, JsonObject> data = holoPlayer.getData();
                JsonObject jsonObject = new JsonObject();

                for (Map.Entry<String, JsonObject> entry : data.entrySet()) {
                    jsonObject.add(entry.getKey(), entry.getValue());
                }

                String query = "INSERT INTO holotools_players (uuid, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, uuid.toString());
                statement.setString(2, jsonObject.toString());
                statement.setString(3, jsonObject.toString());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getNextID() {
        int id = 0;
        try {
            connect();

            String selectQuery = "SELECT next_id FROM holotools_id_tracker WHERE key_name = 'holos'";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            ResultSet resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt("next_id");
            }

            String updateQuery = "UPDATE holotools_id_tracker SET next_id = next_id + 1 WHERE key_name = 'holos'";
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public void saveHolo(int id, JsonObject jsonObject) {
        try {
            connect();
            String query = "INSERT INTO holotools_holos (id, data) VALUES (?, ?) ON DUPLICATE KEY UPDATE data = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.setString(2, jsonObject.toString());
            statement.setString(3, jsonObject.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public JsonObject getHolo(int id) {
        JsonObject jsonObject = new JsonObject();
        try {
            connect();
            String query = "SELECT data FROM holotools_holos WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String data = resultSet.getString("data");
                jsonObject = JsonParser.parseString(data).getAsJsonObject();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
