package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {
    private DeluxeSpawn plugin;

    public SetSpawn(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtils.getColoredMessage(getConsoleDeniedMessage()));
            return true;
        }

        Player player = (Player) sender;
        String permission = plugin.getMainPermissionsManager().getSetSpawn();

        if (plugin.getMainPermissionsManager().isSetSpawnDefault() || player.hasPermission(permission)) {
            setSpawn(sender);
        } else {
            noPermission(sender);
        }

        return true;
    }

    private String getConsoleDeniedMessage() {
        return plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandDeniedConsole();
    }

    public void setSpawn(CommandSender sender) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Location location = ((Player) sender).getLocation();

        if (!plugin.getMainSpawnConfigManager().isByWorld()) {
            setLocation(locations, "Spawn", location);
        } else {
            String worldName = location.getWorld().getName();
            setLocation(locations, "SpawnByWorld." + worldName, location);
        }

        plugin.getLocationsManager().saveLocationsFile();
        String world = location.getWorld().getName();
        String message = plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandSetSpawnSuccessfully().replace("%world%", world);
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    private void setLocation(FileConfiguration locations, String path, Location location) {
        locations.set(path + ".world", location.getWorld().getName());
        locations.set(path + ".x", location.getX());
        locations.set(path + ".y", location.getY());
        locations.set(path + ".z", location.getZ());
        locations.set(path + ".yaw", location.getYaw());
        locations.set(path + ".pitch", location.getPitch());
    }

    public void noPermission(CommandSender sender) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
    }
}