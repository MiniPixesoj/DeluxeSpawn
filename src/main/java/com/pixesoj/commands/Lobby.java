package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.cooldown.CooldownLobby;
import com.pixesoj.managers.cooldown.LobbyCooldownProvider;
import com.pixesoj.managers.delays.DelayLobby;
import com.pixesoj.model.internal.CooldownTimeProvider;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Lobby implements CommandExecutor {
    private DeluxeSpawn plugin;

    public Lobby(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!(sender instanceof Player)) {
            sendConsoleCommandDeniedMessage(sender, prefix);
            return true;
        }

        Player player = (Player) sender;
        String lobbyPermission = plugin.getMainPermissionsManager().getLobby();
        boolean lobbyPermissionDefault = plugin.getMainPermissionsManager().isLobbyDefault();
        boolean playerInCooldown = plugin.playerLobbyInCooldown(player);
        String bypassCooldownPermission = plugin.getMainPermissionsManager().getLobbyBypassCooldown();
        boolean bypassCooldownDefault = plugin.getMainPermissionsManager().isLobbyBypassCooldownDefault();
        boolean cooldownEnabled = plugin.getMainConfigManager().isLobbyCooldownEnabled();

        if (!permission(player, lobbyPermission, lobbyPermissionDefault)){
            sendMessage(player, prefix, "NoPermission");
            return true;
        }
        if (cooldownEnabled){
            if (cooldown(player, playerInCooldown, bypassCooldownPermission, bypassCooldownDefault)) {
                teleportPlayer(player);
                handleLobbyCooldown(player);
                return true;
            }
            sendMessage(player, prefix, "InCooldown");
            return true;
        } else {
            teleportPlayer(player);
        }
        return true;
    }


    private void sendConsoleCommandDeniedMessage(CommandSender sender, String prefix) {
        String message = prefix + plugin.getMainMessagesManager().getCommandDeniedConsole();
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    private boolean cooldown(Player player, boolean playerInCooldown, String bypassCooldownPermission, boolean bypassCooldownDefault) {
        return ((!playerInCooldown || bypassCooldownDefault || player.hasPermission(bypassCooldownPermission)));
    }

    private boolean permission(Player player, String lobbyPermission, boolean lobbyPermissionDefault){
        return (lobbyPermissionDefault || player.hasPermission(lobbyPermission));
    }

    private void sendMessage(Player player, String prefix, String reason) {
        String message;
        if (reason.equals("NoPermission")) {
            message = prefix + plugin.getMainMessagesManager().getPermissionDenied();
        } else if (reason.equals("InCooldown")) {
            int remainingTime = CooldownLobby.getRemainingTime(player);
            message = prefix + plugin.getMainMessagesManager().getLobbyInCooldown();
            message = message.replace("%time%", String.valueOf(remainingTime));
        } else {
            return;
        }
        player.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public void teleportPlayer (CommandSender sender){
        getTeleport(sender);
    }

    public void getTeleport(CommandSender sender) {
        Player player = (Player) sender;
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!plugin.getMainConfigManager().isLobbyEnabled()) {
            sendLobbyNotEnabledMessage(sender);
            return;
        }

        if (!locations.contains("Lobby.world")) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyDoesNotExist()));
        } else {
            getLobby(sender);
        }
    }

    private void sendLobbyNotEnabledMessage(CommandSender sender) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String message = prefix + plugin.getMainMessagesManager().getLobbyIsNotEnabled();
        sender.sendMessage(MessagesUtils.getColoredMessage(message));
    }

    public void getLobby(CommandSender sender) {
        Player player = (Player) sender;
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!validateLobbyWorld(locations, player, prefix)) {
            return;
        }

        Location lobbyLocation = getLobbyLocation(locations);
        int delay = plugin.getMainConfigManager().getLobbyTeleportDelay();
        DelayLobby delayManager = new DelayLobby(plugin, delay, player, lobbyLocation);

        String delayBypassPermission = plugin.getMainPermissionsManager().getLobbyBypassDelay();
        boolean delayBypassDefault = plugin.getMainPermissionsManager().isLobbyBypassDelayDefault();

        if (!plugin.getMainConfigManager().isLobbyTeleportDelayEnabled() || delayBypassDefault || player.hasPermission(delayBypassPermission)) {
            teleportPlayer(player, lobbyLocation, prefix, sender);
            return;
        }

        handleLobbyTeleportDelay(player, delayManager, prefix);
    }

    private boolean validateLobbyWorld(FileConfiguration locations, Player player, String prefix) {
        String world = locations.getString("Lobby.world");

        if (world == null) {
            String message = prefix + plugin.getMainMessagesManager().getLobbyDoesNotExist();
            player.sendMessage(MessagesUtils.getColoredMessage(message));
            return false;
        }

        return true;
    }

    private Location getLobbyLocation(FileConfiguration locations) {
        String world = locations.getString("Lobby.world");
        double x = locations.getDouble("Lobby.x");
        double y = locations.getDouble("Lobby.y");
        double z = locations.getDouble("Lobby.z");
        float yaw = (float) locations.getDouble("Lobby.yaw");
        float pitch = (float) locations.getDouble("Lobby.pitch");
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    private void teleportPlayer(Player player, Location location, String prefix, CommandSender sender) {
        sound(sender);
        executeCommands(sender);
        player.teleport(location);
        String message = prefix + plugin.getMainMessagesManager().getLobbyTeleported();
        player.sendMessage(MessagesUtils.getColoredMessage(message));
        plugin.addLobbyCooldown(player);
    }

    private void handleLobbyTeleportDelay(Player player, DelayLobby delayManager, String prefix) {
        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            delayManager.DelayLobby();
            if (!Objects.equals(plugin.getMainConfigManager().getLobbyTeleportDelayMessageType(), "Chat")) {
                int time = plugin.getMainConfigManager().getLobbyTeleportDelay();
                String message = plugin.getMainMessagesManager().getLobbyMessageDelayTeleport().replace("%time%", String.valueOf(time));
                player.sendMessage(MessagesUtils.getColoredMessage(message));
            }
        } else {
            String message = prefix + plugin.getMainMessagesManager().getLobbyInTeleport();
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void sound(CommandSender sender) {
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!plugin.getMainConfigManager().isLobbyTeleportSoundEnabled()) {
            return;
        }

        String soundName = plugin.getMainConfigManager().getLobbyTeleportSound();

        if (soundName == null) {
            handleNullSound(sender, prefix);
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            float volume = plugin.getMainConfigManager().getLobbyTeleportSoundVolume();
            float pitch = plugin.getMainConfigManager().getLobbyTeleportSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, soundName);
        }
    }

    private void handleNullSound(CommandSender sender, String prefix) {
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault() || sender.hasPermission(permission)) {
            String message = prefix + plugin.getMainMessagesManager().getLobbyNullSound();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
        } else {
            noPermission(sender);
        }
    }

    private void handleInvalidSound(Player player, String prefix, String soundName) {
        String message = prefix + plugin.getMainMessagesManager().getLobbyInvalidSound().replace("%sound%", soundName);
        player.sendMessage(message);
    }

    public void executeCommands(CommandSender sender) {
        if (plugin.getMainConfigManager().isLobbyCommandsEnabled()) {

            List<String> playerCommands = plugin.getMainConfigManager().getLobbyPlayerCommands();
            List<String> consoleCommands = plugin.getMainConfigManager().getLobbyConsoleCommands();

            Player player = (Player) sender;
            for (String command : playerCommands) {
                String replacedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(sender, replacedCommand);
            }

            CommandSender consoleSender = Bukkit.getConsoleSender();
            for (String command : consoleCommands) {
                String replacedCommand = command.replace("%player%", sender.getName());
                Bukkit.dispatchCommand(consoleSender, replacedCommand);
            }
        }
    }

    public void noPermission (CommandSender sender){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
    }

    public void handleLobbyCooldown(Player player){
        CooldownTimeProvider timeProvider = new LobbyCooldownProvider(plugin, player);
        int time = plugin.getMainConfigManager().getLobbyCooldownTime();
        CooldownLobby c = new CooldownLobby(plugin, timeProvider, time, player);
        c.cooldownLobby();
    }
}