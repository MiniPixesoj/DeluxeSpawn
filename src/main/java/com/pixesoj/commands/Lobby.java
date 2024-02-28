package com.pixesoj.commands;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.managers.DelayManagerLobby;
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

public class Lobby implements CommandExecutor {
    private DeluxeSpawn plugin;

    public Lobby(DeluxeSpawn deluxeSpawn) {
        this.plugin = deluxeSpawn;
    }

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (!(sender instanceof Player)) {
            String message = prefix + plugin.getMainMessagesManager().getCommandDeniedConsole();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
            return true;
        }
        Player player = (Player) sender;

        if(!plugin.getMainConfigManager().isLobbyEnabled()){
            String message = prefix + plugin.getMainMessagesManager().getLobbyIsNotEnabled();
            sender.sendMessage(MessagesUtils.getColoredMessage(message));
            return true;
        }

        if (!player.hasPermission("deluxespawn.command.lobby")) {
            sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getPermissionDenied()));
            return true;
        }

        if (!locations.contains("Lobby.world")){
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyDoesNotExist()));
            return true;
        } else {
            LobbyGlobal(sender);
            return true;
        }
    }

    public void LobbyGlobal (CommandSender sender){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = (Player) sender;
        String world = locations.getString("Lobby.world");
        String prefix = plugin.getMainMessagesManager().getPrefix();

        if (world == null) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyDoesNotExist()));
            return;
        }
        double x = locations.getDouble("Lobby.x");
        double y = locations.getDouble("Lobby.y");
        double z = locations.getDouble("Lobby.z");
        float yaw = (float) locations.getDouble("Lobby.yaw");
        float pitch = (float) locations.getDouble("Lobby.pitch");
        Location lobbyLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
        int delay = plugin.getMainConfigManager().getLobbyTeleportDelay();
        DelayManagerLobby d = new DelayManagerLobby(plugin, delay, player, lobbyLocation);

        if (!plugin.getMainConfigManager().isLobbyTeleportDelayEnabled()){
            player.teleport(lobbyLocation);
            lobbySound(sender);
            lobbyExecuteCommands(sender);
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyTeleported()));
            return;
        }

        if (!plugin.playerInDelay(player)){
            int time = plugin.getMainConfigManager().getLobbyTeleportDelay();
            String message = plugin.getMainMessagesManager().getLobbyMessageDelayTeleport();
            message = message.replace("%time%", String.valueOf(time));
            player.sendMessage(MessagesUtils.getColoredMessage(message));
            plugin.addPlayer(player);
            d.DelayLobbyGlobal();
            return;
        }
        sender.sendMessage(MessagesUtils.getColoredMessage(prefix + plugin.getMainMessagesManager().getLobbyInTeleport()));
    }

    public void lobbySound(CommandSender sender) {
        Player player = (Player) sender;

        if (plugin.getMainConfigManager().isLobbyTeleportSoundEnabled()) {
            String soundName = plugin.getMainConfigManager().getLobbyTeleportSound();
            String prefix = plugin.getMainMessagesManager().getPrefix();
            if (soundName == null){
                if (player.hasPermission("deluxespawn.notify") || player.isOp()){
                    String m = prefix + plugin.getMainMessagesManager().getLobbyNullSound();
                    sender.sendMessage(MessagesUtils.getColoredMessage(m));
                }
                return;
            }

            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getLobbyInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }

            float volume = plugin.getMainConfigManager().getLobbyTeleportSoundVolume();
            float pitch = plugin.getMainConfigManager().getLobbyTeleportSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void lobbyExecuteCommands(CommandSender sender) {
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
}