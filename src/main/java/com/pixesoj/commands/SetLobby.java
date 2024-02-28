package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetLobby implements CommandExecutor {
    private DeluxeSpawn plugin;

    public SetLobby(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getCommandDeniedConsole()));
            return true;
        }
        if (!sender.hasPermission("deluxespawn.command.setlobby")) {
            sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
            return true;
        }
        if(!plugin.getMainConfigManager().isLobbyEnabled()){
            String message = prefix + plugin.getMainMessagesManager().getLobbyIsNotEnabled();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
            return true;
        } else {
            SetLobbyGlobal(sender);
            return true;
        }
    }

    public void SetLobbyGlobal (CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Location l = ((Player) sender).getLocation();
        locations.set("Lobby.world", l.getWorld().getName());
        locations.set("Lobby.x", l.getX());
        locations.set("Lobby.y", l.getY());
        locations.set("Lobby.z", l.getZ());
        locations.set("Lobby.yaw", l.getYaw());
        locations.set("Lobby.pitch", l.getPitch());

        plugin.getLocationsManager().saveLocationsFile();

        String world = ((Player) sender).getWorld().getName();
        String message = prefix + plugin.getMainMessagesManager().getCommandSetLobbySuccessfully().replace("%world%", world);
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
        return;
    }
}
