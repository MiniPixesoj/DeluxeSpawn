package com.pixesoj.subcommands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.MySQL;
import com.pixesoj.utils.common.MySQLUtils;
import com.pixesoj.utils.spigot.MessagesUtils;
import com.pixesoj.utils.spigot.PlayerUtils;
import com.pixesoj.utils.common.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public class Lastlocation implements SubCommand {

    private final DeluxeSpawn plugin;
    Connection connection;

    public Lastlocation (DeluxeSpawn plugin){
        this.plugin = plugin;
        this.connection = new MySQLUtils(plugin).getConnection();
    }

    public void colored(CommandSender sender, String prefix, String text){
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + text));
    }

    public String prefix(){
        return plugin.getMainMessagesManager().getPrefix();
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            String message = plugin.getMainMessagesManager().getCommandDeniedConsole();
            colored(sender, prefix(), message);
            return true;
        }

        String p = plugin.getMainPermissionsManager().getLastLocation();
        boolean d = plugin.getMainPermissionsManager().isLastLocationDefault();
        if (!PlayerUtils.hasPermissionMessage(sender, p, d)){
            String m = plugin.getMainMessagesManager().getPermissionDenied();
            colored(sender, prefix(), m);
            return true;
        }
        getLastTeleportLocation(sender, args);
        return true;
    }

    public void getLastTeleportLocation(CommandSender sender, String[] args){
        Player player = (Player) sender;
        boolean oneTime = plugin.getMainLobbyConfigManager().isLastLocationCommandOneTime();
        if (oneTime){
            boolean defaultPermission = plugin.getMainPermissionsManager().isLastLocationBypassCommandDefault();
            String permission = plugin.getMainPermissionsManager().getLastLocationBypassCommand();
            boolean inLastLocationOneTime = plugin.playerLastLocationOneTime(player);

            if (!inLastLocationOneTime || defaultPermission || player.hasPermission(permission)){
                teleportLastLocation(sender, args);
                return;
            }
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String message = prefix + plugin.getMainMessagesManager().getLastLocationOneTime();
            player.sendMessage(MessagesUtils.getColoredMessage(message));
            return;
        }
        teleportLastLocation(sender, args);
    }

    public void teleportLastLocation(CommandSender sender, String[] args) {
        UUID uuid;

        Player player = (Player) sender;
        uuid = player.getUniqueId();
        getLastLocation(uuid, player, sender);
    }

    public void getLastLocation(UUID uuid, Player player, CommandSender sender) {
        FileConfiguration playerConfig = plugin.getPlayerDataManager().getPlayerConfig(uuid);
        String notExistType = plugin.getMainLobbyConfigManager().getLastLocationCommandLocationNotExist();

        String dataType = plugin.getMainConfigManager().getDataType();
        if (dataType.equalsIgnoreCase("MySQL")){
            MySQL.getLastLocation(connection, uuid, player, plugin);
        } else if (dataType.equalsIgnoreCase("localhost")) {
            if (playerConfig.contains("LastLocation.world")) {
                String worldName = playerConfig.getString("LastLocation.world");
                double x = playerConfig.getDouble("LastLocation.x");
                double y = playerConfig.getDouble("LastLocation.y");
                double z = playerConfig.getDouble("LastLocation.z");
                float yaw = (float) playerConfig.getDouble("LastLocation.yaw");
                float pitch = (float) playerConfig.getDouble("LastLocation.pitch");

                Location lastLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

                player.teleport(lastLocation);
                soundLastLocation(sender);
                executeCommandsLastLocation(sender);
                sendMessage(sender);

            } else {
                handleNotExistType(notExistType, player, sender);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&cEl tipo de data no es valido, usa &aMySQL &co &alocalhost"));
        }
    }

    private void handleNotExistType(String notExistType, Player player, CommandSender sender) {
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (notExistType.equals("SendMessage")) {
            String message = prefix + plugin.getMainMessagesManager().getLastLocationNotFound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        } else if (notExistType.equals("Spawn")) {
            boolean spawnByWorld = plugin.getMainSpawnConfigManager().isByWorld();
            FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
            if (!spawnByWorld){
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

        } else if (notExistType.equals("DoNothing")) {

        } else {
            Bukkit.getConsoleSender().sendMessage(MessagesUtils.getColoredMessage("&cTipo de 'notExistType' no válido"));
        }
    }

    public void soundLastLocation(CommandSender sender) {
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean enabled = plugin.getMainLobbyConfigManager().isLastLocationSoundEnabled();

        if (!enabled) {
            return;
        }

        String soundName = plugin.getMainLobbyConfigManager().getLastLocationSound();

        if (soundName == null) {
            handleNullSound(sender, prefix);
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            float volume = plugin.getMainLobbyConfigManager().getLastLocationSoundVolume();
            float pitch = plugin.getMainLobbyConfigManager().getLastLocationSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, soundName);
        }
    }

    private void handleNullSound(CommandSender sender, String prefix) {
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault() || sender.hasPermission(permission)) {
            String message = prefix + plugin.getMainMessagesManager().getLastLocationNullSound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    private void handleInvalidSound(Player player, String prefix, String soundName) {
        String message = prefix + plugin.getMainMessagesManager().getLastLocationInvalidSound().replace("%sound%", soundName);
        player.sendMessage(message);
    }

    public void sendMessage(CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String message = prefix + plugin.getMainMessagesManager().getLastLocationTeleport();
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public void executeCommandsLastLocation(CommandSender sender) {
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

