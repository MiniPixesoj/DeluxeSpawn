package com.pixesoj.listeners;

import com.pixesoj.deluxespawn.DeluxeSpawn;
import com.pixesoj.utils.spigot.CommandUtils;
import com.pixesoj.utils.spigot.MessagesUtils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class OnJoin implements Listener {
    private final DeluxeSpawn plugin;

    public OnJoin(DeluxeSpawn plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        notifyUpdate(event);
        teleportOnJoin(event);
        teleportOnFirstJoin(event);
        plugin.removeLastLocationOneTime(player);
    }

    public void notifyUpdate (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotifyUpdate();
        if (plugin.getMainPermissionsManager().isNotifyUpdateDefault()) {
            player.sendMessage(MessagesUtils.getColoredMessage(prefix + " &cThere is a new version available. &6(&8" + plugin.getUpdateCheckerManager().getLatestVersion() + "&6)"));
            player.sendMessage(MessagesUtils.getColoredMessage("&cYou can download it at: &ahttps://spigotmc.org/resources/111403/"));
        } else {
            if (player.hasPermission(permission)) {
                player.sendMessage(MessagesUtils.getColoredMessage(prefix + " &cThere is a new version available. &6(&8" + plugin.getUpdateCheckerManager().getLatestVersion() + "&6)"));
                player.sendMessage(MessagesUtils.getColoredMessage("&cYou can download it at: &ahttps://spigotmc.org/resources/111403/"));
            }
        }
    }

    public void teleportOnJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        playerTeleportOnJoin(event);
        onJoinSendMessage(event);
        onJoinSound(event);

        List<String> cmdPlayer = plugin.getMainConfigManager().getTeleportOnJoinCommandsPlayer();
        List<String> cmdConsole = plugin.getMainConfigManager().getTeleportOnJoinCommandsConsole();
        boolean enabled = plugin.getMainConfigManager().isTeleportOnJoinCommandsEnabled();
        CommandUtils.executeCommands(player, enabled, cmdPlayer, cmdConsole);
    }

    public void teleportOnFirstJoin (PlayerJoinEvent event){
        Player player = event.getPlayer();
        if (!event.getPlayer().hasPlayedBefore()) {
            playerTeleportOnFirstJoin(event);
            onFirstJoinSendMessage(event);
            onFirstJoinSound(event);


            List<String> cmdPlayer = plugin.getMainConfigManager().getTeleportOnFirstJoinCommandsPlayer();
            List<String> cmdConsole = plugin.getMainConfigManager().getTeleportOnFirstJoinCommandsConsole();
            boolean enabled = plugin.getMainConfigManager().isTeleportOnFirstJoinCommandsEnabled();
            CommandUtils.executeCommands(player, enabled, cmdPlayer, cmdConsole);
        }
    }

    public void playerTeleportOnJoin(PlayerJoinEvent event){
        if (plugin.getMainConfigManager().isTeleportOnJoinEnabled()){
            if (plugin.getMainConfigManager().getTeleportOnJoinDestinationPlace().equals("Lobby")){
                onJoinGetLobby(event);
                return;
            }
            if (plugin.getMainConfigManager().getTeleportOnJoinDestinationPlace().equals("Spawn")){
                onJoinGetSpawn(event);
            } else {
                onJoinDestinationInvalid(event);
            }
        }
    }

    public void playerTeleportOnFirstJoin (PlayerJoinEvent event){

            if (plugin.getMainConfigManager().isTeleportOnFirstJoinJoinEnabled()){
                if (plugin.getMainConfigManager().getTeleportOnFirstJoinDestinationPlace().equals("Lobby")){
                    onFirstJoinGetLobby(event);
                    return;
                }
                if (plugin.getMainConfigManager().getTeleportOnFirstJoinDestinationPlace().equals("Spawn")){
                    onFirstJoinGetSpawn(event);
                } else {
                    onFirstJoinDestinationInvalid(event);
                }
            }
    }

    public void onJoinGetLobby (PlayerJoinEvent event){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = event.getPlayer();
        double x = locations.getDouble("Lobby.x");
        double y = locations.getDouble("Lobby.y");
        double z = locations.getDouble("Lobby.z");
        float yaw = (float) locations.getDouble("Lobby.yaw");
        float pitch = (float) locations.getDouble("Lobby.pitch");
        String world = locations.getString("Lobby.world");
        assert world != null;
        Location lobbyLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        if(!locations.contains("Lobby.x")){
            onJoinDoesNotExist(event);
        } else {
            player.teleport(lobbyLocation);
        }
    }

    public void onJoinDoesNotExist (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (player.isOp()){
            String destination = plugin.getMainConfigManager().getTeleportOnJoinDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnJoinDoesNotExist().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void onJoinSendMessage (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (plugin.getMainConfigManager().isTeleportOnJoinSendMessage()){
            String destination = plugin.getMainConfigManager().getTeleportOnJoinDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnJoinSendMessage().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void onJoinGetSpawn (PlayerJoinEvent event){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = event.getPlayer();
        if (plugin.getMainSpawnConfigManager().isByWorld()){
            String world = plugin.getMainConfigManager().getTeleportOnJoinSpawn();

            double x = locations.getDouble("SpawnByWorld." + world + ".x");
            double y = locations.getDouble("SpawnByWorld." + world + ".y");
            double z = locations.getDouble("SpawnByWorld." + world + ".z");
            float yaw = (float) locations.getDouble("SpawnByWorld." + world + ".yaw");
            float pitch = (float) locations.getDouble("SpawnByWorld." + world + ".pitch");
            Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

            if(!locations.contains("SpawnByWorld." + world + ".x")){
                onJoinDoesNotExist(event);
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
                onJoinDoesNotExist(event);
            } else {
                player.teleport(spawnLocation);
            }
        }
    }

    public void onJoinDestinationInvalid(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String destination = plugin.getMainConfigManager().getTeleportOnJoinDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnJoinDestinationInvalid().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        } else {
            if (player.hasPermission(permission)) {
                String destination = plugin.getMainConfigManager().getTeleportOnJoinDestinationPlace();
                String message = prefix + plugin.getMainMessagesManager().getOnJoinDestinationInvalid().replace("%destination%", destination);
                player.sendMessage(MessagesUtils.getColoredMessage(message));
            }
        }
    }

    public  void onFirstJoinGetLobby (PlayerJoinEvent event){
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = event.getPlayer();
        double x = locations.getDouble("Lobby.x");
        double y = locations.getDouble("Lobby.y");
        double z = locations.getDouble("Lobby.z");
        float yaw = (float) locations.getDouble("Lobby.yaw");
        float pitch = (float) locations.getDouble("Lobby.pitch");
        String world = locations.getString("Lobby.world");
        assert world != null;
        Location lobbyLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);

        if(!locations.contains("Lobby.x")){
            onFirstJoinDoesNotExist(event);
        } else {
            player.teleport(lobbyLocation);
        }
    }

    public void onFirstJoinDoesNotExist (PlayerJoinEvent event){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        Player player = event.getPlayer();
        if (player.isOp()){
            String destination = plugin.getMainConfigManager().getTeleportOnFirstJoinDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnFirstJoinDoesNotExist().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
    }

    public void onFirstJoinSendMessage (PlayerJoinEvent event){
        String prefix = plugin.getMainMessagesManager().getPrefix();
        Player player = event.getPlayer();
        if (plugin.getMainConfigManager().isTeleportOnFirstJoinSendMessage()){
            String destination = plugin.getMainConfigManager().getTeleportOnFirstJoinDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnFirstJoinSendMessage().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        }
        if (plugin.getMainConfigManager().isTeleportOnFirstJoinWelcomeMessage()) {
            List<String> message = plugin.getMainMessagesManager().getOnFirstJoinSendWelcomeMessage();

            for (String m : message) {
                String replacedMessage = m.replace("%player%", player.getName());

                if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    replacedMessage = PlaceholderAPI.setPlaceholders(player, replacedMessage);
                }
                player.sendMessage(MessagesUtils.getColoredMessage(replacedMessage));
            }
        }
    }

    public void onFirstJoinGetSpawn (PlayerJoinEvent event) {
        FileConfiguration locations = plugin.getLocationsManager().getLocationsFile();
        Player player = event.getPlayer();
        if (plugin.getMainSpawnConfigManager().isByWorld()) {
            String world = plugin.getMainConfigManager().getTeleportOnFirstJoinSpawn();

            double x = locations.getDouble("SpawnByWorld." + world + ".x");
            double y = locations.getDouble("SpawnByWorld." + world + ".y");
            double z = locations.getDouble("SpawnByWorld." + world + ".z");
            float yaw = (float) locations.getDouble("SpawnByWorld." + world + ".yaw");
            float pitch = (float) locations.getDouble("SpawnByWorld." + world + ".pitch");
            Location spawnLocation = new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
            if (!locations.contains("SpawnByWorld." + world + ".x")) {
                onFirstJoinDoesNotExist(event);
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
                onFirstJoinDoesNotExist(event);
            } else {
                player.teleport(spawnLocation);
            }
        }
    }

    public void onFirstJoinDestinationInvalid (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String destination = plugin.getMainConfigManager().getTeleportOnFirstJoinDestinationPlace();
            String message = prefix + plugin.getMainMessagesManager().getOnFirstJoinDestinationInvalid().replace("%destination%", destination);
            player.sendMessage(MessagesUtils.getColoredMessage(message));
        } else {
            if (player.hasPermission(permission)) {
                String destination = plugin.getMainConfigManager().getTeleportOnFirstJoinDestinationPlace();
                String message = prefix + plugin.getMainMessagesManager().getOnFirstJoinDestinationInvalid().replace("%destination%", destination);
                player.sendMessage(MessagesUtils.getColoredMessage(message));
            }
        }
    }

    public void onJoinSound (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (plugin.getMainConfigManager().isTeleportOnJoinEnabled()) {
            String soundName = plugin.getMainConfigManager().getTeleportOnJoinSound();
            if (soundName == null){
                onJoinNullSound(event);
                return;
            }
            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getOnJoinInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }
            float volume = plugin.getMainConfigManager().getTeleportOnJoinSoundVolume();
            float pitch = plugin.getMainConfigManager().getTeleportOnJoinSoundPitch();
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void onJoinNullSound (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String m = prefix + plugin.getMainMessagesManager().getOnJoinNullSound();
            player.sendMessage(MessagesUtils.getColoredMessage(m));
        } else {
            if (player.hasPermission(permission)) {
                String m = prefix + plugin.getMainMessagesManager().getOnJoinNullSound();
                player.sendMessage(MessagesUtils.getColoredMessage(m));
            }
        }
    }

    public void onFirstJoinSound (PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        if (plugin.getMainConfigManager().isTeleportOnFirstJoinSoundEnabled()) {
            String soundName = plugin.getMainConfigManager().getTeleportOnFirstJoinSound();
            if (soundName == null){
                onFirstJoinNullSound(event);
                return;
            }
            Sound sound;
            try {
                sound = Sound.valueOf(soundName);
            } catch (IllegalArgumentException e) {
                String m = prefix + plugin.getMainMessagesManager().getOnFirstJoinInvalidSound().replace("%sound%", soundName);
                player.sendMessage(m);
                return;
            }
            float volume = plugin.getMainConfigManager().getTeleportOnFirstJoinSoundVolume();
            float pitch = plugin.getMainConfigManager().getTeleportOnFirstJoinSoundPitch();
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public void onFirstJoinNullSound (PlayerJoinEvent event){
        Player player = event.getPlayer();
        String prefix = plugin.getMainMessagesManager().getPrefix();
        String permission = plugin.getMainPermissionsManager().getNotify();
        if (plugin.getMainPermissionsManager().isNotifyDefault()) {
            String m = prefix + plugin.getMainMessagesManager().getOnFirstJoinNullSound();
            player.sendMessage(MessagesUtils.getColoredMessage(m));
        } else {
            if (player.hasPermission(permission)) {
                String m = prefix + plugin.getMainMessagesManager().getOnFirstJoinNullSound();
                player.sendMessage(MessagesUtils.getColoredMessage(m));
            }
        }
    }

}
