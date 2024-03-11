package com.pixesoj.commands;

import com.pixesoj.commands.tabcompleter.MainCommandTabCompleter;
import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.subcommands.Reload;
import com.pixesoj.subcommands.Update;
import com.pixesoj.utils.MessagesUtils;
import com.pixesoj.utils.common.SubCommand;
import com.pixesoj.utils.common.Updater;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class MainCommand implements CommandExecutor {

    private final DeluxeSpawn plugin;
    private final Map<String, SubCommand> subCommands;

    public MainCommand(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
        plugin.getCommand("deluxespawn").setExecutor(this);
        plugin.getCommand("deluxespawn").setTabCompleter(new MainCommandTabCompleter(deluxeSpawn));

        this.subCommands = new HashMap<>();
        subCommands.put("update", new Update(deluxeSpawn));
        subCommands.put("reload", new Reload(deluxeSpawn));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandName);

        if (subCommand != null) {
            return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
            return true;
        }

        /*if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("version")) {
                String permission = plugin.getMainPermissionsManager().getVersion();
                if (plugin.getMainPermissionsManager().isVersionDefault()) {
                    version(sender);
                } else {
                    if (player.hasPermission(permission)) {
                        version(sender);
                        return true;
                    }
                    noPermission(sender);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                String permission = plugin.getMainPermissionsManager().getHelp();
                if (plugin.getMainPermissionsManager().isHelpDefault()) {
                    help(sender);
                } else {
                    if (player.hasPermission(permission)) {
                        help(sender);
                        return true;
                    }
                    noPermission(sender);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                String permission = plugin.getMainPermissionsManager().getReload();
                if (plugin.getMainPermissionsManager().isReloadDefault()) {
                    reload(sender, args);
                } else {
                    if (player.hasPermission(permission)) {
                        reload(sender, args);
                        return true;
                    }
                    noPermission(sender);
                    return true;
                }
            } else if (args[0].equalsIgnoreCase("update")) {
                Updater updater = new Updater("3.3.0", "DeluxeSpawn", true, 111403, new File("plugins"), "/ds update");

                if (updater == null) {
                    sender.sendMessage("Updater not available. Check your configuration.");
                    return true;
                }

                if (updater.isAlreadyDownloaded()) {
                    sender.sendMessage(MessagesUtils.getColoredMessage("updater.already-downloaded"));
                    return true;
                }

                if (!updater.isUpdateAvailable()) {
                    sender.sendMessage(MessagesUtils.getColoredMessage("updater.no-update"));
                    return true;
                }

                if (updater.downloadUpdate()) {
                    sender.sendMessage(MessagesUtils.getColoredMessage("updater.success"));
                } else {
                    sender.sendMessage(MessagesUtils.getColoredMessage("updater.fail"));
                }
            } else if (args[0].equalsIgnoreCase("lastlocation")) {
                String permission = plugin.getMainPermissionsManager().getLastLocation();
                boolean permissionDefault = plugin.getMainPermissionsManager().isLastLocationDefault();
                if (permissionDefault || player.hasPermission(permission)){
                    getLastTeleportLocation(sender, args);
                } else {
                    noPermission(sender);
                }
            } else {
                sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
                sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
            }
        } else {
            sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));

        }
        return true;
    }*/

    /*public void help(CommandSender sender) {
        List<String> message = plugin.getMainMessagesManager().getCommandHelp();
        for (String m : message) {
            sender.sendMessage(MessagesUtils.getColoredMessage(m));
        }
    }

    public void version(CommandSender sender) {
        String latestVersion = this.plugin.getUpdateCheckerManager().getLatestVersion();
        String message = plugin.getMainMessagesManager().getCommandVersion().replace("%version%", plugin.getDescription().getVersion()).replace("%last_version%", latestVersion);
        sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + message));
    }

    public void noPermission(CommandSender sender) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
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
        }

        else if (notExistType.equals("SendMessage")) {
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String message = prefix + plugin.getMainMessagesManager().getLastLocationNotFound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        }

        else if (notExistType.equals("Spawn")) {
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
            return;
        }
    }

    public void sendMessage(CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String message = prefix + plugin.getMainMessagesManager().getLastLocationTeleport();
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
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
    }*/
    }
}


