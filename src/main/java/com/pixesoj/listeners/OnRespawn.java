package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.CommandUtils;
import com.pixesoj.utils.spigot.MessagesUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class OnRespawn implements Listener {
    private final DeluxeSpawn plugin;

    public OnRespawn(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            respawn(event);
        }, 1L);

    }

    public void respawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        getTeleportIgnoreBed(event);
        sendTeleportMessage(event);
        blindnessTeleport(event);
        sound(event);

        List<String> cmdPlayer = plugin.getMainConfigManager().getTeleportOnRespawnCommandsPlayer();
        List<String> cmdConsole = plugin.getMainConfigManager().getTeleportOnRespawnCommandsConsole();
        boolean enabled = plugin.getMainConfigManager().isTeleportOnRespawnCommandsEnabled();
        CommandUtils.executeCommands(player, enabled, cmdPlayer, cmdConsole);
    }

    public void getTeleportIgnoreBed (PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (plugin.getMainConfigManager().isTeleportOnRespawnIgnoreBed()){
            playerTeleport(event);
        } else {
            if (player.getBedSpawnLocation() != null) {
                return;
            }
            playerTeleport(event);
        }
    }

    public void playerTeleport (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if (plugin.getMainConfigManager().isTeleportOnRespawnEnabled()){
            List<String> ignoredWorlds = plugin.getMainConfigManager().getTeleportOnRespawnIgnoredWorlds();
            String playerWorld = player.getWorld().getName();
            if (ignoredWorlds.contains(playerWorld)){
                return;
            }
            if (plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace().contains("Lobby")){
                getLobbyLocation(event);
            } else if (plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace().contains("SpawnPlayerWorld")) {
                getSpawnPlayerWorldLocation(event);
            } else if (plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace().contains("Spawn")) {
                getSpawnLocation(event);
            } else {
                destinationInvalid(event);
            }
        }
    }

    public void sendTeleportMessage (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (plugin.getMainConfigManager().isTeleportOnRespawnSendMessageTeleport()){
            String destination = plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnRespawnSendMessage().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void getLobbyLocation (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        double x = locations.getDouble("Lobby.x");
        double y = locations.getDouble("Lobby.y");
        double z = locations.getDouble("Lobby.z");
        float yaw = (float) locations.getDouble("Lobby.yaw");
        float pitch = (float) locations.getDouble("Lobby.pitch");
        String world = locations.getString("Lobby.world");
        assert world != null;
        Location lobbyLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        if(!locations.contains("Lobby.x")){
            respawnDoesNotExist(event);
        } else {
            player.teleport(lobbyLocation);
        }
    }

    public void getSpawnPlayerWorldLocation (PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String world = player.getWorld().getName();

        double x = locations.getDouble("SpawnByWorld." + world + ".x");
        double y = locations.getDouble("SpawnByWorld." + world + ".y");
        double z = locations.getDouble("SpawnByWorld." + world + ".z");
        float yaw = (float) locations.getDouble("SpawnByWorld." + world + ".yaw");
        float pitch = (float) locations.getDouble("SpawnByWorld." + world + ".pitch");
        Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        if(!locations.contains("SpawnByWorld." + world + ".x")){
            respawnDoesNotExist(event);
        } else {
            player.teleport(spawnLocation);
        }
    }

    public void getSpawnLocation (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        if (plugin.getMainSpawnConfigManager().isByWorld()){
            String world = plugin.getMainConfigManager().getTeleportOnRespawnDestinationSpawn();

            double x = locations.getDouble("SpawnByWorld." + world + ".x");
            double y = locations.getDouble("SpawnByWorld." + world + ".y");
            double z = locations.getDouble("SpawnByWorld." + world + ".z");
            float yaw = (float) locations.getDouble("SpawnByWorld." + world + ".yaw");
            float pitch = (float) locations.getDouble("SpawnByWorld." + world + ".pitch");
            Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

            if(!locations.contains("SpawnByWorld." + world + ".x")){
                respawnDoesNotExist(event);
            } else {
                player.teleport(spawnLocation);
            }
        } else {
            double x = locations.getDouble("Spawn.x");
            double y = locations.getDouble("Spawn.y");
            double z = locations.getDouble("Spawn.z");
            float yaw = (float) locations.getDouble("Spawn.yaw");
            float pitch = (float) locations.getDouble("Spawn.pitch");
            String world = locations.getString("Spawn.world");
            assert world != null;
            Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

            if(!locations.contains("Spawn.world")){
                respawnDoesNotExist(event);
            } else {
                player.teleport(spawnLocation);
            }
        }
    }

    public void destinationInvalid (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String destination = plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnRespawnDestinationInvalid().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        } else {
            if (player.hasPermission(permission)) {
                String destination = plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace();
                String message = prefix + plugin.getMainMessagesManager().getOnRespawnDestinationInvalid().replace("%destination%", destination);
                player.sendMessage(MessagesUtils.getColoredMessage(message));
            }
        }
    }

    public void respawnDoesNotExist (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String destination = plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnRespawnDoesNotExist().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        } else {
            if (player.hasPermission(permission)) {
                String destination = plugin.getMainConfigManager().getTeleportOnRespawnDestinationPlace();
                String message = prefix + plugin.getMainMessagesManager().getOnRespawnDoesNotExist().replace("%destination%", destination);
                player.sendMessage(MessagesUtils.getColoredMessage(message));
            }
        }
    }

    public void blindnessTeleport (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        int time = plugin.getMainConfigManager().getTeleportOnRespawnBlindnessTime();
        if (plugin.getMainConfigManager().isTeleportOnRespawnBlindness()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * time, 2));
        }
    }

    public void sound (PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (plugin.getMainConfigManager().isTeleportOnRespawnSoundEnabled()) {
            String soundName = plugin.getMainConfigManager().getTeleportOnRespawnSound();
            if (soundName == null){
                nullSound(event);
                return;
            }
            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getOnRespawnInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }
            float volume = plugin.getMainConfigManager().getTeleportOnRespawnSoundVolume();
            float pitch = plugin.getMainConfigManager().getTeleportOnRespawnSoundPitch();
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void nullSound (PlayerRespawnEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String m = prefix + plugin.getMainMessagesManager().getOnRespawnNullSound();
            player.sendMessage(MessagesUtils.getColoredMessage(m));
        } else {
            if (player.hasPermission(permission)) {
                String m = prefix + plugin.getMainMessagesManager().getOnRespawnNullSound();
                player.sendMessage(MessagesUtils.getColoredMessage(m));
            }
        }
    }
}

