package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainCommand implements CommandExecutor {

    private DeluxeSpawn plugin;

    public MainCommand(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
        plugin.getCommand("deluxespawn").setExecutor(this);
        plugin.getCommand("deluxespawn").setTabCompleter(new MainCommandTabCompleter());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("version")) {
                    String latestVersion = this.plugin.getUpdateCheckerManager().getLatestVersion();
                    String message = plugin.getMainMessagesManager().getCommandVersion().replace("%version%", plugin.getDescription().getVersion()).replace("%last_version%", latestVersion);
                    sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + message));
                } else if (args[0].equalsIgnoreCase("help")) {
                    help(sender);
                } else if (args[0].equalsIgnoreCase("reload")) {
                    reload(sender, args);

                } else {
                    sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
                    sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));
                }
            } else {
                sender.sendMessage(MessagesUtils.getColoredMessage("&eDeluxeSpawn &7by &fPixesoj"));
                sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getCommandUsage()));

            }
            return true;
        }

        Player player = (Player) sender;

        if (args.length >= 1) {
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
    }

    public void help(CommandSender sender) {
        List<String> message = plugin.getMainMessagesManager().getCommandHelp();
        for (String m : message) {
            sender.sendMessage(MessagesUtils.getColoredMessage(m));
        }
    }

    public void reload(CommandSender sender, String[] args) {
        if (args.length == 1) {
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandInvalidArgument() + " &a/deluxespawn reload <config|messages|all>"));
            return;
        }
        if (args[1].equalsIgnoreCase("config")) {
            plugin.getMainConfigManager().reloadConfig();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadConfig()));
        } else if (args[1].equalsIgnoreCase("messages")) {
            plugin.getMainMessagesManager().reloadMessages();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadMessages()));

        } else if (args[1].equalsIgnoreCase("permissions")) {
            plugin.getMainPermissionsManager().reloadPermissions();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadMessages()));

        } else if (args[1].equalsIgnoreCase("all")) {
            plugin.getMainMessagesManager().reloadMessages();
            plugin.getMainConfigManager().reloadConfig();
            plugin.getLocationsManager().saveLocationsFile();
            plugin.getMainPermissionsManager().reloadPermissions();
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandReloadAll().replace("%version%", plugin.getDescription().getVersion())));

        } else {
            sender.sendMessage(MessagesUtils.getColoredMessage(plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandInvalidArgument() + " &a/deluxespawn reload &8<&aconfig&8|&amessages&8|&apermissions&8|&aall&8>"));

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
        if (playerConfig.contains("LastLocation")) {
            String worldName = playerConfig.getString("LastLocation.world");
            double x = playerConfig.getDouble("LastLocation.x");
            double y = playerConfig.getDouble("LastLocation.y");
            double z = playerConfig.getDouble("LastLocation.z");
            float yaw = (float) playerConfig.getDouble("LastLocation.yaw");
            float pitch = (float) playerConfig.getDouble("LastLocation.pitch");

            Location lastLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);

            player.teleport(lastLocation);
            sendMessage(sender);
        } else {
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String message = prefix + plugin.getMainMessagesManager().getLastLocationNotFound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));

        }
    }

    public void sendMessage(CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String message = prefix + plugin.getMainMessagesManager().getLastLocationTeleport();
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public void getLastTeleportLocation(CommandSender sender, String[] args){
        Player player = (Player) sender;
        boolean oneTime = plugin.getMainConfigManager().isLobbyLastLocationCommandOneTime();
        if (oneTime){
            UUID uuid = player.getUniqueId();
            FileConfiguration playerConfig = plugin.getPlayerDataManager().getPlayerConfig(uuid);
            String OneTime = playerConfig.getString("LastLocation.OneTime");
            boolean defaultPermission = plugin.getMainPermissionsManager().isLastLocationBypassCommandDefault();
            String permission = plugin.getMainPermissionsManager().getLastLocationBypassCommand();
            if (!OneTime.equals("yes") || defaultPermission || player.hasPermission(permission)){
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
}


class MainCommandTabCompleter implements org.bukkit.command.TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("reload");
            completions.add("version");
            completions.add("help");
            completions.add("lastlocation");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            completions.add("all");
            completions.add("config");
            completions.add("messages");
            completions.add("permissions");
        }
        return completions;
    }
}