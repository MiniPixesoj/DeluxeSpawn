package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class DelLobby implements CommandExecutor {
    private DeluxeSpawn plugin;

    public DelLobby(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String permission = plugin.getMainPermissionsManager().getDelLobby();
        boolean permissionDefault = plugin.getMainPermissionsManager().isDelLobbyDefault();

        if (sender instanceof Player){
            Player player = (Player) sender;
            if (player.hasPermission(permission) || permissionDefault){
                getLobby(sender);
                return true;
            }
            else {
            senMessage(sender, "NotPermission");
            }
            return true;
        }

        getLobby(sender);
        return true;
    }

    public void getLobby (CommandSender sender){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        boolean exist = locations.contains("Lobby");

        if (!exist){
            senMessage(sender, "LobbyNotExist");
            return;
        }

        locations.set("Lobby", null);
        plugin.getLocationsManager().saveLocationsFile();

        senMessage(sender, "SuccessfullyRemoved");
    }

    public void senMessage(CommandSender sender, String reason){
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (reason.equals("LobbyNotExist")){
            String message = plugin.getMainMessagesManager().getDelLobbyNotExist();
            message = prefix + message;
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        }

        else if (reason.equals("SuccessfullyRemoved")) {
            String message = plugin.getMainMessagesManager().getDelLobbySuccessfullyRemoved();
            message = prefix + message;
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        }

        else if (reason.equals("NotPermission")) {
            String message = plugin.getMainMessagesManager().getPermissionDenied();
            message = prefix +  message;
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }
}

