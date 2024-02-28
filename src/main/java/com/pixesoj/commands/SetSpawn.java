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

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        String player = sender.getName();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getCommandDeniedConsole()));
            return true;
        }
        if (!sender.hasPermission("deluxespawn.command.setspawn")) {
            sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
            return true;
        }
        if (!plugin.getMainConfigManager().isSpawnByWorld()){
            SetSpawnGlobal(sender);
            return true;
        } else {
            SetSpawnByWorld(sender);
            return true;
        }
    }

    public void SetSpawnGlobal (CommandSender sender){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Location l = ((Player) sender).getLocation();
        locations.set("Spawn.world", l.getWorld().getName());
        locations.set("Spawn.x", l.getX());
        locations.set("Spawn.y", l.getY());
        locations.set("Spawn.z", l.getZ());
        locations.set("Spawn.yaw", l.getYaw());
        locations.set("Spawn.pitch", l.getPitch());

        plugin.getLocationsManager().saveLocationsFile();

        String world = ((Player) sender).getWorld().getName();
        String message = plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandSetSpawnSuccessfully().replace("%world%", world);
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
        return;
    }

    public void SetSpawnByWorld (CommandSender sender){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Location l = ((Player) sender).getLocation();
        locations.set("SpawnByWorld." + l.getWorld().getName() + ".x", l.getX());
        locations.set("SpawnByWorld." + l.getWorld().getName() + ".y", l.getY());
        locations.set("SpawnByWorld." + l.getWorld().getName() + ".z", l.getZ());
        locations.set("SpawnByWorld." + l.getWorld().getName() + ".yaw", l.getYaw());
        locations.set("SpawnByWorld." + l.getWorld().getName() + ".pitch", l.getPitch());

        plugin.getLocationsManager().saveLocationsFile();

        String world = ((Player) sender).getWorld().getName();
        String message = plugin.getMainMessagesManager().getPrefix() + plugin.getMainMessagesManager().getCommandSetSpawnSuccessfully().replace("%world%", world);
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
        return;
    }
}