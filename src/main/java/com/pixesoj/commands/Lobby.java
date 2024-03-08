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
            if (args.length > 0) {
                commandConsole(sender, args);
                return true;
            }

            sendMessage(sender, prefix, args, "ConsoleUsage");
            return true;
        }

        commandPlayer(sender, args);
        return true;
    }

    public void commandConsole (CommandSender sender, String[] args){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        boolean enabled = plugin.getMainConfigManager().isLobbyEnabled();

        if (!enabled) {
            sendMessage(sender, prefix, args, "LobbyNoEnabled");
            return;
        }

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sendMessage(sender, prefix, args, "PlayerOffline");
            return;
        }

        getTeleportConsole(sender, targetPlayer, args);
    }

    public void getTeleportConsole(CommandSender sender, Player player, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (!locations.contains("Lobby.world")) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        getLobbyConsole(sender, targetPlayer, args);
    }

    public void getLobbyConsole(CommandSender sender, Player player, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        Location location = getLobbyLocation(locations);

        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        assert targetPlayer != null;

        sendMessage(sender, prefix, args, "ConsolePlayerTeleported");
        sendMessage(targetPlayer, prefix, args, "ConsolePlayerTeleport");
        targetPlayer.teleport(location);
    }

    public void commandPlayer(CommandSender sender, String[] args){
        Player player = (Player) sender;
        if (args.length > 0) {
            String permission = plugin.getMainPermissionsManager().getLobbyOther();
            boolean permissionDefault = plugin.getMainPermissionsManager().isLobbyOtherDefault();

            if (permissionDefault || player.hasPermission(permission)){
                getTeleportOtherPlayer(sender, args);
                return;
            }
        }

        String prefix = plugin.getMainMessagesManager().getPrefix();
        String lobbyPermission = plugin.getMainPermissionsManager().getLobby();
        String bypassCooldownPermission = plugin.getMainPermissionsManager().getLobbyBypassCooldown();
        boolean bypassCooldownDefault = plugin.getMainPermissionsManager().isLobbyBypassCooldownDefault();
        boolean cooldownEnabled = plugin.getMainConfigManager().isLobbyCooldownEnabled();
        boolean lobbyPermissionDefault = plugin.getMainPermissionsManager().isLobbyDefault();
        boolean playerInCooldown = plugin.playerLobbyInCooldown(player);

        if (!permission(player, lobbyPermission, lobbyPermissionDefault)){
            sendMessage(sender, prefix, args, "NoPermission");
            return;
        }
        if (cooldownEnabled){
            if (cooldown(player, playerInCooldown, bypassCooldownPermission, bypassCooldownDefault)) {
                getTeleport(player, args);
                handleLobbyCooldown(player);
                return;
            }
            sendMessage(player, prefix, args, "InCooldown");
        } else {
            getTeleport(player, args);
        }
    }

    public void getTeleportOtherPlayer(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

        if (!plugin.getMainConfigManager().isLobbyEnabled()) {
            sendMessage(sender, prefix, args, "LobbyNoEnabled");
            return;
        }

        if (targetPlayer == null || !targetPlayer.isOnline()) {
            sendMessage(sender, prefix, args, "PlayerOffline");
            return;
        }

        if (!locations.contains("Lobby.world")) {
            sendMessage(sender, prefix, args, "NotExist");
            return;
        }

        getLobbyOtherPlayer(sender, targetPlayer, args);
    }

    public void getLobbyOtherPlayer(CommandSender sender, Player player, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        Location location = getLobbyLocation(locations);

        String targetPlayerName = args[0];
        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
        assert targetPlayer != null;

        sendMessage(player, prefix, args, "PlayerTeleport");

        sendMessage(sender, prefix, args, "PlayerTeleported");

        targetPlayer.teleport(location);
    }

    private boolean cooldown(Player player, boolean playerInCooldown, String bypassCooldownPermission, boolean bypassCooldownDefault) {
        return ((!playerInCooldown || bypassCooldownDefault || player.hasPermission(bypassCooldownPermission)));
    }

    private boolean permission(Player player, String lobbyPermission, boolean lobbyPermissionDefault){
        return (lobbyPermissionDefault || player.hasPermission(lobbyPermission));
    }

    public void getTeleport(CommandSender sender, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!plugin.getMainConfigManager().isLobbyEnabled()) {
            sendMessage(sender, prefix, args, "LobbyNoEnabled");
            return;
        }

        if (!locations.contains("Lobby.world")) {
            sendMessage(sender, prefix, args, "NotExist");
        } else {
            getLobby(sender, args);
        }
    }

    public void getLobby(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!validateLobbyWorld(locations, player, prefix, args)) {
            return;
        }

        Location lobbyLocation = getLobbyLocation(locations);
        int delay = plugin.getMainConfigManager().getLobbyTeleportDelay();
        DelayLobby delayManager = new DelayLobby(plugin, delay, player, lobbyLocation);

        String delayBypassPermission = plugin.getMainPermissionsManager().getLobbyBypassDelay();
        boolean delayBypassDefault = plugin.getMainPermissionsManager().isLobbyBypassDelayDefault();

        if (!plugin.getMainConfigManager().isLobbyTeleportDelayEnabled() || delayBypassDefault || player.hasPermission(delayBypassPermission)) {
            teleportPlayer(player, lobbyLocation, prefix, sender, args);
            return;
        }

        handleLobbyTeleportDelay(player, delayManager, prefix, args);
    }

    private boolean validateLobbyWorld(FileConfiguration locations, CommandSender sender, String prefix, String[] args) {
        String world = locations.getString("Lobby.world");

        if (world == null) {
            sendMessage(sender, prefix, args, "NotExist");
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
        assert world != null;
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    private void teleportPlayer(Player player, Location location, String prefix, CommandSender sender, String[] args) {
        sound(sender, args);
        executeCommands(sender);
        player.teleport(location);
        sendMessage(sender, prefix, args, "Teleported");
        plugin.addLobbyCooldown(player);
    }

    private void handleLobbyTeleportDelay(Player player, DelayLobby delayManager, String prefix, String[] args) {
        if (!plugin.playerInDelay(player)) {
            plugin.addPlayerTeleport(player);
            delayManager.DelayLobby();
            if (!Objects.equals(plugin.getMainConfigManager().getLobbyTeleportDelayMessageType(), "Chat")) {
                sendMessage(player, prefix, args, "MessageDelay");
            }
        } else {
            sendMessage(player, prefix, args, "InTeleport");
        }
    }

    public void sound(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (!plugin.getMainConfigManager().isLobbyTeleportSoundEnabled()) {
            return;
        }

        String soundName = plugin.getMainConfigManager().getLobbyTeleportSound();

        if (soundName == null) {
            handleNullSound(sender, prefix, args);
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            float volume = plugin.getMainConfigManager().getLobbyTeleportSoundVolume();
            float pitch = plugin.getMainConfigManager().getLobbyTeleportSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, args);
        }
    }

    private void handleNullSound(CommandSender sender, String prefix, String[] args) {
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault() || sender.hasPermission(permission)) {
            sendMessage(sender, prefix, args, "NullSound");
        }
    }

    private void handleInvalidSound(Player player, String prefix, String[] args) {
        sendMessage(player, prefix, args, "InvalidSound");
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

    public void handleLobbyCooldown(Player player){
        CooldownTimeProvider timeProvider = new LobbyCooldownProvider(plugin, player);
        int time = plugin.getMainConfigManager().getLobbyCooldownTime();
        CooldownLobby c = new CooldownLobby(plugin, timeProvider, time, player);
        c.cooldownLobby();
    }

    public void sendMessage (CommandSender sender, String prefix, String[] args, String reason){
        switch (reason) {
            case "NoPermission": {
                String message = plugin.getMainMessagesManager().getPermissionDenied();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsoleUsage": {
                String message = plugin.getMainMessagesManager().getLobbyOtherConsoleUsage();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerOffline": {
                String message = plugin.getMainMessagesManager().getLobbyOtherPlayerOffline();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsolePlayerTeleport": {
                String replaced = plugin.getMainConfigManager().getReplacedMessagesConsole();
                String message = plugin.getMainMessagesManager().getLobbyOtherConsolePlayerTeleport();
                message = prefix + message.replace("%sender%", replaced);
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "LobbyNoEnabled": {
                String message = plugin.getMainMessagesManager().getLobbyIsNotEnabled();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NotExist": {
                String message = plugin.getMainMessagesManager().getLobbyDoesNotExist();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerTeleported": {
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                assert targetPlayer != null;

                String message = plugin.getMainMessagesManager().getLobbyOtherPlayerTeleported();
                message = prefix + message.replace("%player%", targetPlayer.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "PlayerTeleport": {
                String message = plugin.getMainMessagesManager().getLobbyOtherPlayerTeleport();
                message = prefix + message.replace("%player%", sender.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InCooldown": {
                Player player = (Player) sender;
                String message = plugin.getMainMessagesManager().getLobbyInCooldown();
                int remainingTime = CooldownLobby.getRemainingTime(player);
                message = prefix + message.replace("%time%", String.valueOf(remainingTime));
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "Teleported": {
                String message = plugin.getMainMessagesManager().getLobbyTeleported();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "MessageDelay": {
                int time = plugin.getMainConfigManager().getLobbyTeleportDelay();
                String message = plugin.getMainMessagesManager().getLobbyMessageDelayTeleport();
                message = prefix + message.replace("%time%", String.valueOf(time));
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InTeleport": {
                String message = plugin.getMainMessagesManager().getLobbyInTeleport();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "NullSound": {
                String message = plugin.getMainMessagesManager().getLobbyNullSound();
                message = prefix + message;
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "InvalidSound": {
                String soundName = plugin.getMainConfigManager().getLobbyTeleportSound();
                String message = plugin.getMainMessagesManager().getLobbyInvalidSound();
                message = prefix + message.replace("%sound%", soundName);
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
            case "ConsolePlayerTeleported": {
                String targetPlayerName = args[0];
                Player targetPlayer = Bukkit.getPlayer(targetPlayerName);
                assert targetPlayer != null;

                String message = plugin.getMainMessagesManager().getLobbyOtherConsoleTeleported();
                message = prefix + message.replace("%player%", targetPlayer.getName());
                sender.sendMessage(MessagesUtils.getColoredMessage(message));
                break;
            }
        }
    }
}