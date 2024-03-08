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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class OnVoid implements Listener {
    private final DeluxeSpawn plugin;

    public OnVoid(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (!plugin.getMainConfigManager().isTeleportOnVoidEnabled()) {
            return;
        }

        Player player = event.getPlayer();
        List<String> ignoredWorlds = plugin.getMainConfigManager().getTeleportOnVoidIgnoreWorlds();
        String playerWorld = player.getWorld().getName();

        if (ignoredWorlds.contains(playerWorld)) {
            return;
        }

        Location playerLocation = player.getLocation();
        double y = playerLocation.getY();
        double voidLimit = plugin.getMainConfigManager().getTeleportOnVoidHeight();

        if (y < voidLimit) {
            playerTeleport(event);
            BlindnessTeleport(event);
            SendMessage(event);
            TeleportSound(event);
            ExecuteCommands(event);
        }
    }

    public void ExecuteCommands(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getMainConfigManager().isTeleportOnVoidCommandsEnabled()) {

            List<String> playerCommands = plugin.getMainConfigManager().getTeleportOnVoidCommandsPlayer();
            List<String> consoleCommands = plugin.getMainConfigManager().getTeleportOnVoidCommandsConsole();

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

    public void TeleportSound(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (plugin.getMainConfigManager().isTeleportOnVoidSoundEnabled()) {
            String soundName = plugin.getMainConfigManager().getTeleportOnVoidSound();
            String prefix = plugin.getMainMessagesManager().getPrefix();
            if (soundName == null){
                String permission = plugin.getMainPermissionsManager().getNotify();
                if (plugin.getMainPermissionsManager().isNotifyDefault()) {
                    String m = prefix + plugin.getMainMessagesManager().getSpawnNullSound();
                    player.sendMessage(MessagesUtils.getColoredMessage(m));
                } else {
                    if (player.hasPermission(permission)) {
                        String m = prefix + plugin.getMainMessagesManager().getSpawnNullSound();
                        player.sendMessage(MessagesUtils.getColoredMessage(m));
                        return;
                    }
                }
                return;
            }

            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getOnVoidInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }

            float volume = plugin.getMainConfigManager().getTeleportOnVoidSoundVolume();
            float pitch = plugin.getMainConfigManager().getTeleportOnVoidSoundPitch();

            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void SendMessage(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if (plugin.getMainConfigManager().isTeleportOnVoidSendMessage()){
            String d = plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace();
            String prefix = plugin.getMainMessagesManager().getPrefix();
            String m = prefix + plugin.getMainMessagesManager().getOnVoidTeleportMessage().replace("%destination%", d);
            player.sendMessage(MessagesUtils.getColoredMessage(m));
        }
    }

    public void BlindnessTeleport (PlayerMoveEvent event){
        Player player = event.getPlayer();
        int time = plugin.getMainConfigManager().getTeleportOnVoidBlindnessTime();
        if (plugin.getMainConfigManager().isTeleportOnVoidBlindness()){
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * time, 2));
        }
    }

    public void playerTeleport (PlayerMoveEvent event){
        Player player = event.getPlayer();
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace().equals("Lobby")){
            String world = locations.getString("Lobby.world");
            double x = locations.getDouble("Lobby.x");
            double y = locations.getDouble("Lobby.y");
            double z = locations.getDouble("Lobby.z");
            float yaw = (float) locations.getDouble("Lobby.yaw");
            float pitch = (float) locations.getDouble("Lobby.pitch");
            assert world != null;
            Location lobbyLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

            player.teleport(lobbyLocation);
            return;
        }
        if (plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace().equals("Spawn")){
            if (plugin.getMainConfigManager().isSpawnByWorld()){
                String world = plugin.getMainConfigManager().getTeleportOnVoidDestinationSpawn();
                if (locations.contains("SpawnByWorld." + world)){
                    double x = locations.getDouble("SpawnByWorld." + world + ".x");
                    double y = locations.getDouble("SpawnByWorld." + world + ".y");
                    double z = locations.getDouble("SpawnByWorld." + world + ".z");
                    float yaw = (float) locations.getDouble("SpawnByWorld." + world + ".yaw");
                    float pitch = (float) locations.getDouble("SpawnByWorld." + world + ".pitch");
                    Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

                    player.teleport(spawnLocation);
                } else {
                    String permission = plugin.getMainPermissionsManager().getNotify();
                    if (plugin.getMainPermissionsManager().isNotifyDefault()) {
                        String destination = plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace();
                        String message = prefix + plugin.getMainMessagesManager().getOnVoidDestinationInvalid().replace("%destination%", destination);
                        player.sendMessage(MessagesUtils.getColoredMessage(message));
                    } else {
                        if (player.hasPermission(permission)) {
                            String destination = plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace();
                            String message = prefix + plugin.getMainMessagesManager().getOnVoidDestinationInvalid().replace("%destination%", destination);
                            player.sendMessage(MessagesUtils.getColoredMessage(message));
                        }
                    }
                }
            } else {
                String world = locations.getString("Spawn.world");
                double x = locations.getDouble("Spawn.x");
                double y = locations.getDouble("Spawn.y");
                double z = locations.getDouble("Spawn.z");
                float yaw = (float) locations.getDouble("Spawn.yaw");
                float pitch = (float) locations.getDouble("Spawn.pitch");
                assert world != null;
                Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

                player.teleport(spawnLocation);
            }
        } else {
            String permission = plugin.getMainPermissionsManager().getNotify();
            if (plugin.getMainPermissionsManager().isNotifyDefault()) {
                String destination = plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace();
                String message = prefix + plugin.getMainMessagesManager().getOnVoidDestinationInvalid().replace("%destination%", destination);
                player.sendMessage(MessagesUtils.getColoredMessage(message));
            } else {
                if (player.hasPermission(permission)) {
                    String destination = plugin.getMainConfigManager().getTeleportOnVoidDestinationPlace();
                    String message = prefix + plugin.getMainMessagesManager().getOnVoidDestinationInvalid().replace("%destination%", destination);
                    player.sendMessage(MessagesUtils.getColoredMessage(message));
                }
            }
        }
    }
}