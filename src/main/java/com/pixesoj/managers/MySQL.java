package com.pixesoj.managers;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MySQL {

    public static boolean playerExists (Connection connection, UUID uuid, DeluxeSpawn plugin){
        try {
            String tableName = plugin.getMainConfigManager().getDataTableName();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE (UUID=?)");
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return true;
            }
        }

        catch (SQLException e){

        }
        return false;
    }

    public static void playerCreate(Connection connection, UUID uuid, Player player, DeluxeSpawn plugin) {
        try {
            if (playerExists(connection, uuid, plugin)) {
                String tableName = plugin.getMainConfigManager().getDataTableName();
                PreparedStatement updateStatement = connection.prepareStatement("UPDATE " + tableName + " SET X=?, Y=?, Z=?, Yaw=?, Pitch=?, World=? WHERE UUID=?");

                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                String world = player.getWorld().getName();

                updateStatement.setDouble(1, x);
                updateStatement.setDouble(2, y);
                updateStatement.setDouble(3, z);
                updateStatement.setFloat(4, yaw);
                updateStatement.setFloat(5, pitch);
                updateStatement.setString(6, world);
                updateStatement.setString(7, uuid.toString());

                updateStatement.executeUpdate();
            } else {
                String tableName = plugin.getMainConfigManager().getDataTableName();
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO " + tableName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

                double x = player.getLocation().getX();
                double y = player.getLocation().getY();
                double z = player.getLocation().getZ();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                String world = player.getWorld().getName();

                insertStatement.setString(1, uuid.toString());
                insertStatement.setString(2, player.getName());
                insertStatement.setString(3, world);
                insertStatement.setDouble(4, x);
                insertStatement.setDouble(5, y);
                insertStatement.setDouble(6, z);
                insertStatement.setFloat(7, yaw);
                insertStatement.setFloat(8, pitch);

                insertStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getLastLocation(Connection connection, UUID uuid, Player player, DeluxeSpawn plugin) {
        try {
            if (playerExists(connection, uuid, plugin)) {
                String tableName = plugin.getMainConfigManager().getDataTableName();
                PreparedStatement getStatement = connection.prepareStatement("SELECT * FROM " + tableName + " WHERE (UUID = ?)");
                getStatement.setString(1, uuid.toString());
                ResultSet result = getStatement.executeQuery();

                if (result.next()) {
                    String world = result.getString("world");
                    double x = result.getDouble("x");
                    double y = result.getDouble("y");
                    double z = result.getDouble("z");
                    float yaw = result.getFloat("yaw");
                    float pitch = result.getFloat("pitch");

                    Location location = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
                    player.teleport(location);
                    soundLastLocation(player, plugin);
                    executeCommandsLastLocation(player, plugin);
                }
                return;
            }
            handleLocationNotExist(player, plugin);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleLocationNotExist(Player player, DeluxeSpawn plugin) {
        String notExistType = plugin.getMainLobbyConfigManager().getLastLocationCommandLocationNotExist();
        if (notExistType != null) {
            switch (notExistType.toLowerCase()) {
                case "sendmessage":
                    String prefix = plugin.getMainMessagesManager().getPrefix();
                    String message = prefix + plugin.getMainMessagesManager().getLastLocationNotFound();
                    player.sendMessage(MessagesUtils.getColoredMessage(message));
                    break;

                case "spawn":
                    boolean spawnByWorld = plugin.getMainSpawnConfigManager().isByWorld();
                    FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
                    if (!spawnByWorld) {
                        double x = locations.getDouble("Spawn.x");
                        double y = locations.getDouble("Spawn.y");
                        double z = locations.getDouble("Spawn.z");
                        float yaw = (float) locations.getDouble("Spawn.yaw");
                        float pitch = (float) locations.getDouble("Spawn.pitch");
                        String worldName = locations.getString("Spawn.world");
                        Location spawnLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

                        player.teleport(spawnLocation);
                        return;
                    }

                    String world = plugin.getMainLobbyConfigManager().getLastLocationCommandSpawn();
                    String spawnKey = "Spawn." + world;
                    double x = locations.getDouble(spawnKey + ".x");
                    double y = locations.getDouble(spawnKey + ".y");
                    double z = locations.getDouble(spawnKey + ".z");
                    float yaw = (float) locations.getDouble(spawnKey + ".yaw");
                    float pitch = (float) locations.getDouble(spawnKey + ".pitch");
                    Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

                    player.teleport(spawnLocation);
                    soundLastLocation(player, plugin);
                    executeCommandsLastLocation(player, plugin);
                    break;

                case "donothing":
                    break;

                default:
                    Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&cTipo de 'notExistType' no válido"));
                    break;
            }
        }
    }

    public static void soundLastLocation(CommandSender sender, DeluxeSpawn plugin) {
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean enabled = plugin.getMainLobbyConfigManager().isLastLocationSoundEnabled();

        if (!enabled) {
            return;
        }

        String soundName = plugin.getMainLobbyConfigManager().getLastLocationSound();

        if (soundName == null) {
            handleNullSound(sender, prefix, plugin);
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            float volume = plugin.getMainLobbyConfigManager().getLastLocationSoundVolume();
            float pitch = plugin.getMainLobbyConfigManager().getLastLocationSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, soundName, plugin);
        }
    }

    private static void handleNullSound(CommandSender sender, String prefix, DeluxeSpawn plugin) {
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault() || sender.hasPermission(permission)) {
            String message = prefix + plugin.getMainMessagesManager().getLastLocationNullSound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    private static void handleInvalidSound(Player player, String prefix, String soundName, DeluxeSpawn plugin) {
        String message = prefix + plugin.getMainMessagesManager().getLastLocationInvalidSound().replace("%sound%", soundName);
        player.sendMessage(message);
    }

    private static void executeCommandsLastLocation(CommandSender sender, DeluxeSpawn plugin) {
        if (plugin.getMainLobbyConfigManager().isLastLocationCommandsEnabled()) {

            List<String> playerCommands = plugin.getMainLobbyConfigManager().getLastLocationCommandsPlayer();
            List<String> consoleCommands = plugin.getMainLobbyConfigManager().getLastLocationCommandsConsole();

            for (String command : playerCommands) {
                String replacedCommand = command.replace("%player%", sender.getName());
                Bukkit.dispatchCommand(sender, replacedCommand);
            }

            CommandSender consoleSender = Bukkit.getConsoleSender();
            for (String command : consoleCommands) {
                String replacedCommand = command.replace("%player%", sender.getName());
                Bukkit.dispatchCommand(consoleSender, replacedCommand);
            }
        }
    }
}
