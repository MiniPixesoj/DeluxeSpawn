package com.pixesoj.commands;

import com.pixesoj.commands.tabcompleter.SpawnTabCompleter;
import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DelSpawn implements CommandExecutor {
    private DeluxeSpawn plugin;

    public DelSpawn(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
        plugin.getCommand("delspawn").setExecutor(this);
        plugin.getCommand("delspawn").setTabCompleter(new SpawnTabCompleter(deluxeSpawn));
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        removeSpawn(sender, args);
        return true;
    }

    public void removeSpawn(CommandSender sender, String[] args) {
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String permission = plugin.getMainPermissionsManager().getDelSpawn();
            boolean permissionDefault = plugin.getMainPermissionsManager().isDelSpawnDefault();

            if (player.hasPermission(permission) || permissionDefault) {
                commandArgs(sender, prefix, args);
                return;
            }

            else {
                sendMessage(sender, prefix, "NotPermission", args);
            }
            return;
        }
        commandArgs(sender, prefix, args);
    }

    public void commandArgs (CommandSender sender, String prefix, String[] args){
        if (args == null || args.length == 0) {
            getRemoveSpawn(sender, args);
        } else if (args[0].equalsIgnoreCase("global")) {
            getSpawnGlobal(sender, prefix, args);
        } else {
            String world = args[0];
            getSpawnWorld(sender, prefix, world, args);
        }
    }

    public void getRemoveSpawn(CommandSender sender, String[] args) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean spawnByWorld = plugin.getMainConfigManager().isSpawnByWorld();

        if (spawnByWorld) {
            sendMessage(sender, prefix, "NotSpecified", args);
        } else {
            getSpawnGlobal(sender, prefix, args);
        }
    }

    public void getSpawnGlobal (CommandSender sender, String prefix, String[] args){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (!locations.contains("Spawn")){
            sendMessage(sender, prefix, "SpawnNotExist", args);
            return;
        }

        locations.set("Spawn", null);
        plugin.getLocationsManager().saveLocationsFile();

        sendMessage(sender, prefix, "SuccessfullyRemoved", args);
    }

    public void getSpawnWorld (CommandSender sender, String prefix, String world, String[] args){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (!locations.contains("SpawnByWorld." + world)){
            sendMessage(sender, prefix, "SpawnNotExist", args);
            return;
        }

        locations.set("SpawnByWorld." + world, null);
        plugin.getLocationsManager().saveLocationsFile();

        sendMessage(sender, prefix, "SuccessfullyRemoved", args);
    }

    public void sendMessage(CommandSender sender, String prefix, String reason, String[] args) {
        switch (reason) {
            case "SpawnNotExist":
                if (args != null && args.length >= 1 && !args[0].contains("global")) {
                    String m = plugin.getMainMessagesManager().getDelSpawnNotExistWorld();
                    String message = prefix + m;
                    String world = args[0];
                    message = message.replace("%world%", world);
                    sender.sendMessage(MessagesUtils.getColoredMessage(message));
                } else if (args != null && args.length > 0 && args[0].equalsIgnoreCase("global")) {
                    String m = plugin.getMainMessagesManager().getDelSpawnNotExist();
                    String message = prefix + m;
                    sender.sendMessage(MessagesUtils.getColoredMessage(message));
                } else {
                    String m = plugin.getMainMessagesManager().getDelSpawnNotExist();
                    String message = prefix + m;
                    sender.sendMessage(MessagesUtils.getColoredMessage(message));
                }
                break;
            case "SuccessfullyRemoved":
                if (args != null && args.length >= 1 && !args[0].contains("global")) {
                    String world = args[0];
                    String m = plugin.getMainMessagesManager().getDelSpawnSuccessfullyRemovedWorld();
                    String message = prefix + m;
                    message = message.replace("%world%", world);
                    sender.sendMessage(MessagesUtils.getColoredMessage(message));
                } else if (args != null && args.length > 0 && args[0].equalsIgnoreCase("global")) {
                    String m = plugin.getMainMessagesManager().getDelSpawnSuccessfullyRemoved();
                    String message = prefix + m;
                    sender.sendMessage(MessagesUtils.getColoredMessage(message));
                } else {
                    String m = plugin.getMainMessagesManager().getDelSpawnSuccessfullyRemoved();
                    String message = prefix + m;
                    sender.sendMessage(MessagesUtils.getColoredMessage(message));
                }
                break;
            case "NotSpecified": {
                String m = plugin.getMainMessagesManager().getDelSpawnNotSpecified();
                String message = prefix + m;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NotPermission": {
                String m = plugin.getMainMessagesManager().getPermissionDenied();
                String message = prefix + m;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
        }
    }
}