package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import java.util.List;
import java.util.UUID;

public class LastLocation implements Listener {
    private final DeluxeSpawn plugin;

    public LastLocation(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void changeWorld(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        List<String> teleportMode = plugin.getMainConfigManager().getLobbyLastLocationTeleportMode();
        if (teleportMode.contains("OnChangeWorld")){
            boolean oneTime = plugin.getMainConfigManager().isLobbyLastLocationChangeWorldsOneTime();
            if (oneTime){
                UUID uuid = player.getUniqueId();
                FileConfiguration playerConfig = plugin.getPlayerDataManager().getPlayerConfig(uuid);
                String OneTime = playerConfig.getString("LastLocation.OneTime");
                boolean inOneTime = plugin.playerLastLocationOneTime(player);
                assert OneTime != null;
                if (inOneTime){
                    return;
                }
                    teleportPlayer(player, event);
                return;
            }
                teleportPlayer(player, event);
        }
    }

    public void teleportPlayer(Player player, PlayerChangedWorldEvent event){
        String changeWorldType = plugin.getMainConfigManager().getLobbyLastLocationChangeWorldType();
        if (changeWorldType.equals("AnyWorld")){
            getLastLocation(player);
            return;
        }

        if (changeWorldType.equals("SpecifiedWorlds")){
            String playerWorld = event.getPlayer().getWorld().getName();
            List<String> specifiedWorlds = plugin.getMainConfigManager().getLobbyLastLocationChangeWorldsSpecified();

            if (specifiedWorlds.contains(playerWorld)) {
                getLastLocation(player);
            }
        }
    }

    public void getLastLocation(Player player) {
        UUID uuid = player.getUniqueId();
        FileConfiguration playerConfig = plugin.getPlayerDataManager().getPlayerConfig(uuid);
        if (playerConfig.contains("LastLocation.world")) {
            String worldName = playerConfig.getString("LastLocation.world");
            double x = playerConfig.getDouble("LastLocation.x");
            double y = playerConfig.getDouble("LastLocation.y");
            double z = playerConfig.getDouble("LastLocation.z");
            float yaw = (float) playerConfig.getDouble("LastLocation.yaw");
            float pitch = (float) playerConfig.getDouble("LastLocation.pitch");

            assert worldName != null;
            Location lastLocation = new Location(Bukkit.getWorld(worldName), x, y, z, yaw, pitch);
            player.teleport(lastLocation);
            plugin.addLastLocationOneTime(player);
            soundLastLocation(player);
            executeCommands(player);
        }
    }

    public void soundLastLocation(Player player) {
        String prefix = plugin.getMainMessagesManager().getPrefix();
        boolean enabled = plugin.getMainConfigManager().isLobbyLastLocationSoundEnabled();

        if (!enabled) {
            return;
        }

        String soundName = plugin.getMainConfigManager().getLobbyTeleportSound();

        if (soundName == null) {
            handleNullSound(player, prefix);
            return;
        }

        try {
            Sound sound = Sound.valueOf(soundName);
            float volume = plugin.getMainConfigManager().getLobbyLastLocationSoundVolume();
            float pitch = plugin.getMainConfigManager().getLobbyLastLocationSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        } catch (IllegalArgumentException e) {
            handleInvalidSound(player, prefix, soundName);
        }
    }

    private void handleNullSound(Player player, String prefix) {
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault() || player.hasPermission(permission)) {
            String message = prefix + plugin.getMainMessagesManager().getLastLocationNullSound();
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    private void handleInvalidSound(Player player, String prefix, String soundName) {
        String message = prefix + plugin.getMainMessagesManager().getLastLocationInvalidSound().replace("%sound%", soundName);
        player.sendMessage(message);
    }

    public void executeCommands(Player player) {
        if (plugin.getMainConfigManager().isLobbyLastLocationCommandsEnabled()) {

            List<String> playerCommands = plugin.getMainConfigManager().getLobbyLastLocationCommandsPlayer();
            List<String> consoleCommands = plugin.getMainConfigManager().getLobbyLastLocationCommandsConsole();

            for (String command : playerCommands) {
                String replacedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(player, replacedCommand);
            }

            CommandSender consoleSender = Bukkit.getConsoleSender();
            for (String command : consoleCommands) {
                String replacedCommand = command.replace("%player%", player.getName());
                Bukkit.dispatchCommand(consoleSender, replacedCommand);
            }
        }
    }
}

