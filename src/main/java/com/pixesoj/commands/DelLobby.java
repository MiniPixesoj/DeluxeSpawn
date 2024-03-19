package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DelLobby implements CommandExecutor {
    private final DeluxeSpawn plugin;

    private static final String MSG_LOBBY_NOT_EXIST = "LobbyNotExist";
    private static final String MSG_SUCCESSFULLY_REMOVED = "SuccessfullyRemoved";
    private static final String MSG_NOT_PERMISSION = "NotPermission";

    public DelLobby(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String permission = plugin.getMainPermissionsManager().getDelLobby();
        boolean permissionDefault = plugin.getMainPermissionsManager().isDelLobbyDefault();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission(permission) || permissionDefault) {
                getLobby(sender);
            } else {
                senMessage(sender, MSG_NOT_PERMISSION);
            }
        } else {
            getLobby(sender);
        }
        return true;
    }

    public void getLobby(CommandSender sender) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        boolean exist = locations.contains("Lobby");

        if (!exist) {
            senMessage(sender, MSG_LOBBY_NOT_EXIST);
            return;
        }

        locations.set("Lobby", null);
        plugin.getLocationsManager().saveLocationsFile();

        senMessage(sender, MSG_SUCCESSFULLY_REMOVED);
    }

    public void senMessage(CommandSender sender, String reason) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String message;

        switch (reason) {
            case MSG_LOBBY_NOT_EXIST:
                message = plugin.getMainMessagesManager().getDelLobbyNotExist();
                break;
            case MSG_SUCCESSFULLY_REMOVED:
                message = plugin.getMainMessagesManager().getDelLobbySuccessfullyRemoved();
                break;
            case MSG_NOT_PERMISSION:
                message = plugin.getMainMessagesManager().getPermissionDenied();
                break;
            default:
                return;
        }

        message = prefix + message;
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }
}
