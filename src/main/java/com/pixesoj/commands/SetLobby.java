package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetLobby implements CommandExecutor {
    private final DeluxeSpawn plugin;

    public SetLobby(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(MessagesUtils.getColoredMessage(getConsoleDeniedMessage()));
            return true;
        }

        Player player = (Player) sender;
        String permission = plugin.getMainPermissionsManager().getSetLobby();

        if (plugin.getMainPermissionsManager().isSetLobbyDefault() || player.hasPermission(permission)) {
            if (isLobbyEnabled(sender)) {
                setLobby(sender);
            }
        } else {
            noPermission(sender);
        }

        return true;
    }

    private String getConsoleDeniedMessage() {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        return prefix + plugin.getMainMessagesManager().getCommandDeniedConsole();
    }

    private boolean isLobbyEnabled(CommandSender sender) {
        if (!plugin.getMainLobbyConfigManager().isEnabled()) {
            String message = getPrefix() + plugin.getMainMessagesManager().getLobbyIsNotEnabled();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
            return false;
        }
        return true;
    }

    private String getPrefix() {
        return plugin.getMainMessagesManager().getPrefix();
    }

    public void setLobby (CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Location l = ((Player) sender).getLocation();
        locations.set("Lobby.world", Objects.requireNonNull(l.getWorld()).getName());
        locations.set("Lobby.x", l.getX());
        locations.set("Lobby.y", l.getY());
        locations.set("Lobby.z", l.getZ());
        locations.set("Lobby.yaw", l.getYaw());
        locations.set("Lobby.pitch", l.getPitch());

        plugin.getLocationsManager().saveLocationsFile();

        String world = ((Player) sender).getWorld().getName();
        String message = prefix + plugin.getMainMessagesManager().getCommandSetLobbySuccessfully().replace("%world%", world);
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public void noPermission (CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
    }
}
