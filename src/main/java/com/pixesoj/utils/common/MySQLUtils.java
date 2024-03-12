package com.pixesoj.utils.common;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQLUtils {

    private DeluxeSpawn plugin;
    private Connection connection;

    public void coloredMessage(String text) {
        Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage(text));
    }

    public MySQLUtils(DeluxeSpawn plugin) {
        this.plugin = plugin;
        String dataType = plugin.getMainConfigManager().getDataType();
        if (!dataType.equals("MySQL")){
            return;
        }
        connectToDatabase();
        createTablesIfNotExists();
    }

    private void connectToDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                coloredMessage("&8[&eDeluxeSpawn&8] &cError connecting to MySQL: Connection is already open");
                return;
            }

            String address = plugin.getMainConfigManager().getDataAddress();
            int port = plugin.getMainConfigManager().getDataPort();
            String database = plugin.getMainConfigManager().getDatabase();
            String user = plugin.getMainConfigManager().getDataUserName();
            String password = plugin.getMainConfigManager().getDataPassword();

            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + address + ":" + port + "/" + database, user, password);

            coloredMessage("&8[&eDeluxeSpawn&8] &aPlugin connected to MySQL");
        } catch (SQLException | ClassNotFoundException e) {
            handleException(e, "[DeluxeSpawn] Error in MySQL connection");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                coloredMessage("&8[&eDeluxeSpawn&8] &aConnection closed with MySQL");
            } else {
                coloredMessage("&8[&eDeluxeSpawn&8] &cError closing connection with MySQL: Connection is already closed");
            }
        } catch (SQLException e) {
            handleException(e, "[DeluxeSpawn] Error closing connection with MySQL");
        }
    }

    private void createTablesIfNotExists() {
        try (Statement statement = connection.createStatement()) {
            String tableName = plugin.getMainConfigManager().getDataTableName();
            ResultSet resultSet = connection.getMetaData().getTables(null, null, tableName, null);
            if (!resultSet.next()) {
                createTables();
            }
        } catch (SQLException e) {
            handleException(e, "[DeluxeSpawn] Error checking or creating tables in MySQL");
        }
    }

    private void createTables() {
        try (Statement statement = connection.createStatement()) {
            String tableName = plugin.getMainConfigManager().getDataTableName();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "UUID VARCHAR(36) PRIMARY KEY," +
                    "PlayerName VARCHAR(255)," +
                    "World VARCHAR(255)," +
                    "X DOUBLE," +
                    "Y DOUBLE," +
                    "Z DOUBLE," +
                    "Yaw FLOAT," +
                    "Pitch FLOAT)");

            coloredMessage("&8[&eDeluxeSpawn&8] &aTables created successfully");
        } catch (SQLException e) {
            handleException(e, "[DeluxeSpawn] Error creating tables in MySQL");
        }
    }

    private void handleException(Exception e, String message) {
        e.printStackTrace();
        coloredMessage("&8[&eDeluxeSpawn&8] &c" + message + ": " + e.getMessage());
    }
}